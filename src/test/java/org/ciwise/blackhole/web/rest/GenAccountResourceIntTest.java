package org.ciwise.blackhole.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.ciwise.blackhole.BlackholeApp;
import org.ciwise.blackhole.domain.GenAccount;
import org.ciwise.blackhole.service.GenAccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.IntegrationTest;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;


/**
 * Test class for the GenAccountResource REST controller.
 *
 * @see GenAccountResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = BlackholeApp.class)
//@WebAppConfiguration
//@IntegrationTest
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlackholeApp.class)
public class GenAccountResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";
    private static final String DEFAULT_DC = "AAAAA";
    private static final String UPDATED_DC = "BBBBB";
    private static final String DEFAULT_CNO = "AAAAA";
    private static final String UPDATED_CNO = "BBBBB";

    @Inject
    private GenAccountService genAccountService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGenAccountMockMvc;

    private GenAccount genAccount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GenAccountResource genAccountResource = new GenAccountResource();
        ReflectionTestUtils.setField(genAccountResource, "genAccountService", genAccountService);
        this.restGenAccountMockMvc = MockMvcBuilders.standaloneSetup(genAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
//        genAccountSearchRepository.deleteAll();

        genAccount = new GenAccount();
        genAccount.setName(DEFAULT_NAME);
        genAccount.setType(DEFAULT_TYPE);
        genAccount.setDc(DEFAULT_DC);
        genAccount.setCno(DEFAULT_CNO);
    }

    @Test
    @Transactional
    public void createGenAccount() throws Exception {
        int databaseSizeBeforeCreate = genAccountService.findAll().size();

        // Create the GenAccount

        restGenAccountMockMvc.perform(post("/api/gen-accounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genAccount)))
                .andExpect(status().isCreated());

        // Validate the GenAccount in the database
        //Page<SnapshotAccount> genPageAccounts = genAccountService.findAll(new PageRequest(0,1000));
        List<GenAccount> genAccounts = genAccountService.findAll();
        
        assertThat(genAccounts).hasSize(databaseSizeBeforeCreate + 1);
        GenAccount testGenAccount = genAccounts.get(genAccounts.size() - 1);
        assertThat(testGenAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGenAccount.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGenAccount.getDc()).isEqualTo(DEFAULT_DC);
        assertThat(testGenAccount.getCno()).isEqualTo(DEFAULT_CNO);

    }

    @Test
    @Transactional
    public void getAllGenAccounts() throws Exception {
        // Initialize the database
        genAccountService.save(genAccount);

        // Get all the genAccounts
        restGenAccountMockMvc.perform(get("/api/gen-accounts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(genAccount.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].dc").value(hasItem(DEFAULT_DC.toString())))
                .andExpect(jsonPath("$.[*].cno").value(hasItem(DEFAULT_CNO.toString())));
    }

    @Test
    @Transactional
    public void getGenAccount() throws Exception {
        // Initialize the database
        genAccountService.save(genAccount);

        // Get the genAccount
        restGenAccountMockMvc.perform(get("/api/gen-accounts/{id}", genAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(genAccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dc").value(DEFAULT_DC.toString()))
            .andExpect(jsonPath("$.cno").value(DEFAULT_CNO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGenAccount() throws Exception {
        // Get the genAccount
        restGenAccountMockMvc.perform(get("/api/gen-accounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGenAccount() throws Exception {

        // Initialize the database
        genAccountService.save(genAccount);
        int databaseSizeBeforeUpdate = genAccountService.findAll().size();
        
        // Update the genAccount
        GenAccount updatedGenAccount = new GenAccount();
        updatedGenAccount.setId(genAccount.getId());
        updatedGenAccount.setName(UPDATED_NAME);
        updatedGenAccount.setType(UPDATED_TYPE);
        updatedGenAccount.setDc(UPDATED_DC);
        updatedGenAccount.setCno(UPDATED_CNO);

        restGenAccountMockMvc.perform(put("/api/gen-accounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGenAccount)))
                .andExpect(status().isOk());

        // Validate the GenAccount in the database
        //Page<SnapshotAccount> genPageAccounts = genAccountService.findAll(new PageRequest(0,1000));
        List<GenAccount> genAccounts = genAccountService.findAll();
        
        assertThat(genAccounts).hasSize(databaseSizeBeforeUpdate);
        GenAccount testGenAccount = genAccounts.get(genAccounts.size() - 1);
        assertThat(testGenAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGenAccount.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGenAccount.getDc()).isEqualTo(UPDATED_DC);
        assertThat(testGenAccount.getCno()).isEqualTo(UPDATED_CNO);

    }

    @Test
    @Transactional
    public void deleteGenAccount() throws Exception {
        // Initialize the database
        genAccountService.save(genAccount);

        int databaseSizeBeforeDelete = genAccountService.findAll().size();

        // Get the genAccount
        restGenAccountMockMvc.perform(delete("/api/gen-accounts/{id}", genAccount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());


        // Validate the database is empty
        //Page<SnapshotAccount> genPageAccounts = genAccountService.findAll(new PageRequest(0,1000));
        List<GenAccount> genAccounts = genAccountService.findAll();
        assertThat(genAccounts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGenAccount() throws Exception {
        // Initialize the database
        genAccountService.save(genAccount);

        // Search the genAccount
        restGenAccountMockMvc.perform(get("/api/_search/gen-accounts?query=id:" + genAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dc").value(hasItem(DEFAULT_DC.toString())))
            .andExpect(jsonPath("$.[*].cno").value(hasItem(DEFAULT_CNO.toString())));
    }
}
