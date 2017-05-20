package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.FonctionCommissionPermanente;
import openassemblee.repository.FonctionCommissionPermanenteRepository;
import openassemblee.repository.search.FonctionCommissionPermanenteSearchRepository;

import openassemblee.service.AuditTrailService;
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
 * Test class for the FonctionCommissionPermanenteResource REST controller.
 *
 * @see FonctionCommissionPermanenteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FonctionCommissionPermanenteResourceIntTest {

    private static final String DEFAULT_FONCTION = "AAAAA";
    private static final String UPDATED_FONCTION = "BBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    @Inject
    private FonctionCommissionPermanenteRepository fonctionCommissionPermanenteRepository;

    @Inject
    private FonctionCommissionPermanenteSearchRepository fonctionCommissionPermanenteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restFonctionCommissionPermanenteMockMvc;

    private FonctionCommissionPermanente fonctionCommissionPermanente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FonctionCommissionPermanenteResource fonctionCommissionPermanenteResource = new FonctionCommissionPermanenteResource();
        ReflectionTestUtils.setField(fonctionCommissionPermanenteResource, "fonctionCommissionPermanenteRepository", fonctionCommissionPermanenteRepository);
        ReflectionTestUtils.setField(fonctionCommissionPermanenteResource, "fonctionCommissionPermanenteSearchRepository", fonctionCommissionPermanenteSearchRepository);
        ReflectionTestUtils.setField(fonctionCommissionPermanenteResource, "auditTrailService", auditTrailService);
        this.restFonctionCommissionPermanenteMockMvc = MockMvcBuilders.standaloneSetup(fonctionCommissionPermanenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fonctionCommissionPermanente = new FonctionCommissionPermanente();
        fonctionCommissionPermanente.setFonction(DEFAULT_FONCTION);
        fonctionCommissionPermanente.setDateDebut(DEFAULT_DATE_DEBUT);
        fonctionCommissionPermanente.setDateFin(DEFAULT_DATE_FIN);
        fonctionCommissionPermanente.setMotifFin(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void createFonctionCommissionPermanente() throws Exception {
        int databaseSizeBeforeCreate = fonctionCommissionPermanenteRepository.findAll().size();

        // Create the FonctionCommissionPermanente

        restFonctionCommissionPermanenteMockMvc.perform(post("/api/fonctionCommissionPermanentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionCommissionPermanente)))
                .andExpect(status().isCreated());

        // Validate the FonctionCommissionPermanente in the database
        List<FonctionCommissionPermanente> fonctionCommissionPermanentes = fonctionCommissionPermanenteRepository.findAll();
        assertThat(fonctionCommissionPermanentes).hasSize(databaseSizeBeforeCreate + 1);
        FonctionCommissionPermanente testFonctionCommissionPermanente = fonctionCommissionPermanentes.get(fonctionCommissionPermanentes.size() - 1);
        assertThat(testFonctionCommissionPermanente.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testFonctionCommissionPermanente.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testFonctionCommissionPermanente.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFonctionCommissionPermanente.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllFonctionCommissionPermanentes() throws Exception {
        // Initialize the database
        fonctionCommissionPermanenteRepository.saveAndFlush(fonctionCommissionPermanente);

        // Get all the fonctionCommissionPermanentes
        restFonctionCommissionPermanenteMockMvc.perform(get("/api/fonctionCommissionPermanentes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fonctionCommissionPermanente.getId().intValue())))
                .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getFonctionCommissionPermanente() throws Exception {
        // Initialize the database
        fonctionCommissionPermanenteRepository.saveAndFlush(fonctionCommissionPermanente);

        // Get the fonctionCommissionPermanente
        restFonctionCommissionPermanenteMockMvc.perform(get("/api/fonctionCommissionPermanentes/{id}", fonctionCommissionPermanente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fonctionCommissionPermanente.getId().intValue()))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFonctionCommissionPermanente() throws Exception {
        // Get the fonctionCommissionPermanente
        restFonctionCommissionPermanenteMockMvc.perform(get("/api/fonctionCommissionPermanentes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFonctionCommissionPermanente() throws Exception {
        // Initialize the database
        fonctionCommissionPermanenteRepository.saveAndFlush(fonctionCommissionPermanente);

		int databaseSizeBeforeUpdate = fonctionCommissionPermanenteRepository.findAll().size();

        // Update the fonctionCommissionPermanente
        fonctionCommissionPermanente.setFonction(UPDATED_FONCTION);
        fonctionCommissionPermanente.setDateDebut(UPDATED_DATE_DEBUT);
        fonctionCommissionPermanente.setDateFin(UPDATED_DATE_FIN);
        fonctionCommissionPermanente.setMotifFin(UPDATED_MOTIF_FIN);

        restFonctionCommissionPermanenteMockMvc.perform(put("/api/fonctionCommissionPermanentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionCommissionPermanente)))
                .andExpect(status().isOk());

        // Validate the FonctionCommissionPermanente in the database
        List<FonctionCommissionPermanente> fonctionCommissionPermanentes = fonctionCommissionPermanenteRepository.findAll();
        assertThat(fonctionCommissionPermanentes).hasSize(databaseSizeBeforeUpdate);
        FonctionCommissionPermanente testFonctionCommissionPermanente = fonctionCommissionPermanentes.get(fonctionCommissionPermanentes.size() - 1);
        assertThat(testFonctionCommissionPermanente.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testFonctionCommissionPermanente.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFonctionCommissionPermanente.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFonctionCommissionPermanente.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteFonctionCommissionPermanente() throws Exception {
        // Initialize the database
        fonctionCommissionPermanenteRepository.saveAndFlush(fonctionCommissionPermanente);

		int databaseSizeBeforeDelete = fonctionCommissionPermanenteRepository.findAll().size();

        // Get the fonctionCommissionPermanente
        restFonctionCommissionPermanenteMockMvc.perform(delete("/api/fonctionCommissionPermanentes/{id}", fonctionCommissionPermanente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FonctionCommissionPermanente> fonctionCommissionPermanentes = fonctionCommissionPermanenteRepository.findAll();
        assertThat(fonctionCommissionPermanentes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
