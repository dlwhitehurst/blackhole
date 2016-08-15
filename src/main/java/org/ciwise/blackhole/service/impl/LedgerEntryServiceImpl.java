/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;

import javax.inject.Inject;

import org.ciwise.blackhole.domain.AccountEntry;
import org.ciwise.blackhole.domain.GenAccount;
import org.ciwise.blackhole.domain.LedgerEntry;
import org.ciwise.blackhole.repository.AccountEntryRepository;
import org.ciwise.blackhole.repository.GenAccountRepository;
import org.ciwise.blackhole.repository.LedgerEntryRepository;
import org.ciwise.blackhole.repository.search.AccountEntrySearchRepository;
import org.ciwise.blackhole.repository.search.GenAccountSearchRepository;
import org.ciwise.blackhole.repository.search.LedgerEntrySearchRepository;
import org.ciwise.blackhole.service.LedgerEntryService;
import org.ciwise.blackhole.service.util.CurrencyUtil;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
@Service
@Transactional
public class LedgerEntryServiceImpl implements LedgerEntryService {
    
    private final Logger log = LoggerFactory.getLogger(LedgerEntryServiceImpl.class);
    
    @Inject
    private LedgerEntryRepository ledgerEntryRepository;
    
    @Inject
    private LedgerEntrySearchRepository ledgerEntrySearchRepository;

    @Inject
    private AccountEntryRepository accountEntryRepository;
    
    @Inject
    private AccountEntrySearchRepository accountEntrySearchRepository;

    @Inject
    private GenAccountRepository genAccountRepository;
    
    @Inject
    private GenAccountSearchRepository genAccountSearchRepository;
    
