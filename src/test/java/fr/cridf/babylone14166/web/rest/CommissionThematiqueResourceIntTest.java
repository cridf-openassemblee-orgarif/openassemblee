package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.CommissionThematique;
import fr.cridf.babylone14166.repository.CommissionThematiqueRepository;
import fr.cridf.babylone14166.repository.search.CommissionThematiqueSearchRepository;

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
 * Test class for the CommissionThematiqueResource REST controller.
 *
 * @see CommissionThematiqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CommissionThematiqueResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_NOM_COURT = "AAAAA";
    private static final String UPDATED_NOM_COURT = "BBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    @Inject
    private CommissionThematiqueRepository commissionThematiqueRepository;

    @Inject
    private CommissionThematiqueSearchRepository commissionThematiqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommissionThematiqueMockMvc;

    private CommissionThematique commissionThematique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommissionThematiqueResource commissionThematiqueResource = new CommissionThematiqueResource();
        ReflectionTestUtils.setField(commissionThematiqueResource, "commissionThematiqueRepository", commissionThematiqueRepository);
        ReflectionTestUtils.setField(commissionThematiqueResource, "commissionThematiqueSearchRepository", commissionThematiqueSearchRepository);
        this.restCommissionThematiqueMockMvc = MockMvcBuilders.standaloneSetup(commissionThematiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        commissionThematique = new CommissionThematique();
        commissionThematique.setNom(DEFAULT_NOM);
        commissionThematique.setNomCourt(DEFAULT_NOM_COURT);
        commissionThematique.setDateDebut(DEFAULT_DATE_DEBUT);
        commissionThematique.setDateFin(DEFAULT_DATE_FIN);
        commissionThematique.setMotifFin(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void createCommissionThematique() throws Exception {
        int databaseSizeBeforeCreate = commissionThematiqueRepository.findAll().size();

        // Create the CommissionThematique

        restCommissionThematiqueMockMvc.perform(post("/api/commissionThematiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commissionThematique)))
                .andExpect(status().isCreated());

        // Validate the CommissionThematique in the database
        List<CommissionThematique> commissionThematiques = commissionThematiqueRepository.findAll();
        assertThat(commissionThematiques).hasSize(databaseSizeBeforeCreate + 1);
        CommissionThematique testCommissionThematique = commissionThematiques.get(commissionThematiques.size() - 1);
        assertThat(testCommissionThematique.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCommissionThematique.getNomCourt()).isEqualTo(DEFAULT_NOM_COURT);
        assertThat(testCommissionThematique.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testCommissionThematique.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testCommissionThematique.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllCommissionThematiques() throws Exception {
        // Initialize the database
        commissionThematiqueRepository.saveAndFlush(commissionThematique);

        // Get all the commissionThematiques
        restCommissionThematiqueMockMvc.perform(get("/api/commissionThematiques"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(commissionThematique.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].nomCourt").value(hasItem(DEFAULT_NOM_COURT.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getCommissionThematique() throws Exception {
        // Initialize the database
        commissionThematiqueRepository.saveAndFlush(commissionThematique);

        // Get the commissionThematique
        restCommissionThematiqueMockMvc.perform(get("/api/commissionThematiques/{id}", commissionThematique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(commissionThematique.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.nomCourt").value(DEFAULT_NOM_COURT.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommissionThematique() throws Exception {
        // Get the commissionThematique
        restCommissionThematiqueMockMvc.perform(get("/api/commissionThematiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommissionThematique() throws Exception {
        // Initialize the database
        commissionThematiqueRepository.saveAndFlush(commissionThematique);

		int databaseSizeBeforeUpdate = commissionThematiqueRepository.findAll().size();

        // Update the commissionThematique
        commissionThematique.setNom(UPDATED_NOM);
        commissionThematique.setNomCourt(UPDATED_NOM_COURT);
        commissionThematique.setDateDebut(UPDATED_DATE_DEBUT);
        commissionThematique.setDateFin(UPDATED_DATE_FIN);
        commissionThematique.setMotifFin(UPDATED_MOTIF_FIN);

        restCommissionThematiqueMockMvc.perform(put("/api/commissionThematiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commissionThematique)))
                .andExpect(status().isOk());

        // Validate the CommissionThematique in the database
        List<CommissionThematique> commissionThematiques = commissionThematiqueRepository.findAll();
        assertThat(commissionThematiques).hasSize(databaseSizeBeforeUpdate);
        CommissionThematique testCommissionThematique = commissionThematiques.get(commissionThematiques.size() - 1);
        assertThat(testCommissionThematique.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCommissionThematique.getNomCourt()).isEqualTo(UPDATED_NOM_COURT);
        assertThat(testCommissionThematique.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testCommissionThematique.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testCommissionThematique.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteCommissionThematique() throws Exception {
        // Initialize the database
        commissionThematiqueRepository.saveAndFlush(commissionThematique);

		int databaseSizeBeforeDelete = commissionThematiqueRepository.findAll().size();

        // Get the commissionThematique
        restCommissionThematiqueMockMvc.perform(delete("/api/commissionThematiques/{id}", commissionThematique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CommissionThematique> commissionThematiques = commissionThematiqueRepository.findAll();
        assertThat(commissionThematiques).hasSize(databaseSizeBeforeDelete - 1);
    }
}