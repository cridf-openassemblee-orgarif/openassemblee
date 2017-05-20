package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.FonctionCommissionThematique;
import openassemblee.repository.FonctionCommissionThematiqueRepository;
import openassemblee.repository.search.FonctionCommissionThematiqueSearchRepository;

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
 * Test class for the FonctionCommissionThematiqueResource REST controller.
 *
 * @see FonctionCommissionThematiqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FonctionCommissionThematiqueResourceIntTest {

    private static final String DEFAULT_FONCTION = "AAAAA";
    private static final String UPDATED_FONCTION = "BBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    @Inject
    private FonctionCommissionThematiqueRepository fonctionCommissionThematiqueRepository;

    @Inject
    private FonctionCommissionThematiqueSearchRepository fonctionCommissionThematiqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restFonctionCommissionThematiqueMockMvc;

    private FonctionCommissionThematique fonctionCommissionThematique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FonctionCommissionThematiqueResource fonctionCommissionThematiqueResource = new FonctionCommissionThematiqueResource();
        ReflectionTestUtils.setField(fonctionCommissionThematiqueResource, "fonctionCommissionThematiqueRepository", fonctionCommissionThematiqueRepository);
        ReflectionTestUtils.setField(fonctionCommissionThematiqueResource, "fonctionCommissionThematiqueSearchRepository", fonctionCommissionThematiqueSearchRepository);
        ReflectionTestUtils.setField(fonctionCommissionThematiqueResource, "auditTrailService", auditTrailService);
        this.restFonctionCommissionThematiqueMockMvc = MockMvcBuilders.standaloneSetup(fonctionCommissionThematiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fonctionCommissionThematique = new FonctionCommissionThematique();
        fonctionCommissionThematique.setFonction(DEFAULT_FONCTION);
        fonctionCommissionThematique.setDateDebut(DEFAULT_DATE_DEBUT);
        fonctionCommissionThematique.setDateFin(DEFAULT_DATE_FIN);
        fonctionCommissionThematique.setMotifFin(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void createFonctionCommissionThematique() throws Exception {
        int databaseSizeBeforeCreate = fonctionCommissionThematiqueRepository.findAll().size();

        // Create the FonctionCommissionThematique

        restFonctionCommissionThematiqueMockMvc.perform(post("/api/fonctionCommissionThematiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionCommissionThematique)))
                .andExpect(status().isCreated());

        // Validate the FonctionCommissionThematique in the database
        List<FonctionCommissionThematique> fonctionCommissionThematiques = fonctionCommissionThematiqueRepository.findAll();
        assertThat(fonctionCommissionThematiques).hasSize(databaseSizeBeforeCreate + 1);
        FonctionCommissionThematique testFonctionCommissionThematique = fonctionCommissionThematiques.get(fonctionCommissionThematiques.size() - 1);
        assertThat(testFonctionCommissionThematique.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testFonctionCommissionThematique.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testFonctionCommissionThematique.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFonctionCommissionThematique.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllFonctionCommissionThematiques() throws Exception {
        // Initialize the database
        fonctionCommissionThematiqueRepository.saveAndFlush(fonctionCommissionThematique);

        // Get all the fonctionCommissionThematiques
        restFonctionCommissionThematiqueMockMvc.perform(get("/api/fonctionCommissionThematiques"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fonctionCommissionThematique.getId().intValue())))
                .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getFonctionCommissionThematique() throws Exception {
        // Initialize the database
        fonctionCommissionThematiqueRepository.saveAndFlush(fonctionCommissionThematique);

        // Get the fonctionCommissionThematique
        restFonctionCommissionThematiqueMockMvc.perform(get("/api/fonctionCommissionThematiques/{id}", fonctionCommissionThematique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fonctionCommissionThematique.getId().intValue()))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFonctionCommissionThematique() throws Exception {
        // Get the fonctionCommissionThematique
        restFonctionCommissionThematiqueMockMvc.perform(get("/api/fonctionCommissionThematiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFonctionCommissionThematique() throws Exception {
        // Initialize the database
        fonctionCommissionThematiqueRepository.saveAndFlush(fonctionCommissionThematique);

		int databaseSizeBeforeUpdate = fonctionCommissionThematiqueRepository.findAll().size();

        // Update the fonctionCommissionThematique
        fonctionCommissionThematique.setFonction(UPDATED_FONCTION);
        fonctionCommissionThematique.setDateDebut(UPDATED_DATE_DEBUT);
        fonctionCommissionThematique.setDateFin(UPDATED_DATE_FIN);
        fonctionCommissionThematique.setMotifFin(UPDATED_MOTIF_FIN);

        restFonctionCommissionThematiqueMockMvc.perform(put("/api/fonctionCommissionThematiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionCommissionThematique)))
                .andExpect(status().isOk());

        // Validate the FonctionCommissionThematique in the database
        List<FonctionCommissionThematique> fonctionCommissionThematiques = fonctionCommissionThematiqueRepository.findAll();
        assertThat(fonctionCommissionThematiques).hasSize(databaseSizeBeforeUpdate);
        FonctionCommissionThematique testFonctionCommissionThematique = fonctionCommissionThematiques.get(fonctionCommissionThematiques.size() - 1);
        assertThat(testFonctionCommissionThematique.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testFonctionCommissionThematique.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFonctionCommissionThematique.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFonctionCommissionThematique.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteFonctionCommissionThematique() throws Exception {
        // Initialize the database
        fonctionCommissionThematiqueRepository.saveAndFlush(fonctionCommissionThematique);

		int databaseSizeBeforeDelete = fonctionCommissionThematiqueRepository.findAll().size();

        // Get the fonctionCommissionThematique
        restFonctionCommissionThematiqueMockMvc.perform(delete("/api/fonctionCommissionThematiques/{id}", fonctionCommissionThematique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FonctionCommissionThematique> fonctionCommissionThematiques = fonctionCommissionThematiqueRepository.findAll();
        assertThat(fonctionCommissionThematiques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
