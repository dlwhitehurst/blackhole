package org.ciwise.blackhole.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.ciwise.blackhole.domain.GenJournal;
import org.ciwise.blackhole.service.GenJournalService;
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
 * REST controller for managing GenJournal entries.
 */
@RestController
@RequestMapping("/api")
public class GenJournalResource {

    private final Logger log = LoggerFactory.getLogger(GenJournalResource.class);
        
    @Inject
    private GenJournalService genJournalService;

    /**
     * POST  /gen-journal-entries : Create a new genJournal entry.
     *
     * @param genJournalEntry the GenJournal entry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new genJournal, or with status 400 (Bad Request) if the genJournal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gen-journal-entries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenJournal> createGeneralJournalEntry(@RequestBody GenJournal genJournalEntry) throws URISyntaxException {
        log.debug("REST request to save GenJournal : {}", genJournalEntry);
        if (genJournalEntry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("genJournal", "idexists", "A new genJournal cannot already have an ID")).body(null);
        }
  
        GenJournal result = genJournalService.save(genJournalEntry);
        return ResponseEntity.created(new URI("/api/gen-journal-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("genJournal", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gen-journal-entries : Updates an existing GenJournal entry.
     *
     * @param genJournalEntry the GenJournal entry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated genJournal entry,
     * or with status 400 (Bad Request) if the genJournal entry is not valid,
     * or with status 500 (Internal Server Error) if the genJournal entry couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gen-journal-entries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenJournal> updateGeneralJournalEntry(@RequestBody GenJournal genJournalEntry) throws URISyntaxException {
        log.debug("REST request to update GenJournal entry : {}", genJournalEntry);
        if (genJournalEntry.getId() == null) {
            return createGeneralJournalEntry(genJournalEntry);
        }

        GenJournal result = genJournalService.save(genJournalEntry);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("genJournal", genJournalEntry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gen-journal-entries : get all the GenJournal entries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of genJournal entries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/gen-journal-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GenJournal>> getAllGeneralJournalEntries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of GenJournal entries");
        
        Page<GenJournal> page = genJournalService.findAll(pageable); 

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gen-journal-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gen-journal-entries/:id : get the GenJournal entry by "id".
     *
     * @param id the id of the GenJournal entry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the GenJournal, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/gen-journal-entries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenJournal> getGeneralJournalEntry(@PathVariable Long id) {
        log.debug("REST request to get GenJournal entry : {}", id);

        GenJournal genJournal = genJournalService.findOne(id);

        return Optional.ofNullable(genJournal)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gen-journal-entries/:id : delete the GenJournal entry by "id".
     *
     * @param id the id of the GenJournal entry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/gen-journal-entries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGeneralJournalEntry(@PathVariable Long id) {
        log.debug("REST request to delete GenJournal entry : {}", id);

        genJournalService.delete(id);
        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("genJournal", id.toString())).build();
    }

    /**
     * SEARCH  /_search/gen-journal-entries?query=:query : search for the GenJournal entry corresponding
     * to the query.
     *
     * @param query the query of the genJournal search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/gen-journal-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GenJournal>> searchGeneralJournalEntries(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of GenJournal entries for query {}", query);

        Page<GenJournal> page = genJournalService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/gen-journals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
