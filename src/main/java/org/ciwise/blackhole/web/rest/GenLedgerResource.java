package org.ciwise.blackhole.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.ciwise.blackhole.domain.GenLedger;
import org.ciwise.blackhole.service.GenLedgerService;
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
 * REST controller for managing General Ledger entries.
 */
@RestController
@RequestMapping("/api")
public class GenLedgerResource {

    private final Logger log = LoggerFactory.getLogger(GenLedgerResource.class);

    @Inject
    private GenLedgerService genLedgerService;

    /**
     * POST  /gen-ledger-entries : Create a new genLedger entry.
     *
     * @param genLedger the genLedger to create
     * @return the ResponseEntity with status 201 (Created) and with body the new genLedger entry, or with status 400 (Bad Request) if the genLedger entry already has an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gen-ledger-entries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenLedger> createGeneralLedgerEntry(@RequestBody GenLedger genLedgerEntry) throws URISyntaxException {
        log.debug("REST request to save GenLedger entry : {}", genLedgerEntry);
        if (genLedgerEntry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("genLedgerEntry", "idexists", "A new GenLedger entry cannot already have an ID")).body(null);
        }
        GenLedger result = genLedgerService.save(genLedgerEntry);
        return ResponseEntity.created(new URI("/api/gen-ledger-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("genLedgerEntry", result.getId().toString()))
            .body(result);
    } 

    /**
     * PUT  /gen-ledger-entries : Updates an existing GenLedger entry.
     *
     * @param genLedgerEntry the GenLedger entry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated GenLedger entry,
     * or with status 400 (Bad Request) if the GenLedger entry is not valid,
     * or with status 500 (Internal Server Error) if the GenLedger entry couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gen-ledger-entries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenLedger> updateGeneralLedgerEntry(@RequestBody GenLedger genLedgerEntry) throws URISyntaxException {
        log.debug("REST request to update GenLedger entry : {}", genLedgerEntry);
        if (genLedgerEntry.getId() == null) {
            return createGeneralLedgerEntry(genLedgerEntry);
        }
        GenLedger result = genLedgerService.save(genLedgerEntry);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("genLedgerEntry", genLedgerEntry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gen-ledger-entries : get all the GenLedger entries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of GenLedger entries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/gen-ledger-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GenLedger>> getAllGeneralLedgerEntries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of GenLedger entries");
        Page<GenLedger> page = genLedgerService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gen-ledger-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gen-ledger-entries/:id : get the GenLedger entry by "id".
     *
     * @param id the id of the GenLedger entry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the GenLedger entry, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/gen-ledger-entries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenLedger> getGeneralLedgerEntry(@PathVariable Long id) {
        log.debug("REST request to get GenLedger entry : {}", id);
        GenLedger genLedgerEntry = genLedgerService.findOne(id);
        return Optional.ofNullable(genLedgerEntry)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gen-ledger-entries/:id : delete the GenLedger entry by "id".
     *
     * @param id the id of the GenLedger entry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/gen-ledger-entries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGeneralLedgerEntry(@PathVariable Long id) {
        log.debug("REST request to delete GenLedger entry : {}", id);
        genLedgerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("genLedgerEntry", id.toString())).build();
    }

    /**
     * SEARCH  /_search/gen-ledger-entries?query=:query : search for the genLedgerEntry corresponding
     * to the query.
     *
     * @param query the query of the GenLedger entry search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/gen-ledger-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GenLedger>> searchGeneralLedgerEntries(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of GenLedger entries for query {}", query);
        Page<GenLedger> page = genLedgerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/gen-ledger-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
