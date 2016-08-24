/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.ciwise.blackhole.domain.AccountEntry;
import org.ciwise.blackhole.domain.GenAccount;
import org.ciwise.blackhole.service.AccountEntryService;
import org.ciwise.blackhole.service.GenAccountService;
import org.ciwise.blackhole.service.TrialBalanceService;
import org.ciwise.blackhole.service.dto.AccountBalance;
import org.ciwise.blackhole.service.util.CurrencyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
@Service
@Transactional
public class TrialBalanceServiceImpl implements TrialBalanceService {

    @Inject
    private AccountEntryService accountEntryService;

    @Inject
    private GenAccountService genAccountService;
    
    /* (non-Javadoc)
     * @see org.ciwise.blackhole.service.TrialBalanceService#getAllAccountBalances()
     */
    @Override
    public List<AccountBalance> getAllAccountBalances() {
        
        // create an ArrayList of AccountBalance for return
        List<AccountBalance> balances = new ArrayList<AccountBalance>();
        
        // get chart numbers for iteration
        List<GenAccount> genAccounts = genAccountService.findAll();
        
        // load all chart numbers 
        for (GenAccount genAcct: genAccounts) {
            
            // get all entries for unique chart number
            List<AccountEntry> accountEntries = accountEntryService.findByCno(genAcct.getCno());

            AccountEntry entryLatest = new AccountEntry();
                int maxId = 0;
                
                for (AccountEntry entry: accountEntries) {
                    if (entry.getId().intValue() > maxId) {
                        maxId = entry.getId().intValue();
                        entryLatest.setDebitbalance(entry.getDebitbalance());
                        entryLatest.setCreditbalance(entry.getCreditbalance());
                        entryLatest.setCno(entry.getCno());
                    }
                }
                
                // write AccountBalance object and add to List<AccountBalance>
                AccountBalance balanceObj = new AccountBalance();
                balanceObj.setCno(entryLatest.getCno());

                if (entryLatest.getDebitbalance() != null) {
                    if (entryLatest.getDebitbalance().contains("(")) {
                        String tmp = CurrencyUtil.fixNegativeCurrency(entryLatest.getDebitbalance());
                        entryLatest.setDebitbalance(tmp);
                    }
                    balanceObj.setBalance(entryLatest.getDebitbalance());
                    balanceObj.setType("Debit");
                }
                if (entryLatest.getCreditbalance() != null) {
                    if (entryLatest.getCreditbalance().contains("(")) {
                        String tmp = CurrencyUtil.fixNegativeCurrency(entryLatest.getCreditbalance());
                        entryLatest.setCreditbalance(tmp);
                    }
                    balanceObj.setBalance(entryLatest.getCreditbalance());
                    balanceObj.setType("Credit");
                }

                balances.add(balanceObj);
        }
        return balances;
    }

    /* (non-Javadoc)
     * @see org.ciwise.blackhole.service.TrialBalanceService#getDebitAccountsTotal()
     */
    @Override
    public AccountBalance getDebitAccountsTotal() {
        List<AccountBalance> debits = getDebitAccountBalances();
        String total = "0.00";
        // String addCurrency(String a, String b)
        for (AccountBalance bal: debits) {
            total = CurrencyUtil.addCurrency(total, bal.getBalance());
        }
        AccountBalance balance = new AccountBalance();
        balance.setBalance(total);
        return balance;
    }

    /* (non-Javadoc)
     * @see org.ciwise.blackhole.service.TrialBalanceService#getCreditAccountsTotal()
     */
    @Override
    public AccountBalance getCreditAccountsTotal() {
        List<AccountBalance> credits = getCreditAccountBalances();
        String total= "0.00";
        // String addCurrency(String a, String b)
        for (AccountBalance bal: credits) {
            if (bal.getBalance().contains("(")) {
                total = CurrencyUtil.subtractCurrency(total, bal.getBalance());
            } else {
                total = CurrencyUtil.addCurrency(total, bal.getBalance());
            }
        }
        AccountBalance balance = new AccountBalance();
        balance.setBalance(total);
        
        return balance;
    }

    /* (non-Javadoc)
     * @see org.ciwise.blackhole.service.TrialBalanceService#getDebitAccountBalances()
     */
    @Override
    public List<AccountBalance> getDebitAccountBalances() {
        List<AccountBalance> all = getAllAccountBalances();
        List<AccountBalance> debits = new ArrayList<AccountBalance>();
        
        for (AccountBalance bal: all) {
            if (bal.getType().equals("Debit")) {
                debits.add(bal);
            }
        }
        return debits;
    }

    /* (non-Javadoc)
     * @see org.ciwise.blackhole.service.TrialBalanceService#getCreditAccountBalances()
     */
    @Override
    public List<AccountBalance> getCreditAccountBalances() {
        List<AccountBalance> all = getAllAccountBalances();
        List<AccountBalance> credits = new ArrayList<AccountBalance>();
        
        for (AccountBalance bal: all) {
            if (bal.getType().equals("Credit")) {
                credits.add(bal);
            }
        }
        return credits;
    }

}
