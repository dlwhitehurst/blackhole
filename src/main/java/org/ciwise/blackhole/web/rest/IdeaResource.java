package org.ciwise.blackhole.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ciwise.blackhole.domain.Idea;
import org.ciwise.blackhole.repository.IdeaRepository;
import org.ciwise.blackhole.repository.search.IdeaSearchRepository;
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
 * REST controller for managing Idea.
 */
@RestController
@RequestMapping("/api")
public class IdeaResource {

    private final Logger log = LoggerFactory.getLogger(IdeaResource.class);
        
    @Inject
    private IdeaRepository ideaRepository;
    
    @Inject
    private IdeaSearchRepository ideaSearchRepository;
    
    /**
     * POST  /ideas : Create a new idea.
     *
     * @param idea the idea to create
     * @return the ResponseEntity with status 201 (Created) and with body the new idea, or with status 400 (Bad Request) if the idea has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ideas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Idea> createIdea(@RequestBody Idea idea) throws URISyntaxException {
        log.debug("REST request to save Idea : {}", idea);
        if (idea.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("idea", "idexists", "A new idea cannot already have an ID")).body(null);
        }
        Idea result = ideaRepository.save(idea);
        ideaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ideas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("idea", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ideas : Updates an existing idea.
     *
     * @param idea the idea to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated idea,
     * or with status 400 (Bad Request) if the idea is not valid,
     * or with status 500 (Internal Server Error) if the idea couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ideas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Idea> updateIdea(@RequestBody Idea idea) throws URISyntaxException {
        log.debug("REST request to update Idea : {}", idea);
        if (idea.getId() == null) {
            return createIdea(idea);
        }
        Idea result = ideaRepository.save(idea);
        ideaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("idea", idea.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ideas : get all the ideas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ideas in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ideas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Idea>> getAllIdeas(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Ideas");
        Page<Idea> page = ideaRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ideas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ideas/:id : get the "id" idea.
     *
     * @param id the id of the idea to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the idea, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ideas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Idea> getIdea(@PathVariable Long id) {
        log.debug("REST request to get Idea : {}", id);
        Idea idea = ideaRepository.findOne(id);
        return Optional.ofNullable(idea)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ideas/:id : delete the "id" idea.
     *
     * @param id the id of the idea to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ideas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIdea(@PathVariable Long id) {
        log.debug("REST request to delete Idea : {}", id);
        ideaRepository.delete(id);
        ideaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("idea", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ideas?query=:query : search for the idea corresponding
     * to the query.
     *
     * @param query the query of the idea search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/ideas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Idea>> searchIdeas(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Ideas for query {}", query);
        Page<Idea> page = ideaSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ideas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
