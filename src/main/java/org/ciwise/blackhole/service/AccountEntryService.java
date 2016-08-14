/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.service;

import org.ciwise.blackhole.domain.AccountEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
public interface AccountEntryService {
    /**
     * Save a AccountEntry.
     * 
     * @param accountEntry the entity to save
     * @return the persisted entity
     */
    AccountEntry save(AccountEntry accountEntry);

    /**
     *  Get all the accountEntries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AccountEntry> findAll(Pageable pageable);

    /**
     *  Get the "id" accountEntry.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    AccountEntry findOne(Long id);

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
    Page<AccountEntry> search(String query, Pageable pageable);


}
