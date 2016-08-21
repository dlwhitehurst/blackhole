/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.ciwise.blackhole.service.TrialBalanceService;
import org.ciwise.blackhole.service.dto.AccountBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
@RestController
@RequestMapping("/api")
public class TrialBalanceResource {

    private final Logger log = LoggerFactory.getLogger(TrialBalanceResource.class);
    
    @Inject
    private TrialBalanceService trialBalanceService;
    
    /**
     * GET /debitbalances : get all the Debit AccountBalance objects (current).
     *
     */
    @RequestMapping(value = "/debitbalances",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AccountBalance> getDebitBalances() {
        log.debug("REST request to get Debit AccountBalances");
        List<AccountBalance> debits = trialBalanceService.getDebitAccountBalances(); 
        return debits;
    }

    /**
     * GET /creditbalances : get all the Credit AccountBalance objects (current).
     *
     */
    @RequestMapping(value = "/creditbalances",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AccountBalance> getCreditBalances() {
        log.debug("REST request to get Credit AccountBalances");
        List<AccountBalance> credits = trialBalanceService.getCreditAccountBalances(); 
        return credits;
    }

    /**
     * GET  /debitbalances/foot : get sum of all the Debit AccountBalance objects (current).
     *
     */
    @RequestMapping(value = "/debitbalances/foot",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public AccountBalance getDebitFoot() {
        log.debug("REST request to get sum of all Debit AccountBalances");
        AccountBalance result = trialBalanceService.getDebitAccountsTotal(); 
        return result;
    }

    /**
     * GET  /creditbalances/foot : get sum of all the Credit AccountBalance objects (current).
     *
     */
    @RequestMapping(value = "/creditbalances/foot",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public AccountBalance getCreditFoot() {
        log.debug("REST request to get sum of all Credit AccountBalances");
        AccountBalance result = trialBalanceService.getCreditAccountsTotal(); 
        return result;
    }

}
