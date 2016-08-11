package org.ciwise.blackhole.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ciwise.blackhole.domain.Lead;
import org.ciwise.blackhole.repository.LeadRepository;
import org.ciwise.blackhole.repository.search.LeadSearchRepository;
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
 * REST controller for managing Lead.
 */
@RestController
@RequestMapping("/api")
public class LeadResource {

    private final Logger log = LoggerFactory.getLogger(LeadResource.class);
        
    @Inject
    private LeadRepository leadRepository;
    
    @Inject
    private LeadSearchRepository leadSearchRepository;
    
    /**
     * POST  /leads : Create a new lead.
     *
     * @param lead the lead to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lead, or with status 400 (Bad Request) if the lead has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/leads",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) throws URISyntaxException {
        log.debug("REST request to save Lead : {}", lead);
        if (lead.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lead", "idexists", "A new lead cannot already have an ID")).body(null);
        }
        Lead result = leadRepository.save(lead);
        leadSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/leads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lead", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leads : Updates an existing lead.
     *
     * @param lead the lead to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lead,
     * or with status 400 (Bad Request) if the lead is not valid,
     * or with status 500 (Internal Server Error) if the lead couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/leads",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lead> updateLead(@RequestBody Lead lead) throws URISyntaxException {
        log.debug("REST request to update Lead : {}", lead);
        if (lead.getId() == null) {
            return createLead(lead);
        }
        Lead result = leadRepository.save(lead);
        leadSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lead", lead.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leads : get all the leads.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of leads in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/leads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lead>> getAllLeads(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Leads");
        Page<Lead> page = leadRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leads/:id : get the "id" lead.
     *
     * @param id the id of the lead to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lead, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/leads/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lead> getLead(@PathVariable Long id) {
        log.debug("REST request to get Lead : {}", id);
        Lead lead = leadRepository.findOne(id);
        return Optional.ofNullable(lead)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /leads/:id : delete the "id" lead.
     *
     * @param id the id of the lead to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/leads/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        log.debug("REST request to delete Lead : {}", id);
        leadRepository.delete(id);
        leadSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lead", id.toString())).build();
    }

    /**
     * SEARCH  /_search/leads?query=:query : search for the lead corresponding
     * to the query.
     *
     * @param query the query of the lead search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/leads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lead>> searchLeads(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Leads for query {}", query);
        Page<Lead> page = leadSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/leads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
