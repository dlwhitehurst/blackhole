/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import javax.inject.Inject;

import org.ciwise.blackhole.domain.AccountEntry;
import org.ciwise.blackhole.repository.AccountEntryRepository;
import org.ciwise.blackhole.repository.search.AccountEntrySearchRepository;
import org.ciwise.blackhole.service.AccountEntryService;
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
public class AccountEntryServiceImpl implements AccountEntryService {

    private final Logger log = LoggerFactory.getLogger(AccountEntryServiceImpl.class);
    
    @Inject
    private AccountEntryRepository accountEntryRepository;
    
    @Inject
    private AccountEntrySearchRepository accountEntrySearchRepository;
    
    /**
     * Save an AccountEntry.
     * 
     * @param accountEntry the entity to save
     * @return the persisted entity
     */
    public AccountEntry save(AccountEntry accountEntry) {
        log.debug("Request to save AccountEntry : {}", accountEntry);
        AccountEntry result = accountEntryRepository.save(accountEntry);
        accountEntrySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the accountEntries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AccountEntry> findAll(Pageable pageable) {
        log.debug("Request to get all LedgerEntries");
        Page<AccountEntry> result = accountEntryRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one accountEntry by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AccountEntry findOne(Long id) {
        log.debug("Request to get AccountEntry : {}", id);
        AccountEntry accountEntry = accountEntryRepository.findOne(id);
        return accountEntry;
    }

    /**
     *  Delete the  accountEntry by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AccountEntry : {}", id);
        accountEntryRepository.delete(id);
        accountEntrySearchRepository.delete(id);
    }

    /**
     * Search for the accountEntry corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AccountEntry> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LedgerEntries for query {}", query);
        return accountEntrySearchRepository.search(queryStringQuery(query), pageable);
    }
}
