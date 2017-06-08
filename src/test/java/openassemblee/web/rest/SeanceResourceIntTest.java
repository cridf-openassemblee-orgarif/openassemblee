package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.Seance;
import openassemblee.domain.enumeration.TypeSeance;
import openassemblee.repository.SeanceRepository;
import openassemblee.repository.search.SeanceSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.service.SeanceService;
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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SeanceResource REST controller.
 *
 * @see SeanceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SeanceResourceIntTest {

    private static final String DEFAULT_INTITULE = "AAAAA";
    private static final String UPDATED_INTITULE = "BBBBB";


private static final TypeSeance DEFAULT_TYPE = TypeSeance.PLENIERE;
    private static final TypeSeance UPDATED_TYPE = TypeSeance.COMMISSION_PERMANENTE;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NOMBRE_SIGNATURES = 1;
    private static final Integer UPDATED_NOMBRE_SIGNATURES = 2;

    @Inject
    private SeanceRepository seanceRepository;

    @Inject
    private SeanceSearchRepository seanceSearchRepository;

    @Inject
    private SeanceService seanceService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restSeanceMockMvc;

    private Seance seance;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SeanceResource seanceResource = new SeanceResource();
        ReflectionTestUtils.setField(seanceResource, "seanceRepository", seanceRepository);
        ReflectionTestUtils.setField(seanceResource, "seanceSearchRepository", seanceSearchRepository);
        ReflectionTestUtils.setField(seanceResource, "seanceService", seanceService);
        ReflectionTestUtils.setField(seanceResource, "auditTrailService", auditTrailService);
        this.restSeanceMockMvc = MockMvcBuilders.standaloneSetup(seanceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        seance = new Seance();
        seance.setIntitule(DEFAULT_INTITULE);
        seance.setType(DEFAULT_TYPE);
        seance.setDate(DEFAULT_DATE);
        seance.setNombreSignatures(DEFAULT_NOMBRE_SIGNATURES);
    }

    @Test
    @Transactional
    public void createSeance() throws Exception {
        int databaseSizeBeforeCreate = seanceRepository.findAll().size();

        // Create the Seance

        restSeanceMockMvc.perform(post("/api/seances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(seance)))
                .andExpect(status().isCreated());

        // Validate the Seance in the database
        List<Seance> seances = seanceRepository.findAll();
        assertThat(seances).hasSize(databaseSizeBeforeCreate + 1);
        Seance testSeance = seances.get(seances.size() - 1);
        assertThat(testSeance.getIntitule()).isEqualTo(DEFAULT_INTITULE);
        assertThat(testSeance.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSeance.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSeance.getNombreSignatures()).isEqualTo(DEFAULT_NOMBRE_SIGNATURES);
    }

    @Test
    @Transactional
    public void getAllSeances() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        // Get all the seances
        restSeanceMockMvc.perform(get("/api/seances"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(seance.getId().intValue())))
                .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].nombreSignatures").value(hasItem(DEFAULT_NOMBRE_SIGNATURES)));
    }

    @Test
    @Transactional
    public void getSeance() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        // Get the seance
        restSeanceMockMvc.perform(get("/api/seances/{id}", seance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(seance.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.nombreSignatures").value(DEFAULT_NOMBRE_SIGNATURES));
    }

    @Test
    @Transactional
    public void getNonExistingSeance() throws Exception {
        // Get the seance
        restSeanceMockMvc.perform(get("/api/seances/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSeance() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

		int databaseSizeBeforeUpdate = seanceRepository.findAll().size();

        // Update the seance
        seance.setIntitule(UPDATED_INTITULE);
        seance.setType(UPDATED_TYPE);
        seance.setDate(UPDATED_DATE);
        seance.setNombreSignatures(UPDATED_NOMBRE_SIGNATURES);

        restSeanceMockMvc.perform(put("/api/seances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(seance)))
                .andExpect(status().isOk());

        // Validate the Seance in the database
        List<Seance> seances = seanceRepository.findAll();
        assertThat(seances).hasSize(databaseSizeBeforeUpdate);
        Seance testSeance = seances.get(seances.size() - 1);
        assertThat(testSeance.getIntitule()).isEqualTo(UPDATED_INTITULE);
        assertThat(testSeance.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSeance.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSeance.getNombreSignatures()).isEqualTo(UPDATED_NOMBRE_SIGNATURES);
    }

    @Test
    @Transactional
    public void deleteSeance() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

		int databaseSizeBeforeDelete = seanceRepository.findAll().size();

        // Get the seance
        restSeanceMockMvc.perform(delete("/api/seances/{id}", seance.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Seance> seances = seanceRepository.findAll();
        assertThat(seances).hasSize(databaseSizeBeforeDelete - 1);
    }
}