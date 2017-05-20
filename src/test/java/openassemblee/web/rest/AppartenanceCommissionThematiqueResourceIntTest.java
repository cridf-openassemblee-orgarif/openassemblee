package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.AppartenanceCommissionThematique;
import openassemblee.repository.AppartenanceCommissionThematiqueRepository;
import openassemblee.repository.search.AppartenanceCommissionThematiqueSearchRepository;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AppartenanceCommissionThematiqueResource REST controller.
 *
 * @see AppartenanceCommissionThematiqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AppartenanceCommissionThematiqueResourceIntTest {


    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    @Inject
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;

    @Inject
    private AppartenanceCommissionThematiqueSearchRepository appartenanceCommissionThematiqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restAppartenanceCommissionThematiqueMockMvc;

    private AppartenanceCommissionThematique appartenanceCommissionThematique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppartenanceCommissionThematiqueResource appartenanceCommissionThematiqueResource = new AppartenanceCommissionThematiqueResource();
        ReflectionTestUtils.setField(appartenanceCommissionThematiqueResource, "appartenanceCommissionThematiqueRepository", appartenanceCommissionThematiqueRepository);
        ReflectionTestUtils.setField(appartenanceCommissionThematiqueResource, "appartenanceCommissionThematiqueSearchRepository", appartenanceCommissionThematiqueSearchRepository);
        ReflectionTestUtils.setField(appartenanceCommissionThematiqueResource, "auditTrailService", auditTrailService);
        this.restAppartenanceCommissionThematiqueMockMvc = MockMvcBuilders.standaloneSetup(appartenanceCommissionThematiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        appartenanceCommissionThematique = new AppartenanceCommissionThematique();
        appartenanceCommissionThematique.setDateDebut(DEFAULT_DATE_DEBUT);
        appartenanceCommissionThematique.setDateFin(DEFAULT_DATE_FIN);
        appartenanceCommissionThematique.setMotifFin(DEFAULT_MOTIF_FIN);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(new User("admin", "admin", Collections.emptyList()), "admin"));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Transactional
    public void createAppartenanceCommissionThematique() throws Exception {
        int databaseSizeBeforeCreate = appartenanceCommissionThematiqueRepository.findAll().size();

        // Create the AppartenanceCommissionThematique

        restAppartenanceCommissionThematiqueMockMvc.perform(post("/api/appartenanceCommissionThematiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appartenanceCommissionThematique)))
                .andExpect(status().isCreated());

        // Validate the AppartenanceCommissionThematique in the database
        List<AppartenanceCommissionThematique> appartenanceCommissionThematiques = appartenanceCommissionThematiqueRepository.findAll();
        assertThat(appartenanceCommissionThematiques).hasSize(databaseSizeBeforeCreate + 1);
        AppartenanceCommissionThematique testAppartenanceCommissionThematique = appartenanceCommissionThematiques.get(appartenanceCommissionThematiques.size() - 1);
        assertThat(testAppartenanceCommissionThematique.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAppartenanceCommissionThematique.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testAppartenanceCommissionThematique.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllAppartenanceCommissionThematiques() throws Exception {
        // Initialize the database
        appartenanceCommissionThematiqueRepository.saveAndFlush(appartenanceCommissionThematique);

        // Get all the appartenanceCommissionThematiques
        restAppartenanceCommissionThematiqueMockMvc.perform(get("/api/appartenanceCommissionThematiques"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(appartenanceCommissionThematique.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getAppartenanceCommissionThematique() throws Exception {
        // Initialize the database
        appartenanceCommissionThematiqueRepository.saveAndFlush(appartenanceCommissionThematique);

        // Get the appartenanceCommissionThematique
        restAppartenanceCommissionThematiqueMockMvc.perform(get("/api/appartenanceCommissionThematiques/{id}", appartenanceCommissionThematique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(appartenanceCommissionThematique.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppartenanceCommissionThematique() throws Exception {
        // Get the appartenanceCommissionThematique
        restAppartenanceCommissionThematiqueMockMvc.perform(get("/api/appartenanceCommissionThematiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppartenanceCommissionThematique() throws Exception {
        // Initialize the database
        appartenanceCommissionThematiqueRepository.saveAndFlush(appartenanceCommissionThematique);

		int databaseSizeBeforeUpdate = appartenanceCommissionThematiqueRepository.findAll().size();

        // Update the appartenanceCommissionThematique
        appartenanceCommissionThematique.setDateDebut(UPDATED_DATE_DEBUT);
        appartenanceCommissionThematique.setDateFin(UPDATED_DATE_FIN);
        appartenanceCommissionThematique.setMotifFin(UPDATED_MOTIF_FIN);

        restAppartenanceCommissionThematiqueMockMvc.perform(put("/api/appartenanceCommissionThematiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appartenanceCommissionThematique)))
                .andExpect(status().isOk());

        // Validate the AppartenanceCommissionThematique in the database
        List<AppartenanceCommissionThematique> appartenanceCommissionThematiques = appartenanceCommissionThematiqueRepository.findAll();
        assertThat(appartenanceCommissionThematiques).hasSize(databaseSizeBeforeUpdate);
        AppartenanceCommissionThematique testAppartenanceCommissionThematique = appartenanceCommissionThematiques.get(appartenanceCommissionThematiques.size() - 1);
        assertThat(testAppartenanceCommissionThematique.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAppartenanceCommissionThematique.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testAppartenanceCommissionThematique.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteAppartenanceCommissionThematique() throws Exception {
        // Initialize the database
        appartenanceCommissionThematiqueRepository.saveAndFlush(appartenanceCommissionThematique);

		int databaseSizeBeforeDelete = appartenanceCommissionThematiqueRepository.findAll().size();

        // Get the appartenanceCommissionThematique
        restAppartenanceCommissionThematiqueMockMvc.perform(delete("/api/appartenanceCommissionThematiques/{id}", appartenanceCommissionThematique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AppartenanceCommissionThematique> appartenanceCommissionThematiques = appartenanceCommissionThematiqueRepository.findAll();
        assertThat(appartenanceCommissionThematiques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
