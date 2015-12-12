package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.AppartenanceGroupePolitique;
import fr.cridf.babylone14166.repository.AppartenanceGroupePolitiqueRepository;
import fr.cridf.babylone14166.repository.search.AppartenanceGroupePolitiqueSearchRepository;

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
 * Test class for the AppartenanceGroupePolitiqueResource REST controller.
 *
 * @see AppartenanceGroupePolitiqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AppartenanceGroupePolitiqueResourceIntTest {


    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    @Inject
    private AppartenanceGroupePolitiqueRepository appartenanceGroupePolitiqueRepository;

    @Inject
    private AppartenanceGroupePolitiqueSearchRepository appartenanceGroupePolitiqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAppartenanceGroupePolitiqueMockMvc;

    private AppartenanceGroupePolitique appartenanceGroupePolitique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppartenanceGroupePolitiqueResource appartenanceGroupePolitiqueResource = new AppartenanceGroupePolitiqueResource();
        ReflectionTestUtils.setField(appartenanceGroupePolitiqueResource, "appartenanceGroupePolitiqueRepository", appartenanceGroupePolitiqueRepository);
        ReflectionTestUtils.setField(appartenanceGroupePolitiqueResource, "appartenanceGroupePolitiqueSearchRepository", appartenanceGroupePolitiqueSearchRepository);
        this.restAppartenanceGroupePolitiqueMockMvc = MockMvcBuilders.standaloneSetup(appartenanceGroupePolitiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        appartenanceGroupePolitique = new AppartenanceGroupePolitique();
        appartenanceGroupePolitique.setDateDebut(DEFAULT_DATE_DEBUT);
        appartenanceGroupePolitique.setDateFin(DEFAULT_DATE_FIN);
        appartenanceGroupePolitique.setMotifFin(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void createAppartenanceGroupePolitique() throws Exception {
        int databaseSizeBeforeCreate = appartenanceGroupePolitiqueRepository.findAll().size();

        // Create the AppartenanceGroupePolitique

        restAppartenanceGroupePolitiqueMockMvc.perform(post("/api/appartenanceGroupePolitiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appartenanceGroupePolitique)))
                .andExpect(status().isCreated());

        // Validate the AppartenanceGroupePolitique in the database
        List<AppartenanceGroupePolitique> appartenanceGroupePolitiques = appartenanceGroupePolitiqueRepository.findAll();
        assertThat(appartenanceGroupePolitiques).hasSize(databaseSizeBeforeCreate + 1);
        AppartenanceGroupePolitique testAppartenanceGroupePolitique = appartenanceGroupePolitiques.get(appartenanceGroupePolitiques.size() - 1);
        assertThat(testAppartenanceGroupePolitique.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAppartenanceGroupePolitique.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testAppartenanceGroupePolitique.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllAppartenanceGroupePolitiques() throws Exception {
        // Initialize the database
        appartenanceGroupePolitiqueRepository.saveAndFlush(appartenanceGroupePolitique);

        // Get all the appartenanceGroupePolitiques
        restAppartenanceGroupePolitiqueMockMvc.perform(get("/api/appartenanceGroupePolitiques"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(appartenanceGroupePolitique.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getAppartenanceGroupePolitique() throws Exception {
        // Initialize the database
        appartenanceGroupePolitiqueRepository.saveAndFlush(appartenanceGroupePolitique);

        // Get the appartenanceGroupePolitique
        restAppartenanceGroupePolitiqueMockMvc.perform(get("/api/appartenanceGroupePolitiques/{id}", appartenanceGroupePolitique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(appartenanceGroupePolitique.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppartenanceGroupePolitique() throws Exception {
        // Get the appartenanceGroupePolitique
        restAppartenanceGroupePolitiqueMockMvc.perform(get("/api/appartenanceGroupePolitiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppartenanceGroupePolitique() throws Exception {
        // Initialize the database
        appartenanceGroupePolitiqueRepository.saveAndFlush(appartenanceGroupePolitique);

		int databaseSizeBeforeUpdate = appartenanceGroupePolitiqueRepository.findAll().size();

        // Update the appartenanceGroupePolitique
        appartenanceGroupePolitique.setDateDebut(UPDATED_DATE_DEBUT);
        appartenanceGroupePolitique.setDateFin(UPDATED_DATE_FIN);
        appartenanceGroupePolitique.setMotifFin(UPDATED_MOTIF_FIN);

        restAppartenanceGroupePolitiqueMockMvc.perform(put("/api/appartenanceGroupePolitiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appartenanceGroupePolitique)))
                .andExpect(status().isOk());

        // Validate the AppartenanceGroupePolitique in the database
        List<AppartenanceGroupePolitique> appartenanceGroupePolitiques = appartenanceGroupePolitiqueRepository.findAll();
        assertThat(appartenanceGroupePolitiques).hasSize(databaseSizeBeforeUpdate);
        AppartenanceGroupePolitique testAppartenanceGroupePolitique = appartenanceGroupePolitiques.get(appartenanceGroupePolitiques.size() - 1);
        assertThat(testAppartenanceGroupePolitique.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAppartenanceGroupePolitique.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testAppartenanceGroupePolitique.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteAppartenanceGroupePolitique() throws Exception {
        // Initialize the database
        appartenanceGroupePolitiqueRepository.saveAndFlush(appartenanceGroupePolitique);

		int databaseSizeBeforeDelete = appartenanceGroupePolitiqueRepository.findAll().size();

        // Get the appartenanceGroupePolitique
        restAppartenanceGroupePolitiqueMockMvc.perform(delete("/api/appartenanceGroupePolitiques/{id}", appartenanceGroupePolitique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AppartenanceGroupePolitique> appartenanceGroupePolitiques = appartenanceGroupePolitiqueRepository.findAll();
        assertThat(appartenanceGroupePolitiques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
