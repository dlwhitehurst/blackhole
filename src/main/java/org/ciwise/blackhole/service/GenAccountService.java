/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.service;

import java.util.List;

import org.ciwise.blackhole.domain.GenAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
public interface GenAccountService {
    /**
     * Save a GenAccount.
     * 
     * @param genAccount the entity to save
     * @return the persisted entity
     */
    GenAccount save(GenAccount genAccount);

    /**
     *  Get all the genAccounts.
     *  
     *  @param pageable the pagination information
     *  @return the list of genAccounts
     */
    Page<GenAccount> findAll(Pageable pageable);

    /**
     *  Get all the genAccounts (unpaged).
     *  
     *  @param pageable the pagination information
     *  @return the list of genAccounts
     */
    List<GenAccount> findAll();
    
    /**
     *  Get the "id" GenAccount.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    GenAccount findOne(Long id);

    /**
     *  Delete the "id" GenAccount.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the GenAccount corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<GenAccount> search(String query, Pageable pageable);

}
