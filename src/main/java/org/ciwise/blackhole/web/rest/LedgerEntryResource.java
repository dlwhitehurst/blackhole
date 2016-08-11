package org.ciwise.blackhole.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ciwise.blackhole.domain.LedgerEntry;
import org.ciwise.blackhole.repository.LedgerEntryRepository;
import org.ciwise.blackhole.repository.search.LedgerEntrySearchRepository;
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
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing LedgerEntry.
 */
@RestController
@RequestMapping("/api")
public class LedgerEntryResource {

    private final Logger log = LoggerFactory.getLogger(LedgerEntryResource.class);
        
    @Inject
    private LedgerEntryRepository ledgerEntryRepository;
    
    @Inject
    private LedgerEntrySearchRepository ledgerEntrySearchRepository;
    
    /**
     * POST  /ledger-entries : Create a new ledgerEntry.
     *
     * @param ledgerEntry the ledgerEntry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ledgerEntry, or with status 400 (Bad Request) if the ledgerEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ledger-entries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LedgerEntry> createLedgerEntry(@RequestBody LedgerEntry ledgerEntry) throws URISyntaxException {
        log.debug("REST request to save LedgerEntry : {}", ledgerEntry);
        if (ledgerEntry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ledgerEntry", "idexists", "A new ledgerEntry cannot already have an ID")).body(null);
        }
        LedgerEntry result = ledgerEntryRepository.save(ledgerEntry);
        ledgerEntrySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ledger-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ledgerEntry", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ledger-entries : Updates an existing ledgerEntry.
     *
     * @param ledgerEntry the ledgerEntry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ledgerEntry,
     * or with status 400 (Bad Request) if the ledgerEntry is not valid,
     * or with status 500 (Internal Server Error) if the ledgerEntry couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ledger-entries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LedgerEntry> updateLedgerEntry(@RequestBody LedgerEntry ledgerEntry) throws URISyntaxException {
        log.debug("REST request to update LedgerEntry : {}", ledgerEntry);
        if (ledgerEntry.getId() == null) {
            return createLedgerEntry(ledgerEntry);
        }
        LedgerEntry result = ledgerEntryRepository.save(ledgerEntry);
        ledgerEntrySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ledgerEntry", ledgerEntry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ledger-entries : get all the ledgerEntries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ledgerEntries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ledger-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LedgerEntry>> getAllLedgerEntries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LedgerEntries");
        Page<LedgerEntry> page = ledgerEntryRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ledger-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ledger-entries/:id : get the "id" ledgerEntry.
     *
     * @param id the id of the ledgerEntry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ledgerEntry, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ledger-entries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LedgerEntry> getLedgerEntry(@PathVariable Long id) {
        log.debug("REST request to get LedgerEntry : {}", id);
        LedgerEntry ledgerEntry = ledgerEntryRepository.findOne(id);
        return Optional.ofNullable(ledgerEntry)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ledger-entries/:id : delete the "id" ledgerEntry.
     *
     * @param id the id of the ledgerEntry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ledger-entries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLedgerEntry(@PathVariable Long id) {
        log.debug("REST request to delete LedgerEntry : {}", id);
        ledgerEntryRepository.delete(id);
        ledgerEntrySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ledgerEntry", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ledger-entries?query=:query : search for the ledgerEntry corresponding
     * to the query.
     *
     * @param query the query of the ledgerEntry search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/ledger-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LedgerEntry>> searchLedgerEntries(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of LedgerEntries for query {}", query);
        Page<LedgerEntry> page = ledgerEntrySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ledger-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
