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
import org.ciwise.blackhole.domain.LedgerEntry;
import org.ciwise.blackhole.repository.LedgerEntryRepository;
import org.ciwise.blackhole.repository.search.LedgerEntrySearchRepository;
import org.ciwise.blackhole.service.AccountEntryService;
import org.ciwise.blackhole.service.LedgerEntryService;
import org.ciwise.blackhole.service.util.CurrencyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

//    @Inject
//    private AccountEntryRepository accountEntryRepository;
    
//    @Inject
//    private AccountEntrySearchRepository accountEntrySearchRepository;

    @Inject
    private AccountEntryService accountEntryService;

//    @Inject
//    private GenAccountRepository genAccountRepository;
    
//    @Inject
//    private GenAccountSearchRepository genAccountSearchRepository;
    
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
        
        // create both account entry records, load what we can
        AccountEntry debitAccountEntry = loadDebitAccountData(ledgerEntry);
        AccountEntry creditAccountEntry = loadCreditAccountData(ledgerEntry);

        Page<AccountEntry> debitAccountEntries = accountEntryService.findByCno(ledgerEntry.getDacctno(),new PageRequest(0,1000)); 
        Page<AccountEntry> creditAccountEntries = accountEntryService.findByCno(ledgerEntry.getCacctno(), new PageRequest(0,1000));
        
        List<AccountEntry> debitList = debitAccountEntries.getContent();
        List<AccountEntry> creditList = creditAccountEntries.getContent();
       
        System.out.println("The size of the debit list=" + debitList.size());
        System.out.println("The size of the credit list=" + debitList.size());
        
        
        // Debit Operation Running Balance Section
        // ###################################################################################################################
        
        AccountEntry debitLatest = new AccountEntry();
        int maxDebitId = 0;
        
        for (AccountEntry entry: debitList) {
            if (entry.getId().intValue() > maxDebitId) {
                maxDebitId = entry.getId().intValue();
                debitLatest.setDebitbalance(entry.getDebitbalance());
                debitLatest.setCreditbalance(entry.getCreditbalance());
            }
        }
        
        if (debitLatest.getDebitbalance() != null) {
            System.out.println("LASTDEBITDEBITBAL=" + debitLatest.getDebitbalance());
        }
        if (debitLatest.getCreditbalance() != null) {
            System.out.println("LASTDEBITCREDITBAL=" + debitLatest.getCreditbalance());
        }
        
        String debitOperationDebitBalance = new String();
        String debitOperationCreditBalance = new String();
        
        
        if (debitLatest.getDebitbalance() != null) { // Debit(+) Type Account (Assumes initializing balance rows all accounts)
            if (debitAccountEntry.getDebit() != null) { // addition to balance
                // adding
                debitOperationDebitBalance = CurrencyUtil.addCurrency(debitLatest.getDebitbalance(), debitAccountEntry.getDebit());
                debitAccountEntry.setDebitbalance(debitOperationDebitBalance);

            } else {
                // subtracting
                debitOperationDebitBalance = CurrencyUtil.subtractCurrency(debitLatest.getDebitbalance(), debitAccountEntry.getCredit());
                debitAccountEntry.setDebitbalance(debitOperationDebitBalance);

            }
        } else if (debitLatest.getCreditbalance()  != null) { // Credit(+) Type Account
            if (debitAccountEntry.getCredit() != null) {
                // adding
                debitOperationCreditBalance = CurrencyUtil.subtractCurrency(debitLatest.getCreditbalance(), debitAccountEntry.getDebit());
                debitAccountEntry.setCreditbalance(debitOperationCreditBalance);

            } else {
                // subtracting
                debitOperationCreditBalance = CurrencyUtil.addCurrency(debitLatest.getCreditbalance(), debitAccountEntry.getCredit());
                debitAccountEntry.setCreditbalance(debitOperationCreditBalance);

            }
        } else { // Error - Something is wrong
            throw new RuntimeException("FATAL: Possible initializing ledger account rows not available or missing!");
        }
        

        // Credit Operation Running Balance Section
        // ###################################################################################################################

        AccountEntry creditLatest = new AccountEntry();
        int maxCreditId = 0;
        
        for (AccountEntry entry: creditList) {
            if (entry.getId().intValue() > maxCreditId) {
                maxCreditId = entry.getId().intValue();
                creditLatest.setDebitbalance(entry.getDebitbalance());
                creditLatest.setCreditbalance(entry.getCreditbalance());
            }
        }
        
        if (creditLatest.getCreditbalance() != null) {
            System.out.println("LASTCREDITDEBITBAL=" + creditLatest.getDebitbalance());
        }
        if (creditLatest.getDebitbalance() != null) {
            System.out.println("LASTCREDITCREDITBAL=" + creditLatest.getCreditbalance());
        }
        
        String creditOperationCreditBalance = new String();
        String creditOperationDebitBalance = new String();
        
        if (creditLatest.getCreditbalance() != null) { // Credit(+) Type Account (Assumes initializing balance rows all accounts)
            if (creditAccountEntry.getCredit() != null) {
                // adding
                creditOperationCreditBalance = CurrencyUtil.addCurrency(creditLatest.getCreditbalance(), creditAccountEntry.getCredit());
                creditAccountEntry.setCreditbalance(creditOperationCreditBalance);

            } else {
                // subtracting
                creditOperationCreditBalance = CurrencyUtil.subtractCurrency(creditLatest.getCreditbalance(), creditAccountEntry.getDebit());
                creditAccountEntry.setCreditbalance(creditOperationCreditBalance);

            }
        } else if (creditLatest.getDebitbalance() != null) { // Debit(+) Type Account
            if (creditAccountEntry.getCredit() != null) { // subtraction from balance
                // subtracting
                creditOperationDebitBalance = CurrencyUtil.subtractCurrency(creditLatest.getDebitbalance(), creditAccountEntry.getCredit());
                creditAccountEntry.setDebitbalance(creditOperationDebitBalance);

            } else {
                // adding
                creditOperationDebitBalance = CurrencyUtil.addCurrency(creditLatest.getDebitbalance(), creditAccountEntry.getDebit());
                creditAccountEntry.setDebitbalance(creditOperationDebitBalance);

            }
        } else { // Error - Something is wrong
            throw new RuntimeException("FATAL: Possible initializing ledger account rows not available or missing!");
        }
        
        // ###################################################################################################################
        
        accountEntryService.save(debitAccountEntry);
        accountEntryService.save(creditAccountEntry);

        
        return result;
    }

    /**
     * @param ledgerEntry
     * @return
     */
    private AccountEntry loadCreditAccountData(LedgerEntry ledgerEntry) {
        AccountEntry creditAccountEntry = new AccountEntry();
        creditAccountEntry.setCno(ledgerEntry.getCacctno());
        creditAccountEntry.setTransaction(ledgerEntry.getTransaction());
        creditAccountEntry.setPostingref(ledgerEntry.getId());
        creditAccountEntry.setEntrydate(ledgerEntry.getEntrydate());
        creditAccountEntry.setDebit(ledgerEntry.getCadebit());
        creditAccountEntry.setCredit(ledgerEntry.getCacredit());
        creditAccountEntry.setPostingref(ledgerEntry.getId());
        return creditAccountEntry;
    }

    /**
     * @param ledgerEntry
     * @return
     */
    private AccountEntry loadDebitAccountData(LedgerEntry ledgerEntry) {
        AccountEntry debitAccountEntry = new AccountEntry();
        debitAccountEntry.setCno(ledgerEntry.getDacctno());
        debitAccountEntry.setTransaction(ledgerEntry.getTransaction());
        debitAccountEntry.setPostingref(ledgerEntry.getId());
        debitAccountEntry.setEntrydate(ledgerEntry.getEntrydate());
        debitAccountEntry.setDebit(ledgerEntry.getDadebit());
        debitAccountEntry.setCredit(ledgerEntry.getDacredit());
        debitAccountEntry.setPostingref(ledgerEntry.getId());
        return debitAccountEntry;
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
