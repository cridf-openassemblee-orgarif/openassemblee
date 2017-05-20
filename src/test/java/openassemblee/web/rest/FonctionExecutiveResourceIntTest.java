package openassemblee.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import openassemblee.service.AuditTrailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import openassemblee.Application;
import openassemblee.domain.FonctionExecutive;
import openassemblee.repository.FonctionExecutiveRepository;
import openassemblee.repository.search.FonctionExecutiveSearchRepository;


/**
 * Test class for the FonctionExecutiveResource REST controller.
 *
 * @see FonctionExecutiveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FonctionExecutiveResourceIntTest {

    private static final String DEFAULT_FONCTION = "AAAAA";
    private static final String UPDATED_FONCTION = "BBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    @Inject
    private FonctionExecutiveRepository fonctionExecutiveRepository;

    @Inject
    private FonctionExecutiveSearchRepository fonctionExecutiveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restFonctionExecutiveMockMvc;

    private FonctionExecutive fonctionExecutive;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FonctionExecutiveResource fonctionExecutiveResource = new FonctionExecutiveResource();
        ReflectionTestUtils.setField(fonctionExecutiveResource, "fonctionExecutiveRepository", fonctionExecutiveRepository);
        ReflectionTestUtils.setField(fonctionExecutiveResource, "fonctionExecutiveSearchRepository", fonctionExecutiveSearchRepository);
        ReflectionTestUtils.setField(fonctionExecutiveResource, "auditTrailService", auditTrailService);
        this.restFonctionExecutiveMockMvc = MockMvcBuilders.standaloneSetup(fonctionExecutiveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fonctionExecutive = new FonctionExecutive();
        fonctionExecutive.setFonction(DEFAULT_FONCTION);
        fonctionExecutive.setDateDebut(DEFAULT_DATE_DEBUT);
        fonctionExecutive.setDateFin(DEFAULT_DATE_FIN);
        fonctionExecutive.setMotifFin(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void createFonctionExecutive() throws Exception {
        int databaseSizeBeforeCreate = fonctionExecutiveRepository.findAll().size();

        // Create the FonctionExecutive

        restFonctionExecutiveMockMvc.perform(post("/api/fonctionExecutives")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionExecutive)))
                .andExpect(status().isCreated());

        // Validate the FonctionExecutive in the database
        List<FonctionExecutive> fonctionExecutives = fonctionExecutiveRepository.findAll();
        assertThat(fonctionExecutives).hasSize(databaseSizeBeforeCreate + 1);
        FonctionExecutive testFonctionExecutive = fonctionExecutives.get(fonctionExecutives.size() - 1);
        assertThat(testFonctionExecutive.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testFonctionExecutive.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testFonctionExecutive.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFonctionExecutive.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllFonctionExecutives() throws Exception {
        // Initialize the database
        fonctionExecutiveRepository.saveAndFlush(fonctionExecutive);

        // Get all the fonctionExecutives
        restFonctionExecutiveMockMvc.perform(get("/api/fonctionExecutives"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fonctionExecutive.getId().intValue())))
                .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getFonctionExecutive() throws Exception {
        // Initialize the database
        fonctionExecutiveRepository.saveAndFlush(fonctionExecutive);

        // Get the fonctionExecutive
        restFonctionExecutiveMockMvc.perform(get("/api/fonctionExecutives/{id}", fonctionExecutive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fonctionExecutive.getId().intValue()))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFonctionExecutive() throws Exception {
        // Get the fonctionExecutive
        restFonctionExecutiveMockMvc.perform(get("/api/fonctionExecutives/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFonctionExecutive() throws Exception {
        // Initialize the database
        fonctionExecutiveRepository.saveAndFlush(fonctionExecutive);

		int databaseSizeBeforeUpdate = fonctionExecutiveRepository.findAll().size();

        // Update the fonctionExecutive
        fonctionExecutive.setFonction(UPDATED_FONCTION);
        fonctionExecutive.setDateDebut(UPDATED_DATE_DEBUT);
        fonctionExecutive.setDateFin(UPDATED_DATE_FIN);
        fonctionExecutive.setMotifFin(UPDATED_MOTIF_FIN);

        restFonctionExecutiveMockMvc.perform(put("/api/fonctionExecutives")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionExecutive)))
                .andExpect(status().isOk());

        // Validate the FonctionExecutive in the database
        List<FonctionExecutive> fonctionExecutives = fonctionExecutiveRepository.findAll();
        assertThat(fonctionExecutives).hasSize(databaseSizeBeforeUpdate);
        FonctionExecutive testFonctionExecutive = fonctionExecutives.get(fonctionExecutives.size() - 1);
        assertThat(testFonctionExecutive.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testFonctionExecutive.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFonctionExecutive.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFonctionExecutive.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteFonctionExecutive() throws Exception {
        // Initialize the database
        fonctionExecutiveRepository.saveAndFlush(fonctionExecutive);

		int databaseSizeBeforeDelete = fonctionExecutiveRepository.findAll().size();

        // Get the fonctionExecutive
        restFonctionExecutiveMockMvc.perform(delete("/api/fonctionExecutives/{id}", fonctionExecutive.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FonctionExecutive> fonctionExecutives = fonctionExecutiveRepository.findAll();
        assertThat(fonctionExecutives).hasSize(databaseSizeBeforeDelete - 1);
    }
}
