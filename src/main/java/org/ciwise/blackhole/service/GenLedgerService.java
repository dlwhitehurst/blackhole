/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.service;

import java.util.List;

import org.ciwise.blackhole.domain.GenLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
public interface GenLedgerService {
    /**
     * Save a AccountEntry.
     * 
     * @param accountEntry the entity to save
     * @return the persisted entity
     */
    GenLedger save(GenLedger accountEntry);

    /**
     *  Get all the accountEntries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GenLedger> findAll(Pageable pageable);

    /**
     *  Get all the accountEntries (unpaged).
     *  
     *  @return the list of entities
     */
    List<GenLedger> findAll();
    
    /**
     * Get all the accountEntries by Chart Number (cno)
     * 
     * @param chartNumber
     * @param pageable
     * @return
     */
    Page<GenLedger> findByCno(String chartNumber, Pageable pageable);

    /**
     * Get all the accountEntries by Chart Number (cno) (unpaged)
     * 
     * @param chartNumber
     * @return
     */
    List<GenLedger> findByCno(String chartNumber);
    
    /**
     *  Get the "id" accountEntry.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    GenLedger findOne(Long id);

    /**
     *  Delete the "id" accountEntry.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the accountEntry corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<GenLedger> search(String query, Pageable pageable);


}
