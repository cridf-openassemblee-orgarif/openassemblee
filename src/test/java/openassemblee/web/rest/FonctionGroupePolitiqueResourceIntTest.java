package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.FonctionGroupePolitique;
import openassemblee.repository.FonctionGroupePolitiqueRepository;
import openassemblee.repository.search.FonctionGroupePolitiqueSearchRepository;

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
 * Test class for the FonctionGroupePolitiqueResource REST controller.
 *
 * @see FonctionGroupePolitiqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FonctionGroupePolitiqueResourceIntTest {

    private static final String DEFAULT_FONCTION = "AAAAA";
    private static final String UPDATED_FONCTION = "BBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    @Inject
    private FonctionGroupePolitiqueRepository fonctionGroupePolitiqueRepository;

    @Inject
    private FonctionGroupePolitiqueSearchRepository fonctionGroupePolitiqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restFonctionGroupePolitiqueMockMvc;

    private FonctionGroupePolitique fonctionGroupePolitique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FonctionGroupePolitiqueResource fonctionGroupePolitiqueResource = new FonctionGroupePolitiqueResource();
        ReflectionTestUtils.setField(fonctionGroupePolitiqueResource, "fonctionGroupePolitiqueRepository", fonctionGroupePolitiqueRepository);
        ReflectionTestUtils.setField(fonctionGroupePolitiqueResource, "fonctionGroupePolitiqueSearchRepository", fonctionGroupePolitiqueSearchRepository);
        ReflectionTestUtils.setField(fonctionGroupePolitiqueResource, "auditTrailService", auditTrailService);
        this.restFonctionGroupePolitiqueMockMvc = MockMvcBuilders.standaloneSetup(fonctionGroupePolitiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fonctionGroupePolitique = new FonctionGroupePolitique();
        fonctionGroupePolitique.setFonction(DEFAULT_FONCTION);
        fonctionGroupePolitique.setDateDebut(DEFAULT_DATE_DEBUT);
        fonctionGroupePolitique.setDateFin(DEFAULT_DATE_FIN);
        fonctionGroupePolitique.setMotifFin(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void createFonctionGroupePolitique() throws Exception {
        int databaseSizeBeforeCreate = fonctionGroupePolitiqueRepository.findAll().size();

        // Create the FonctionGroupePolitique

        restFonctionGroupePolitiqueMockMvc.perform(post("/api/fonctionGroupePolitiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionGroupePolitique)))
                .andExpect(status().isCreated());

        // Validate the FonctionGroupePolitique in the database
        List<FonctionGroupePolitique> fonctionGroupePolitiques = fonctionGroupePolitiqueRepository.findAll();
        assertThat(fonctionGroupePolitiques).hasSize(databaseSizeBeforeCreate + 1);
        FonctionGroupePolitique testFonctionGroupePolitique = fonctionGroupePolitiques.get(fonctionGroupePolitiques.size() - 1);
        assertThat(testFonctionGroupePolitique.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testFonctionGroupePolitique.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testFonctionGroupePolitique.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFonctionGroupePolitique.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllFonctionGroupePolitiques() throws Exception {
        // Initialize the database
        fonctionGroupePolitiqueRepository.saveAndFlush(fonctionGroupePolitique);

        // Get all the fonctionGroupePolitiques
        restFonctionGroupePolitiqueMockMvc.perform(get("/api/fonctionGroupePolitiques"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fonctionGroupePolitique.getId().intValue())))
                .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getFonctionGroupePolitique() throws Exception {
        // Initialize the database
        fonctionGroupePolitiqueRepository.saveAndFlush(fonctionGroupePolitique);

        // Get the fonctionGroupePolitique
        restFonctionGroupePolitiqueMockMvc.perform(get("/api/fonctionGroupePolitiques/{id}", fonctionGroupePolitique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fonctionGroupePolitique.getId().intValue()))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFonctionGroupePolitique() throws Exception {
        // Get the fonctionGroupePolitique
        restFonctionGroupePolitiqueMockMvc.perform(get("/api/fonctionGroupePolitiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFonctionGroupePolitique() throws Exception {
        // Initialize the database
        fonctionGroupePolitiqueRepository.saveAndFlush(fonctionGroupePolitique);

		int databaseSizeBeforeUpdate = fonctionGroupePolitiqueRepository.findAll().size();

        // Update the fonctionGroupePolitique
        fonctionGroupePolitique.setFonction(UPDATED_FONCTION);
        fonctionGroupePolitique.setDateDebut(UPDATED_DATE_DEBUT);
        fonctionGroupePolitique.setDateFin(UPDATED_DATE_FIN);
        fonctionGroupePolitique.setMotifFin(UPDATED_MOTIF_FIN);

        restFonctionGroupePolitiqueMockMvc.perform(put("/api/fonctionGroupePolitiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionGroupePolitique)))
                .andExpect(status().isOk());

        // Validate the FonctionGroupePolitique in the database
        List<FonctionGroupePolitique> fonctionGroupePolitiques = fonctionGroupePolitiqueRepository.findAll();
        assertThat(fonctionGroupePolitiques).hasSize(databaseSizeBeforeUpdate);
        FonctionGroupePolitique testFonctionGroupePolitique = fonctionGroupePolitiques.get(fonctionGroupePolitiques.size() - 1);
        assertThat(testFonctionGroupePolitique.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testFonctionGroupePolitique.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFonctionGroupePolitique.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFonctionGroupePolitique.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteFonctionGroupePolitique() throws Exception {
        // Initialize the database
        fonctionGroupePolitiqueRepository.saveAndFlush(fonctionGroupePolitique);

		int databaseSizeBeforeDelete = fonctionGroupePolitiqueRepository.findAll().size();

        // Get the fonctionGroupePolitique
        restFonctionGroupePolitiqueMockMvc.perform(delete("/api/fonctionGroupePolitiques/{id}", fonctionGroupePolitique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FonctionGroupePolitique> fonctionGroupePolitiques = fonctionGroupePolitiqueRepository.findAll();
        assertThat(fonctionGroupePolitiques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
