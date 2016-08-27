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

import org.ciwise.blackhole.domain.GenLedger;
import org.ciwise.blackhole.repository.GenLedgerRepository;
import org.ciwise.blackhole.repository.search.GenLedgerSearchRepository;
import org.ciwise.blackhole.service.GenLedgerService;
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
public class GenLedgerServiceImpl implements GenLedgerService {

    private final Logger log = LoggerFactory.getLogger(GenLedgerServiceImpl.class);
    
    @Inject
    private GenLedgerRepository genLedgerRepository;
    
    @Inject
    private GenLedgerSearchRepository genLedgerSearchRepository;
    
    /**
     * Save a GenLedger entry.
     * 
     * @param accountEntry the entity to save
     * @return the persisted entity
     */
    public GenLedger save(GenLedger accountEntry) {
        log.debug("Request to save AccountEntry : {}", accountEntry);
        GenLedger result = genLedgerRepository.save(accountEntry);
        genLedgerSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all of the GenLedger entries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<GenLedger> findAll(Pageable pageable) {
        log.debug("Request to get all LedgerEntries");
        Page<GenLedger> result = genLedgerRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get all of the GenLedger entries (unpaged).
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<GenLedger> findAll() {
        log.debug("Request to get all LedgerEntries (unpaged)");
        List<GenLedger> result = genLedgerRepository.findAll(); 
        return result;
    }

    /**
     *  Get a GenLedger entry by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public GenLedger findOne(Long id) {
        log.debug("Request to get GenLedger : {}", id);
        GenLedger accountEntry = genLedgerRepository.findOne(id);
        return accountEntry;
    }

    /**
     * Get a GenLedger entry by chart number
     */
    @Transactional(readOnly = true) 
    public Page<GenLedger> findByCno(String chartNumber, Pageable pageable) {
        log.debug("Request to get GenLedger by cno: {}", chartNumber);
        Page<GenLedger> result = genLedgerRepository.findByCno(chartNumber, pageable);
        return result;
    }

    /**
     * Get a GenLedger entry by chart number (unpaged)
     */
    @Transactional(readOnly = true) 
    public List<GenLedger> findByCno(String chartNumber) {
        log.debug("Request to get GenLedger by cno: {}", chartNumber);
        List<GenLedger> result = genLedgerRepository.findByCno(chartNumber);
        return result;
    }
    
    /**
     *  Delete a GenLedger entry by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete GenLedger : {}", id);
        genLedgerRepository.delete(id);
        genLedgerSearchRepository.delete(id);
    }

    /**
     * Search for the GenLedger entry(s) corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<GenLedger> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LedgerEntries for query {}", query);
        return genLedgerSearchRepository.search(queryStringQuery(query), pageable);
    }
}
