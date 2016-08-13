package org.ciwise.blackhole.web.rest;

import org.ciwise.blackhole.BlackholeApp;
import org.ciwise.blackhole.domain.Idea;
import org.ciwise.blackhole.repository.IdeaRepository;
import org.ciwise.blackhole.repository.search.IdeaSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the IdeaResource REST controller.
 *
 * @see IdeaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BlackholeApp.class)
@WebAppConfiguration
@IntegrationTest
public class IdeaResourceIntTest {


    private static final LocalDate DEFAULT_ENTRYDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENTRYDATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_IDEA = "AAAAA";
    private static final String UPDATED_IDEA = "BBBBB";

    private static final Boolean DEFAULT_INPROCESS = false;
    private static final Boolean UPDATED_INPROCESS = true;

    @Inject
    private IdeaRepository ideaRepository;

    @Inject
    private IdeaSearchRepository ideaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restIdeaMockMvc;

    private Idea idea;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IdeaResource ideaResource = new IdeaResource();
        ReflectionTestUtils.setField(ideaResource, "ideaSearchRepository", ideaSearchRepository);
        ReflectionTestUtils.setField(ideaResource, "ideaRepository", ideaRepository);
        this.restIdeaMockMvc = MockMvcBuilders.standaloneSetup(ideaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ideaSearchRepository.deleteAll();
        idea = new Idea();
        idea.setEntrydate(DEFAULT_ENTRYDATE);
        idea.setIdea(DEFAULT_IDEA);
        idea.setInprocess(DEFAULT_INPROCESS);
    }

    @Test
    @Transactional
    public void createIdea() throws Exception {
        int databaseSizeBeforeCreate = ideaRepository.findAll().size();

        // Create the Idea

        restIdeaMockMvc.perform(post("/api/ideas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(idea)))
                .andExpect(status().isCreated());

        // Validate the Idea in the database
        List<Idea> ideas = ideaRepository.findAll();
        assertThat(ideas).hasSize(databaseSizeBeforeCreate + 1);
        Idea testIdea = ideas.get(ideas.size() - 1);
        assertThat(testIdea.getEntrydate()).isEqualTo(DEFAULT_ENTRYDATE);
        assertThat(testIdea.getIdea()).isEqualTo(DEFAULT_IDEA);
        assertThat(testIdea.isInprocess()).isEqualTo(DEFAULT_INPROCESS);

        // Validate the Idea in ElasticSearch
        Idea ideaEs = ideaSearchRepository.findOne(testIdea.getId());
        assertThat(ideaEs).isEqualToComparingFieldByField(testIdea);
    }

    @Test
    @Transactional
    public void getAllIdeas() throws Exception {
        // Initialize the database
        ideaRepository.saveAndFlush(idea);

        // Get all the ideas
        restIdeaMockMvc.perform(get("/api/ideas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(idea.getId().intValue())))
                .andExpect(jsonPath("$.[*].entrydate").value(hasItem(DEFAULT_ENTRYDATE.toString())))
                .andExpect(jsonPath("$.[*].idea").value(hasItem(DEFAULT_IDEA.toString())))
                .andExpect(jsonPath("$.[*].inprocess").value(hasItem(DEFAULT_INPROCESS.booleanValue())));
    }

    @Test
    @Transactional
    public void getIdea() throws Exception {
        // Initialize the database
        ideaRepository.saveAndFlush(idea);

        // Get the idea
        restIdeaMockMvc.perform(get("/api/ideas/{id}", idea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(idea.getId().intValue()))
            .andExpect(jsonPath("$.entrydate").value(DEFAULT_ENTRYDATE.toString()))
            .andExpect(jsonPath("$.idea").value(DEFAULT_IDEA.toString()))
            .andExpect(jsonPath("$.inprocess").value(DEFAULT_INPROCESS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingIdea() throws Exception {
        // Get the idea
        restIdeaMockMvc.perform(get("/api/ideas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIdea() throws Exception {
        // Initialize the database
        ideaRepository.saveAndFlush(idea);
        ideaSearchRepository.save(idea);
        int databaseSizeBeforeUpdate = ideaRepository.findAll().size();

        // Update the idea
        Idea updatedIdea = new Idea();
        updatedIdea.setId(idea.getId());
        updatedIdea.setEntrydate(UPDATED_ENTRYDATE);
        updatedIdea.setIdea(UPDATED_IDEA);
        updatedIdea.setInprocess(UPDATED_INPROCESS);

        restIdeaMockMvc.perform(put("/api/ideas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedIdea)))
                .andExpect(status().isOk());

        // Validate the Idea in the database
        List<Idea> ideas = ideaRepository.findAll();
        assertThat(ideas).hasSize(databaseSizeBeforeUpdate);
        Idea testIdea = ideas.get(ideas.size() - 1);
        assertThat(testIdea.getEntrydate()).isEqualTo(UPDATED_ENTRYDATE);
        assertThat(testIdea.getIdea()).isEqualTo(UPDATED_IDEA);
        assertThat(testIdea.isInprocess()).isEqualTo(UPDATED_INPROCESS);

        // Validate the Idea in ElasticSearch
        Idea ideaEs = ideaSearchRepository.findOne(testIdea.getId());
        assertThat(ideaEs).isEqualToComparingFieldByField(testIdea);
    }

    @Test
    @Transactional
    public void deleteIdea() throws Exception {
        // Initialize the database
        ideaRepository.saveAndFlush(idea);
        ideaSearchRepository.save(idea);
        int databaseSizeBeforeDelete = ideaRepository.findAll().size();

        // Get the idea
        restIdeaMockMvc.perform(delete("/api/ideas/{id}", idea.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean ideaExistsInEs = ideaSearchRepository.exists(idea.getId());
        assertThat(ideaExistsInEs).isFalse();

        // Validate the database is empty
        List<Idea> ideas = ideaRepository.findAll();
        assertThat(ideas).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIdea() throws Exception {
        // Initialize the database
        ideaRepository.saveAndFlush(idea);
        ideaSearchRepository.save(idea);

        // Search the idea
        restIdeaMockMvc.perform(get("/api/_search/ideas?query=id:" + idea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idea.getId().intValue())))
            .andExpect(jsonPath("$.[*].entrydate").value(hasItem(DEFAULT_ENTRYDATE.toString())))
            .andExpect(jsonPath("$.[*].idea").value(hasItem(DEFAULT_IDEA.toString())))
            .andExpect(jsonPath("$.[*].inprocess").value(hasItem(DEFAULT_INPROCESS.booleanValue())));
    }
}
