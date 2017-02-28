/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.service;

import org.ciwise.blackhole.domain.GenJournal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
public interface GenJournalService {
    /**
     * Save a GenJournal entry.
     * 
     * @param ledgerEntry the entity to save
     * @return the persisted entity
     */
    GenJournal save(GenJournal ledgerEntry);

    /**
     *  Get all the GenJournal entries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GenJournal> findAll(Pageable pageable);

    /**
     *  Get the GenJournal entry by "id".
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    GenJournal findOne(Long id);

    /**
     *  Delete the GenJournal entry by "id".
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the GenJournal entry corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<GenJournal> search(String query, Pageable pageable);

    /**
     * Gets the last,latest, or most recent journal entry
     * @return
     */
    GenJournal getLatestEntry();
}
