package org.ciwise.blackhole.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.ciwise.blackhole.domain.GenAccount;
import org.ciwise.blackhole.service.GenAccountService;
import org.ciwise.blackhole.service.dto.SnapshotAccount;
import org.ciwise.blackhole.web.rest.util.HeaderUtil;
import org.ciwise.blackhole.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing GenAccount.
 */
@RestController
@RequestMapping("/api")
public class GenAccountResource {

    private final Logger log = LoggerFactory.getLogger(GenAccountResource.class);

    @Inject
    private GenAccountService genAccountService;

    /**
     * POST  /gen-accounts : Create a new genAccount.
     *
     * @param genAccount the genAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new genAccount, or with status 400 (Bad Request) if the genAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gen-accounts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenAccount> createGenAccount(@RequestBody GenAccount genAccount) throws URISyntaxException {
        log.debug("REST request to save GenAccount : {}", genAccount);
        if (genAccount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("genAccount", "idexists", "A new genAccount cannot already have an ID")).body(null);
        }
        GenAccount result = genAccountService.save(genAccount);
        return ResponseEntity.created(new URI("/api/gen-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("genAccount", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gen-accounts : Updates an existing genAccount.
     *
     * @param genAccount the genAccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated genAccount,
     * or with status 400 (Bad Request) if the genAccount is not valid,
     * or with status 500 (Internal Server Error) if the genAccount couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gen-accounts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenAccount> updateGenAccount(@RequestBody GenAccount genAccount) throws URISyntaxException {
        log.debug("REST request to update GenAccount : {}", genAccount);
        if (genAccount.getId() == null) {
            return createGenAccount(genAccount);
        }
        GenAccount result = genAccountService.save(genAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("genAccount", genAccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gen-accounts : get all the genAccounts.
     *
     * @return the list of genAccounts
     */
    @RequestMapping(value = "/gen-accounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SnapshotAccount> getAllGenAccounts()
        throws URISyntaxException {
        log.debug("REST request to get a page of GenAccounts");
        List<SnapshotAccount> snaps = genAccountService.findAllSnapshots(); 
        return snaps;
    }

    /**
     * GET  /gen-accounts/:id : get the "id" genAccount.
     *
     * @param id the id of the genAccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the genAccount, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/gen-accounts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenAccount> getGenAccount(@PathVariable Long id) {
        log.debug("REST request to get GenAccount : {}", id);
        GenAccount genAccount = genAccountService.findOne(id);
        return Optional.ofNullable(genAccount)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gen-accounts/:id : delete the "id" genAccount.
     *
     * @param id the id of the genAccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/gen-accounts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGenAccount(@PathVariable Long id) {
        log.debug("REST request to delete GenAccount : {}", id);
        genAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("genAccount", id.toString())).build();
    }

    /**
     * SEARCH  /_search/gen-accounts?query=:query : search for the genAccount corresponding
     * to the query.
     *
     * @param query the query of the genAccount search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/gen-accounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GenAccount>> searchGenAccounts(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of GenAccounts for query {}", query);
        Page<GenAccount> page = genAccountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/gen-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
