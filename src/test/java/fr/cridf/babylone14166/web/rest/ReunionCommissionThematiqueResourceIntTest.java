package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.ReunionCommissionThematique;
import fr.cridf.babylone14166.repository.ReunionCommissionThematiqueRepository;
import fr.cridf.babylone14166.repository.search.ReunionCommissionThematiqueSearchRepository;

import fr.cridf.babylone14166.service.AuditTrailService;
import fr.cridf.babylone14166.service.ReunionCommissionThematiqueService;
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
 * Test class for the ReunionCommissionThematiqueResource REST controller.
 *
 * @see ReunionCommissionThematiqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ReunionCommissionThematiqueResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_HEURE_DEBUT = "AAAAA";
    private static final String UPDATED_HEURE_DEBUT = "BBBBB";
    private static final String DEFAULT_HEURE_FIN = "AAAAA";
    private static final String UPDATED_HEURE_FIN = "BBBBB";

    @Inject
    private ReunionCommissionThematiqueRepository reunionCommissionThematiqueRepository;

    @Inject
    private ReunionCommissionThematiqueSearchRepository reunionCommissionThematiqueSearchRepository;

    @Inject
    private ReunionCommissionThematiqueService reunionCommissionThematiqueService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restReunionCommissionThematiqueMockMvc;

    private ReunionCommissionThematique reunionCommissionThematique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReunionCommissionThematiqueResource reunionCommissionThematiqueResource = new ReunionCommissionThematiqueResource();
        ReflectionTestUtils.setField(reunionCommissionThematiqueResource, "reunionCommissionThematiqueRepository", reunionCommissionThematiqueRepository);
        ReflectionTestUtils.setField(reunionCommissionThematiqueResource, "reunionCommissionThematiqueSearchRepository", reunionCommissionThematiqueSearchRepository);
        ReflectionTestUtils.setField(reunionCommissionThematiqueResource, "reunionCommissionThematiqueService", reunionCommissionThematiqueService);
        ReflectionTestUtils.setField(reunionCommissionThematiqueResource, "auditTrailService", auditTrailService);
        this.restReunionCommissionThematiqueMockMvc = MockMvcBuilders.standaloneSetup(reunionCommissionThematiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        reunionCommissionThematique = new ReunionCommissionThematique();
        reunionCommissionThematique.setLibelle(DEFAULT_LIBELLE);
        reunionCommissionThematique.setDate(DEFAULT_DATE);
        reunionCommissionThematique.setHeureDebut(DEFAULT_HEURE_DEBUT);
        reunionCommissionThematique.setHeureFin(DEFAULT_HEURE_FIN);
    }

    @Test
    @Transactional
    public void createReunionCommissionThematique() throws Exception {
        int databaseSizeBeforeCreate = reunionCommissionThematiqueRepository.findAll().size();

        // Create the ReunionCommissionThematique

        restReunionCommissionThematiqueMockMvc.perform(post("/api/reunionCommissionThematiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reunionCommissionThematique)))
                .andExpect(status().isCreated());

        // Validate the ReunionCommissionThematique in the database
        List<ReunionCommissionThematique> reunionCommissionThematiques = reunionCommissionThematiqueRepository.findAll();
        assertThat(reunionCommissionThematiques).hasSize(databaseSizeBeforeCreate + 1);
        ReunionCommissionThematique testReunionCommissionThematique = reunionCommissionThematiques.get(reunionCommissionThematiques.size() - 1);
        assertThat(testReunionCommissionThematique.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testReunionCommissionThematique.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testReunionCommissionThematique.getHeureDebut()).isEqualTo(DEFAULT_HEURE_DEBUT);
        assertThat(testReunionCommissionThematique.getHeureFin()).isEqualTo(DEFAULT_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllReunionCommissionThematiques() throws Exception {
        // Initialize the database
        reunionCommissionThematiqueRepository.saveAndFlush(reunionCommissionThematique);

        // Get all the reunionCommissionThematiques
        restReunionCommissionThematiqueMockMvc.perform(get("/api/reunionCommissionThematiques"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reunionCommissionThematique.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN.toString())));
    }

    @Test
    @Transactional
    public void getReunionCommissionThematique() throws Exception {
        // Initialize the database
        reunionCommissionThematiqueRepository.saveAndFlush(reunionCommissionThematique);

        // Get the reunionCommissionThematique
        restReunionCommissionThematiqueMockMvc.perform(get("/api/reunionCommissionThematiques/{id}", reunionCommissionThematique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(reunionCommissionThematique.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.heureDebut").value(DEFAULT_HEURE_DEBUT.toString()))
            .andExpect(jsonPath("$.heureFin").value(DEFAULT_HEURE_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReunionCommissionThematique() throws Exception {
        // Get the reunionCommissionThematique
        restReunionCommissionThematiqueMockMvc.perform(get("/api/reunionCommissionThematiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReunionCommissionThematique() throws Exception {
        // Initialize the database
        reunionCommissionThematiqueRepository.saveAndFlush(reunionCommissionThematique);

		int databaseSizeBeforeUpdate = reunionCommissionThematiqueRepository.findAll().size();

        // Update the reunionCommissionThematique
        reunionCommissionThematique.setLibelle(UPDATED_LIBELLE);
        reunionCommissionThematique.setDate(UPDATED_DATE);
        reunionCommissionThematique.setHeureDebut(UPDATED_HEURE_DEBUT);
        reunionCommissionThematique.setHeureFin(UPDATED_HEURE_FIN);

        restReunionCommissionThematiqueMockMvc.perform(put("/api/reunionCommissionThematiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reunionCommissionThematique)))
                .andExpect(status().isOk());

        // Validate the ReunionCommissionThematique in the database
        List<ReunionCommissionThematique> reunionCommissionThematiques = reunionCommissionThematiqueRepository.findAll();
        assertThat(reunionCommissionThematiques).hasSize(databaseSizeBeforeUpdate);
        ReunionCommissionThematique testReunionCommissionThematique = reunionCommissionThematiques.get(reunionCommissionThematiques.size() - 1);
        assertThat(testReunionCommissionThematique.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testReunionCommissionThematique.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReunionCommissionThematique.getHeureDebut()).isEqualTo(UPDATED_HEURE_DEBUT);
        assertThat(testReunionCommissionThematique.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void deleteReunionCommissionThematique() throws Exception {
        // Initialize the database
        reunionCommissionThematiqueRepository.saveAndFlush(reunionCommissionThematique);

		int databaseSizeBeforeDelete = reunionCommissionThematiqueRepository.findAll().size();

        // Get the reunionCommissionThematique
        restReunionCommissionThematiqueMockMvc.perform(delete("/api/reunionCommissionThematiques/{id}", reunionCommissionThematique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ReunionCommissionThematique> reunionCommissionThematiques = reunionCommissionThematiqueRepository.findAll();
        assertThat(reunionCommissionThematiques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
