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

import org.ciwise.blackhole.domain.GenJournal;
import org.ciwise.blackhole.domain.GenLedger;
import org.ciwise.blackhole.repository.GenJournalRepository;
import org.ciwise.blackhole.repository.search.GenJournalSearchRepository;
import org.ciwise.blackhole.service.GenJournalService;
import org.ciwise.blackhole.service.GenLedgerService;
import org.ciwise.blackhole.service.util.CurrencyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
@Service
@Transactional
public class GenJournalServiceImpl implements GenJournalService {
    
    private final Logger log = LoggerFactory.getLogger(GenJournalServiceImpl.class);
    
    @Inject
    private GenJournalRepository genJournalRepository;
    
    @Inject
    private GenJournalSearchRepository genJournalSearchRepository;

    @Inject
    private GenLedgerService genLedgerService;

    /**
     * Save a GenJournal entry.
     * 
     * @param genJournal the entity to save
     * @return the persisted entity
     */
    public GenJournal save(GenJournal genJournal) {
        log.debug("Request to save GenJournal entry : {}", genJournal);

        GenJournal result = genJournalRepository.save(genJournal);
        genJournalSearchRepository.save(result);
        
        // create both account entry records, load what we can
        GenLedger debitAccountEntry = loadDebitAccountData(genJournal);
        GenLedger creditAccountEntry = loadCreditAccountData(genJournal);

        List<GenLedger> debitList = genLedgerService.findByCno(genJournal.getDacctno()); 
        List<GenLedger> creditList = genLedgerService.findByCno(genJournal.getCacctno());
        
        System.out.println("The size of the debit list=" + debitList.size());
        System.out.println("The size of the credit list=" + debitList.size());
        
        
        // Debit Operation Running Balance Section
        // ###################################################################################################################
        
        GenLedger debitLatest = new GenLedger();
        int maxDebitId = 0;
        
        for (GenLedger entry: debitList) {
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

        GenLedger creditLatest = new GenLedger();
        int maxCreditId = 0;
        
        for (GenLedger entry: creditList) {
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
        
        genLedgerService.save(debitAccountEntry);
        genLedgerService.save(creditAccountEntry);

        
        return result;
    }

    /**
     * @param genJournal
     * @return
     */
    private GenLedger loadCreditAccountData(GenJournal genJournal) {
        GenLedger creditAccountEntry = new GenLedger();
        creditAccountEntry.setCno(genJournal.getCacctno());
        creditAccountEntry.setTransaction(genJournal.getTransaction());
        creditAccountEntry.setPostingref(genJournal.getId());
        creditAccountEntry.setEntrydate(genJournal.getEntrydate());
        creditAccountEntry.setDebit(genJournal.getCadebit());
        creditAccountEntry.setCredit(genJournal.getCacredit());
        creditAccountEntry.setPostingref(genJournal.getId());
        creditAccountEntry.setAccountname(genJournal.getCreditaccountname());
        return creditAccountEntry;
    }

    /**
     * @param genJournal
     * @return
     */
    private GenLedger loadDebitAccountData(GenJournal genJournal) {
        GenLedger debitAccountEntry = new GenLedger();
        debitAccountEntry.setCno(genJournal.getDacctno());
        debitAccountEntry.setTransaction(genJournal.getTransaction());
        debitAccountEntry.setPostingref(genJournal.getId());
        debitAccountEntry.setEntrydate(genJournal.getEntrydate());
        debitAccountEntry.setDebit(genJournal.getDadebit());
        debitAccountEntry.setCredit(genJournal.getDacredit());
        debitAccountEntry.setPostingref(genJournal.getId());
        debitAccountEntry.setAccountname(genJournal.getDebitaccountname());
        return debitAccountEntry;
    }

    /**
     *  Get all the GenJournal entries
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<GenJournal> findAll(Pageable pageable) {
        log.debug("Request to get all GenJournal entries");
        Page<GenJournal> result = genJournalRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get a GenJournal entry by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public GenJournal findOne(Long id) {
        log.debug("Request to get GenJournal entry : {}", id);
        GenJournal ledgerEntry = genJournalRepository.findOne(id);
        return ledgerEntry;
    }

    /**
     *  Delete the GenJournal entry by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete GenJournal entry : {}", id);
        genJournalRepository.delete(id);
        genJournalSearchRepository.delete(id);
    }

    /**
     * Search for the GenJournal entry corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<GenJournal> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GenJournal entries for query {}", query);
        return genJournalSearchRepository.search(queryStringQuery(query), pageable);
    }
}