    /**
     * Save a Ledger.
     * 
     * @param ledgerEntry the entity to save
     * @return the persisted entity
     */
    public LedgerEntry save(LedgerEntry ledgerEntry) {
        log.debug("Request to save LedgerEntry : {}", ledgerEntry);
        
        LedgerEntry result = ledgerEntryRepository.save(ledgerEntry);
        ledgerEntrySearchRepository.save(result);

        // create both account entry records
        AccountEntry debitAccountEntry = new AccountEntry();
        AccountEntry creditAccountEntry = new AccountEntry();
        
        debitAccountEntry.setCno(ledgerEntry.getDacctno());
        creditAccountEntry.setCno(ledgerEntry.getCacctno());
        
        debitAccountEntry.setTransaction(ledgerEntry.getTransaction());
        creditAccountEntry.setTransaction(ledgerEntry.getTransaction());

        debitAccountEntry.setPostingref(ledgerEntry.getId());
        creditAccountEntry.setPostingref(ledgerEntry.getId());
        
        debitAccountEntry.setEntrydate(ledgerEntry.getEntrydate());
        creditAccountEntry.setEntrydate(ledgerEntry.getEntrydate());
        
        debitAccountEntry.setDebit(ledgerEntry.getDadebit());
        debitAccountEntry.setCredit(ledgerEntry.getDacredit());
        creditAccountEntry.setDebit(ledgerEntry.getCadebit());
        creditAccountEntry.setCredit(ledgerEntry.getCacredit());
        
        QueryBuilder qb_d = QueryBuilders.termQuery("cno", ledgerEntry.getDacctno());
        QueryBuilder qb_c = QueryBuilders.termQuery("cno", ledgerEntry.getCacctno());
        
        Iterable<AccountEntry> debitAccountEntries = accountEntrySearchRepository.search(qb_d); // need only by ledgerEntry.getDacctno()
        Iterable<AccountEntry> creditAccountEntries = accountEntrySearchRepository.search(qb_c);
        // Debit Balance Section
        // ###################################################################################################################
        
        AccountEntry debitLatest = null;
        int maxDebitId = 0;
        
        while (debitAccountEntries.iterator().hasNext()) {
            AccountEntry entry = debitAccountEntries.iterator().next();
            if (entry.getId().intValue() > maxDebitId) {
                maxDebitId = entry.getId().intValue();
                debitLatest = entry;
                System.out.println("DEBUG: " + entry.getId());
            }
        } // null pointer ln 118
        String lastDebitOperationDebitBalance = debitLatest.getDebitbalance(); // not null Debit(+) type account
        String lastDebitOperationCreditBalance = debitLatest.getCreditbalance(); // not null Credit(+) type account
        
        String debitOperationDebitBalance = null;
        String debitOperationCreditBalance = null;
        
        
        if (lastDebitOperationDebitBalance != null) { // Debit(+) Type Account (Assumes initializing balance rows all accounts)
            if (debitAccountEntry.getDebit() != null) {
                // adding
                debitOperationDebitBalance = CurrencyUtil.addCurrency(lastDebitOperationDebitBalance, debitAccountEntry.getDebit());
                debitAccountEntry.setDebitbalance(debitOperationDebitBalance);

            } else {
                // subtracting
                debitOperationDebitBalance = CurrencyUtil.subtractCurrency(lastDebitOperationDebitBalance, debitAccountEntry.getCredit());
                debitAccountEntry.setDebitbalance(debitOperationDebitBalance);

            }
        } else if (lastDebitOperationCreditBalance != null) { // Credit(+) Type Account
            if (debitAccountEntry.getCredit() != null) {
                // adding
                debitOperationCreditBalance = CurrencyUtil.subtractCurrency(lastDebitOperationCreditBalance, debitAccountEntry.getDebit());
                debitAccountEntry.setCreditbalance(debitOperationCreditBalance);

            } else {
                // subtracting
                debitOperationCreditBalance = CurrencyUtil.addCurrency(lastDebitOperationCreditBalance, debitAccountEntry.getCredit());
                debitAccountEntry.setCreditbalance(debitOperationCreditBalance);

            }
        } else { // Error - Something is wrong
            throw new RuntimeException("FATAL: Possible initializing ledger account rows not available or missing!");
        }
        

        // Credit Balance Section
        // ###################################################################################################################

        AccountEntry creditLatest = null;
        int maxCreditId = 0;
        
        while (creditAccountEntries.iterator().hasNext()) {
            AccountEntry entry = creditAccountEntries.iterator().next();
            if (entry.getId().intValue() > maxCreditId) {
                maxCreditId = entry.getId().intValue();
                creditLatest = entry;
            }
        }

        String lastCreditOperationCreditBalance = creditLatest.getCreditbalance();
        String lastCreditOperationDebitBalance = creditLatest.getCreditbalance();
        
        String creditOperationCreditBalance = null;
        String creditOperationDebitBalance = null;
        
        if (lastCreditOperationCreditBalance != null) { // Credit(+) Type Account (Assumes initializing balance rows all accounts)
            if (creditAccountEntry.getCredit() != null) {
                // adding
                creditOperationCreditBalance = CurrencyUtil.addCurrency(lastCreditOperationCreditBalance, creditAccountEntry.getCredit());
                creditAccountEntry.setCreditbalance(creditOperationCreditBalance);

            } else {
                // subtracting
                creditOperationCreditBalance = CurrencyUtil.subtractCurrency(lastCreditOperationCreditBalance, creditAccountEntry.getDebit());
                creditAccountEntry.setCreditbalance(creditOperationCreditBalance);

            }
        } else if (lastCreditOperationDebitBalance != null) { // Debit(+) Type Account
            if (creditAccountEntry.getCredit() != null) {
                // adding
                creditOperationDebitBalance = CurrencyUtil.subtractCurrency(lastCreditOperationDebitBalance, creditAccountEntry.getCredit());
                creditAccountEntry.setDebitbalance(creditOperationDebitBalance);

            } else {
                // subtracting
                creditOperationDebitBalance = CurrencyUtil.addCurrency(lastCreditOperationDebitBalance, creditAccountEntry.getDebit());
                creditAccountEntry.setDebitbalance(creditOperationDebitBalance);

            }
        } else { // Error - Something is wrong
            throw new RuntimeException("FATAL: Possible initializing ledger account rows not available or missing!");
        }
        
        // ###################################################################################################################
        
        // Get balance from accounts
   /*     
        Page<GenAccount> debitAccounts = genAccountSearchRepository.search(queryStringQuery(ledgerEntry.getDacctno()), new PageRequest(1,10));
        Page<GenAccount> creditAccounts = genAccountSearchRepository.search(queryStringQuery(ledgerEntry.getCacctno()), new PageRequest(1,10));
        
        List<GenAccount> debitList = debitAccounts.getContent();
        List<GenAccount> creditList = creditAccounts.getContent();
        
        String debitBal = null;
        String creditBal = null;
        
        for (GenAccount acct: debitList) {
            debitBal = acct.getBalance();
        }
        
        for (GenAccount acct: creditList) {
            creditBal = acct.getBalance();
        }
   */     
        accountEntryRepository.save(debitAccountEntry);
        accountEntryRepository.save(creditAccountEntry);
        accountEntrySearchRepository.save(debitAccountEntry);
        accountEntrySearchRepository.save(creditAccountEntry);
        
        
        
        return result;
    }

    /**
     *  Get all the ledgerEntries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<LedgerEntry> findAll(Pageable pageable) {
        log.debug("Request to get all LedgerEntries");
        Page<LedgerEntry> result = ledgerEntryRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one ledgerEntry by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public LedgerEntry findOne(Long id) {
        log.debug("Request to get LedgerEntry : {}", id);
        LedgerEntry ledgerEntry = ledgerEntryRepository.findOne(id);
        return ledgerEntry;
    }

    /**
     *  Delete the  ledgerEntry by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LedgerEntry : {}", id);
        ledgerEntryRepository.delete(id);
        ledgerEntrySearchRepository.delete(id);
    }

    /**
     * Search for the ledgerEntry corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LedgerEntry> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LedgerEntries for query {}", query);
        return ledgerEntrySearchRepository.search(queryStringQuery(query), pageable);
    }
}
