package org.ciwise.blackhole.web.rest;

import org.ciwise.blackhole.BlackholeApp;
import org.ciwise.blackhole.domain.Lead;
import org.ciwise.blackhole.repository.LeadRepository;
import org.ciwise.blackhole.repository.search.LeadSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LeadResource REST controller.
 *
 * @see LeadResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BlackholeApp.class)
@WebAppConfiguration
@IntegrationTest
public class LeadResourceIntTest {

    private static final String DEFAULT_REFERRINGCO = "AAAAA";
    private static final String UPDATED_REFERRINGCO = "BBBBB";
    private static final String DEFAULT_REFERRINGNAME = "AAAAA";
    private static final String UPDATED_REFERRINGNAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";
    private static final String DEFAULT_OPP = "AAAAA";
    private static final String UPDATED_OPP = "BBBBB";
    private static final String DEFAULT_OPPWHERE = "AAAAA";
    private static final String UPDATED_OPPWHERE = "BBBBB";
    private static final String DEFAULT_OPPWHO = "AAAAA";
    private static final String UPDATED_OPPWHO = "BBBBB";
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private LeadRepository leadRepository;

    @Inject
    private LeadSearchRepository leadSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLeadMockMvc;

    private Lead lead;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LeadResource leadResource = new LeadResource();
        ReflectionTestUtils.setField(leadResource, "leadSearchRepository", leadSearchRepository);
        ReflectionTestUtils.setField(leadResource, "leadRepository", leadRepository);
        this.restLeadMockMvc = MockMvcBuilders.standaloneSetup(leadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        leadSearchRepository.deleteAll();
        lead = new Lead();
        lead.setReferringco(DEFAULT_REFERRINGCO);
        lead.setReferringname(DEFAULT_REFERRINGNAME);
        lead.setEmail(DEFAULT_EMAIL);
        lead.setPhone(DEFAULT_PHONE);
        lead.setOpp(DEFAULT_OPP);
        lead.setOppwhere(DEFAULT_OPPWHERE);
        lead.setOppwho(DEFAULT_OPPWHO);
        lead.setNotes(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createLead() throws Exception {
        int databaseSizeBeforeCreate = leadRepository.findAll().size();

        // Create the Lead

        restLeadMockMvc.perform(post("/api/leads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lead)))
                .andExpect(status().isCreated());

        // Validate the Lead in the database
        List<Lead> leads = leadRepository.findAll();
        assertThat(leads).hasSize(databaseSizeBeforeCreate + 1);
        Lead testLead = leads.get(leads.size() - 1);
        assertThat(testLead.getReferringco()).isEqualTo(DEFAULT_REFERRINGCO);
        assertThat(testLead.getReferringname()).isEqualTo(DEFAULT_REFERRINGNAME);
        assertThat(testLead.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLead.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testLead.getOpp()).isEqualTo(DEFAULT_OPP);
        assertThat(testLead.getOppwhere()).isEqualTo(DEFAULT_OPPWHERE);
        assertThat(testLead.getOppwho()).isEqualTo(DEFAULT_OPPWHO);
        assertThat(testLead.getNotes()).isEqualTo(DEFAULT_NOTES);

        // Validate the Lead in ElasticSearch
        Lead leadEs = leadSearchRepository.findOne(testLead.getId());
        assertThat(leadEs).isEqualToComparingFieldByField(testLead);
    }

    @Test
    @Transactional
    public void getAllLeads() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leads
        restLeadMockMvc.perform(get("/api/leads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lead.getId().intValue())))
                .andExpect(jsonPath("$.[*].referringco").value(hasItem(DEFAULT_REFERRINGCO.toString())))
                .andExpect(jsonPath("$.[*].referringname").value(hasItem(DEFAULT_REFERRINGNAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].opp").value(hasItem(DEFAULT_OPP.toString())))
                .andExpect(jsonPath("$.[*].oppwhere").value(hasItem(DEFAULT_OPPWHERE.toString())))
                .andExpect(jsonPath("$.[*].oppwho").value(hasItem(DEFAULT_OPPWHO.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get the lead
        restLeadMockMvc.perform(get("/api/leads/{id}", lead.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lead.getId().intValue()))
            .andExpect(jsonPath("$.referringco").value(DEFAULT_REFERRINGCO.toString()))
            .andExpect(jsonPath("$.referringname").value(DEFAULT_REFERRINGNAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.opp").value(DEFAULT_OPP.toString()))
            .andExpect(jsonPath("$.oppwhere").value(DEFAULT_OPPWHERE.toString()))
            .andExpect(jsonPath("$.oppwho").value(DEFAULT_OPPWHO.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLead() throws Exception {
        // Get the lead
        restLeadMockMvc.perform(get("/api/leads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);
        leadSearchRepository.save(lead);
        int databaseSizeBeforeUpdate = leadRepository.findAll().size();

        // Update the lead
        Lead updatedLead = new Lead();
        updatedLead.setId(lead.getId());
        updatedLead.setReferringco(UPDATED_REFERRINGCO);
        updatedLead.setReferringname(UPDATED_REFERRINGNAME);
        updatedLead.setEmail(UPDATED_EMAIL);
        updatedLead.setPhone(UPDATED_PHONE);
        updatedLead.setOpp(UPDATED_OPP);
        updatedLead.setOppwhere(UPDATED_OPPWHERE);
        updatedLead.setOppwho(UPDATED_OPPWHO);
        updatedLead.setNotes(UPDATED_NOTES);

        restLeadMockMvc.perform(put("/api/leads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLead)))
                .andExpect(status().isOk());

        // Validate the Lead in the database
        List<Lead> leads = leadRepository.findAll();
        assertThat(leads).hasSize(databaseSizeBeforeUpdate);
        Lead testLead = leads.get(leads.size() - 1);
        assertThat(testLead.getReferringco()).isEqualTo(UPDATED_REFERRINGCO);
        assertThat(testLead.getReferringname()).isEqualTo(UPDATED_REFERRINGNAME);
        assertThat(testLead.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLead.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testLead.getOpp()).isEqualTo(UPDATED_OPP);
        assertThat(testLead.getOppwhere()).isEqualTo(UPDATED_OPPWHERE);
        assertThat(testLead.getOppwho()).isEqualTo(UPDATED_OPPWHO);
        assertThat(testLead.getNotes()).isEqualTo(UPDATED_NOTES);

        // Validate the Lead in ElasticSearch
        Lead leadEs = leadSearchRepository.findOne(testLead.getId());
        assertThat(leadEs).isEqualToComparingFieldByField(testLead);
    }

    @Test
    @Transactional
    public void deleteLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);
        leadSearchRepository.save(lead);
        int databaseSizeBeforeDelete = leadRepository.findAll().size();

        // Get the lead
        restLeadMockMvc.perform(delete("/api/leads/{id}", lead.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean leadExistsInEs = leadSearchRepository.exists(lead.getId());
        assertThat(leadExistsInEs).isFalse();

        // Validate the database is empty
        List<Lead> leads = leadRepository.findAll();
        assertThat(leads).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);
        leadSearchRepository.save(lead);

        // Search the lead
        restLeadMockMvc.perform(get("/api/_search/leads?query=id:" + lead.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lead.getId().intValue())))
            .andExpect(jsonPath("$.[*].referringco").value(hasItem(DEFAULT_REFERRINGCO.toString())))
            .andExpect(jsonPath("$.[*].referringname").value(hasItem(DEFAULT_REFERRINGNAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].opp").value(hasItem(DEFAULT_OPP.toString())))
            .andExpect(jsonPath("$.[*].oppwhere").value(hasItem(DEFAULT_OPPWHERE.toString())))
            .andExpect(jsonPath("$.[*].oppwho").value(hasItem(DEFAULT_OPPWHO.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }
}
