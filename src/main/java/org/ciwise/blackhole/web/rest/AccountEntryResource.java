package org.ciwise.blackhole.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ciwise.blackhole.domain.AccountEntry;
import org.ciwise.blackhole.repository.AccountEntryRepository;
import org.ciwise.blackhole.repository.search.AccountEntrySearchRepository;
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
 * REST controller for managing AccountEntry.
 */
@RestController
@RequestMapping("/api")
public class AccountEntryResource {

    private final Logger log = LoggerFactory.getLogger(AccountEntryResource.class);
        
    @Inject
    private AccountEntryRepository accountEntryRepository;
    
    @Inject
    private AccountEntrySearchRepository accountEntrySearchRepository;
    
    /**
     * POST  /account-entries : Create a new accountEntry.
     *
     * @param accountEntry the accountEntry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountEntry, or with status 400 (Bad Request) if the accountEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/account-entries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountEntry> createAccountEntry(@RequestBody AccountEntry accountEntry) throws URISyntaxException {
        log.debug("REST request to save AccountEntry : {}", accountEntry);
        if (accountEntry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("accountEntry", "idexists", "A new accountEntry cannot already have an ID")).body(null);
        }
        AccountEntry result = accountEntryRepository.save(accountEntry);
        accountEntrySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/account-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("accountEntry", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-entries : Updates an existing accountEntry.
     *
     * @param accountEntry the accountEntry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountEntry,
     * or with status 400 (Bad Request) if the accountEntry is not valid,
     * or with status 500 (Internal Server Error) if the accountEntry couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/account-entries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountEntry> updateAccountEntry(@RequestBody AccountEntry accountEntry) throws URISyntaxException {
        log.debug("REST request to update AccountEntry : {}", accountEntry);
        if (accountEntry.getId() == null) {
            return createAccountEntry(accountEntry);
        }
        AccountEntry result = accountEntryRepository.save(accountEntry);
        accountEntrySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("accountEntry", accountEntry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-entries : get all the accountEntries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountEntries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/account-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AccountEntry>> getAllAccountEntries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AccountEntries");
        Page<AccountEntry> page = accountEntryRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /account-entries/:id : get the "id" accountEntry.
     *
     * @param id the id of the accountEntry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountEntry, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/account-entries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountEntry> getAccountEntry(@PathVariable Long id) {
        log.debug("REST request to get AccountEntry : {}", id);
        AccountEntry accountEntry = accountEntryRepository.findOne(id);
        return Optional.ofNullable(accountEntry)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /account-entries/:id : delete the "id" accountEntry.
     *
     * @param id the id of the accountEntry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/account-entries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAccountEntry(@PathVariable Long id) {
        log.debug("REST request to delete AccountEntry : {}", id);
        accountEntryRepository.delete(id);
        accountEntrySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("accountEntry", id.toString())).build();
    }

    /**
     * SEARCH  /_search/account-entries?query=:query : search for the accountEntry corresponding
     * to the query.
     *
     * @param query the query of the accountEntry search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/account-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AccountEntry>> searchAccountEntries(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AccountEntries for query {}", query);
        Page<AccountEntry> page = accountEntrySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/account-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
