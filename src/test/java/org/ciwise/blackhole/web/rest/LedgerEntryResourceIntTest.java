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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.ciwise.blackhole.BlackholeApp;
import org.ciwise.blackhole.domain.GenJournal;
import org.ciwise.blackhole.service.GenJournalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;


/**
 * Test class for the LedgerEntryResource REST controller.
 *
 * @see GenJournalResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BlackholeApp.class)
@WebAppConfiguration
@IntegrationTest
public class LedgerEntryResourceIntTest {


//    private static final LocalDate DEFAULT_ENTRYDATE = LocalDate.ofEpochDay(0L);
//    private static final LocalDate UPDATED_ENTRYDATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_ENTRYDATE = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
    private static final ZonedDateTime UPDATED_ENTRYDATE = ZonedDateTime.now();

    private static final String DEFAULT_TRANSACTION = "AAAAA";
    private static final String UPDATED_TRANSACTION = "BBBBB";
    private static final String DEFAULT_DACCTNO = "AAAAA";
    private static final String UPDATED_DACCTNO = "BBBBB";
    private static final String DEFAULT_CACCTNO = "AAAAA";
    private static final String UPDATED_CACCTNO = "BBBBB";
    private static final String DEFAULT_DADEBIT = "AAAAA";
    private static final String UPDATED_DADEBIT = "BBBBB";
    private static final String DEFAULT_DACREDIT = "AAAAA";
    private static final String UPDATED_DACREDIT = "BBBBB";
    private static final String DEFAULT_CADEBIT = "AAAAA";
    private static final String UPDATED_CADEBIT = "BBBBB";
    private static final String DEFAULT_CACREDIT = "AAAAA";
    private static final String UPDATED_CACREDIT = "BBBBB";
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private GenJournalService ledgerEntryService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLedgerEntryMockMvc;

    private GenJournal ledgerEntry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GenJournalResource ledgerEntryResource = new GenJournalResource();
        ReflectionTestUtils.setField(ledgerEntryResource, "ledgerEntryService", ledgerEntryService);

        this.restLedgerEntryMockMvc = MockMvcBuilders.standaloneSetup(ledgerEntryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        //ledgerEntrySearchRepository.deleteAll();
        ledgerEntry = new GenJournal();
        ledgerEntry.setEntrydate(DEFAULT_ENTRYDATE);
        ledgerEntry.setTransaction(DEFAULT_TRANSACTION);
        ledgerEntry.setDacctno(DEFAULT_DACCTNO);
        ledgerEntry.setCacctno(DEFAULT_CACCTNO);
        ledgerEntry.setDadebit(DEFAULT_DADEBIT);
        ledgerEntry.setDacredit(DEFAULT_DACREDIT);
        ledgerEntry.setCadebit(DEFAULT_CADEBIT);
        ledgerEntry.setCacredit(DEFAULT_CACREDIT);
        ledgerEntry.setNotes(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createLedgerEntry() throws Exception {
        int databaseSizeBeforeCreate = ledgerEntryService.findAll(new PageRequest(0,1000)).getContent().size();

        // Create the LedgerEntry

        restLedgerEntryMockMvc.perform(post("/api/ledger-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ledgerEntry)))
                .andExpect(status().isCreated());

        // Validate the LedgerEntry in the database
        Page<GenJournal> ledgerPageEntries = ledgerEntryService.findAll(new PageRequest(0,1000));
        List<GenJournal> ledgerEntries = ledgerPageEntries.getContent();

        assertThat(ledgerEntries).hasSize(databaseSizeBeforeCreate + 1);
        GenJournal testLedgerEntry = ledgerEntries.get(ledgerEntries.size() - 1);
        assertThat(testLedgerEntry.getEntrydate()).isEqualTo(DEFAULT_ENTRYDATE);
        assertThat(testLedgerEntry.getTransaction()).isEqualTo(DEFAULT_TRANSACTION);
        assertThat(testLedgerEntry.getDacctno()).isEqualTo(DEFAULT_DACCTNO);
        assertThat(testLedgerEntry.getCacctno()).isEqualTo(DEFAULT_CACCTNO);
        assertThat(testLedgerEntry.getDadebit()).isEqualTo(DEFAULT_DADEBIT);
        assertThat(testLedgerEntry.getDacredit()).isEqualTo(DEFAULT_DACREDIT);
        assertThat(testLedgerEntry.getCadebit()).isEqualTo(DEFAULT_CADEBIT);
        assertThat(testLedgerEntry.getCacredit()).isEqualTo(DEFAULT_CACREDIT);
        assertThat(testLedgerEntry.getNotes()).isEqualTo(DEFAULT_NOTES);

    }

    @Test
    @Transactional
    public void getAllLedgerEntries() throws Exception {
        // Initialize the database
        ledgerEntryService.save(ledgerEntry);

        // Get all the ledgerEntries
        restLedgerEntryMockMvc.perform(get("/api/ledger-entries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ledgerEntry.getId().intValue())))
                .andExpect(jsonPath("$.[*].entrydate").value(hasItem(DEFAULT_ENTRYDATE.toString())))
                .andExpect(jsonPath("$.[*].transaction").value(hasItem(DEFAULT_TRANSACTION.toString())))
                .andExpect(jsonPath("$.[*].dacctno").value(hasItem(DEFAULT_DACCTNO.toString())))
                .andExpect(jsonPath("$.[*].cacctno").value(hasItem(DEFAULT_CACCTNO.toString())))
                .andExpect(jsonPath("$.[*].dadebit").value(hasItem(DEFAULT_DADEBIT.toString())))
                .andExpect(jsonPath("$.[*].dacredit").value(hasItem(DEFAULT_DACREDIT.toString())))
                .andExpect(jsonPath("$.[*].cadebit").value(hasItem(DEFAULT_CADEBIT.toString())))
                .andExpect(jsonPath("$.[*].cacredit").value(hasItem(DEFAULT_CACREDIT.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getLedgerEntry() throws Exception {
        // Initialize the database
        ledgerEntryService.save(ledgerEntry);

        // Get the ledgerEntry
        restLedgerEntryMockMvc.perform(get("/api/ledger-entries/{id}", ledgerEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ledgerEntry.getId().intValue()))
            .andExpect(jsonPath("$.entrydate").value(DEFAULT_ENTRYDATE.toString()))
            .andExpect(jsonPath("$.transaction").value(DEFAULT_TRANSACTION.toString()))
            .andExpect(jsonPath("$.dacctno").value(DEFAULT_DACCTNO.toString()))
            .andExpect(jsonPath("$.cacctno").value(DEFAULT_CACCTNO.toString()))
            .andExpect(jsonPath("$.dadebit").value(DEFAULT_DADEBIT.toString()))
            .andExpect(jsonPath("$.dacredit").value(DEFAULT_DACREDIT.toString()))
            .andExpect(jsonPath("$.cadebit").value(DEFAULT_CADEBIT.toString()))
            .andExpect(jsonPath("$.cacredit").value(DEFAULT_CACREDIT.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLedgerEntry() throws Exception {
        // Get the ledgerEntry
        restLedgerEntryMockMvc.perform(get("/api/ledger-entries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLedgerEntry() throws Exception {
        // Initialize the database
        ledgerEntryService.save(ledgerEntry);

        int databaseSizeBeforeUpdate = ledgerEntryService.findAll(new PageRequest(0,1000)).getContent().size();

        // Update the ledgerEntry
        GenJournal updatedLedgerEntry = new GenJournal();
        updatedLedgerEntry.setId(ledgerEntry.getId());
        updatedLedgerEntry.setEntrydate(UPDATED_ENTRYDATE);
        updatedLedgerEntry.setTransaction(UPDATED_TRANSACTION);
        updatedLedgerEntry.setDacctno(UPDATED_DACCTNO);
        updatedLedgerEntry.setCacctno(UPDATED_CACCTNO);
        updatedLedgerEntry.setDadebit(UPDATED_DADEBIT);
        updatedLedgerEntry.setDacredit(UPDATED_DACREDIT);
        updatedLedgerEntry.setCadebit(UPDATED_CADEBIT);
        updatedLedgerEntry.setCacredit(UPDATED_CACREDIT);
        updatedLedgerEntry.setNotes(UPDATED_NOTES);

        restLedgerEntryMockMvc.perform(put("/api/ledger-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLedgerEntry)))
                .andExpect(status().isOk());

        // Validate the LedgerEntry in the database
        Page<GenJournal> ledgerPageEntries = ledgerEntryService.findAll(new PageRequest(0,1000));
        List<GenJournal> ledgerEntries = ledgerPageEntries.getContent();

        assertThat(ledgerEntries).hasSize(databaseSizeBeforeUpdate);
        GenJournal testLedgerEntry = ledgerEntries.get(ledgerEntries.size() - 1);
        assertThat(testLedgerEntry.getEntrydate()).isEqualTo(UPDATED_ENTRYDATE);
        assertThat(testLedgerEntry.getTransaction()).isEqualTo(UPDATED_TRANSACTION);
        assertThat(testLedgerEntry.getDacctno()).isEqualTo(UPDATED_DACCTNO);
        assertThat(testLedgerEntry.getCacctno()).isEqualTo(UPDATED_CACCTNO);
        assertThat(testLedgerEntry.getDadebit()).isEqualTo(UPDATED_DADEBIT);
        assertThat(testLedgerEntry.getDacredit()).isEqualTo(UPDATED_DACREDIT);
        assertThat(testLedgerEntry.getCadebit()).isEqualTo(UPDATED_CADEBIT);
        assertThat(testLedgerEntry.getCacredit()).isEqualTo(UPDATED_CACREDIT);
        assertThat(testLedgerEntry.getNotes()).isEqualTo(UPDATED_NOTES);

    }

    @Test
    @Transactional
    public void deleteLedgerEntry() throws Exception {
        // Initialize the database
        ledgerEntryService.save(ledgerEntry);

        int databaseSizeBeforeDelete = ledgerEntryService.findAll(new PageRequest(0,1000)).getContent().size();

        // Get the ledgerEntry
        restLedgerEntryMockMvc.perform(delete("/api/ledger-entries/{id}", ledgerEntry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());


        // Validate the database is empty
        Page<GenJournal> ledgerPageEntries = ledgerEntryService.findAll(new PageRequest(0,1000));
        List<GenJournal> ledgerEntries = ledgerPageEntries.getContent();

        assertThat(ledgerEntries).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLedgerEntry() throws Exception {
        // Initialize the database
        ledgerEntryService.save(ledgerEntry);

        // Search the ledgerEntry
        restLedgerEntryMockMvc.perform(get("/api/_search/ledger-entries?query=id:" + ledgerEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ledgerEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].entrydate").value(hasItem(DEFAULT_ENTRYDATE.toString())))
            .andExpect(jsonPath("$.[*].transaction").value(hasItem(DEFAULT_TRANSACTION.toString())))
            .andExpect(jsonPath("$.[*].dacctno").value(hasItem(DEFAULT_DACCTNO.toString())))
            .andExpect(jsonPath("$.[*].cacctno").value(hasItem(DEFAULT_CACCTNO.toString())))
            .andExpect(jsonPath("$.[*].dadebit").value(hasItem(DEFAULT_DADEBIT.toString())))
            .andExpect(jsonPath("$.[*].dacredit").value(hasItem(DEFAULT_DACREDIT.toString())))
            .andExpect(jsonPath("$.[*].cadebit").value(hasItem(DEFAULT_CADEBIT.toString())))
            .andExpect(jsonPath("$.[*].cacredit").value(hasItem(DEFAULT_CACREDIT.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }
}
