/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.ciwise.blackhole.domain.GenAccount;
import org.ciwise.blackhole.repository.GenAccountRepository;
import org.ciwise.blackhole.repository.search.GenAccountSearchRepository;
import org.ciwise.blackhole.service.GenAccountService;
import org.ciwise.blackhole.service.TrialBalanceService;
import org.ciwise.blackhole.service.dto.AccountBalance;
import org.ciwise.blackhole.service.dto.SnapshotAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
@Service
@Transactional
public class GenAccountServiceImpl implements GenAccountService {

    private final Logger log = LoggerFactory.getLogger(GenAccountServiceImpl.class);
    
    @Inject
    private GenAccountRepository genAccountRepository;
    
    @Inject
    private GenAccountSearchRepository genAccountSearchRepository;

    @Inject
    private TrialBalanceService trialBalanceService;
    
    /**
     * Save a GenAccount.
     * 
     * @param genAccount the entity to save
     * @return the persisted entity
     */
    public GenAccount save(GenAccount genAccount) {
        log.debug("Request to save GenAccount : {}", genAccount);
        GenAccount result = genAccountRepository.save(genAccount);
        genAccountSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the genAccounts (unpaged).
     *  
     *  @return the list of accounts
     */
    public List<SnapshotAccount> findAllSnapshots() {
        List<SnapshotAccount> snaps = new ArrayList<SnapshotAccount>();
    	List<GenAccount> result = findAll();

    	List<AccountBalance> balances = trialBalanceService.getAllAccountBalances();
        for (GenAccount account: result) {
        	SnapshotAccount sAccount = new SnapshotAccount();
        	sAccount.setId(account.getId());
        	sAccount.setCno(account.getCno());
        	sAccount.setName(account.getName());
        	sAccount.setDc(account.getDc());
        	sAccount.setType(account.getType());
        	
            for (AccountBalance balance: balances){
            	if (account.getName().equals(balance.getAccountName())) {
            		sAccount.setBalance(balance.getBalance());
            	}
            }
            snaps.add(sAccount);
        }
        return snaps;
    }

    /**
     *  Get all the genAccounts (unpaged).
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<GenAccount> findAll() {
        log.debug("Request to get all LedgerEntries (unpaged)");
        List<GenAccount> result = genAccountRepository.findAll(); 
        return result;
    }    
    /**
     *  Get one genAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public GenAccount findOne(Long id) {
        log.debug("Request to get GenAccount : {}", id);
        GenAccount genAccount = genAccountRepository.findOne(id);
        return genAccount;
    }

    /**
     *  Delete the  genAccount by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete GenAccount : {}", id);
        genAccountRepository.delete(id);
        genAccountSearchRepository.delete(id);
    }

    /**
     * Search for the genAccount corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<GenAccount> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LedgerEntries for query {}", query);
        return genAccountSearchRepository.search(queryStringQuery(query), pageable);
    }

}
