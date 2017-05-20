package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.AppartenanceCommissionPermanente;
import openassemblee.repository.AppartenanceCommissionPermanenteRepository;
import openassemblee.repository.search.AppartenanceCommissionPermanenteSearchRepository;
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
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AppartenanceCommissionPermanenteResource REST controller.
 *
 * @see AppartenanceCommissionPermanenteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AppartenanceCommissionPermanenteResourceIntTest {


    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    @Inject
    private AppartenanceCommissionPermanenteRepository appartenanceCommissionPermanenteRepository;

    @Inject
    private AppartenanceCommissionPermanenteSearchRepository appartenanceCommissionPermanenteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restAppartenanceCommissionPermanenteMockMvc;

    private AppartenanceCommissionPermanente appartenanceCommissionPermanente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppartenanceCommissionPermanenteResource appartenanceCommissionPermanenteResource = new AppartenanceCommissionPermanenteResource();
        ReflectionTestUtils.setField(appartenanceCommissionPermanenteResource, "appartenanceCommissionPermanenteRepository", appartenanceCommissionPermanenteRepository);
        ReflectionTestUtils.setField(appartenanceCommissionPermanenteResource, "appartenanceCommissionPermanenteSearchRepository", appartenanceCommissionPermanenteSearchRepository);
        ReflectionTestUtils.setField(appartenanceCommissionPermanenteResource, "auditTrailService", auditTrailService);
        this.restAppartenanceCommissionPermanenteMockMvc = MockMvcBuilders.standaloneSetup(appartenanceCommissionPermanenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        appartenanceCommissionPermanente = new AppartenanceCommissionPermanente();
        appartenanceCommissionPermanente.setDateDebut(DEFAULT_DATE_DEBUT);
        appartenanceCommissionPermanente.setDateFin(DEFAULT_DATE_FIN);
        appartenanceCommissionPermanente.setMotifFin(DEFAULT_MOTIF_FIN);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(new User("admin", "admin", Collections.emptyList()), "admin"));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Transactional
    public void createAppartenanceCommissionPermanente() throws Exception {
        int databaseSizeBeforeCreate = appartenanceCommissionPermanenteRepository.findAll().size();

        // Create the AppartenanceCommissionPermanente

        restAppartenanceCommissionPermanenteMockMvc.perform(post("/api/appartenanceCommissionPermanentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appartenanceCommissionPermanente)))
                .andExpect(status().isCreated());

        // Validate the AppartenanceCommissionPermanente in the database
        List<AppartenanceCommissionPermanente> appartenanceCommissionPermanentes = appartenanceCommissionPermanenteRepository.findAll();
        assertThat(appartenanceCommissionPermanentes).hasSize(databaseSizeBeforeCreate + 1);
        AppartenanceCommissionPermanente testAppartenanceCommissionPermanente = appartenanceCommissionPermanentes.get(appartenanceCommissionPermanentes.size() - 1);
        assertThat(testAppartenanceCommissionPermanente.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAppartenanceCommissionPermanente.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testAppartenanceCommissionPermanente.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllAppartenanceCommissionPermanentes() throws Exception {
        // Initialize the database
        appartenanceCommissionPermanenteRepository.saveAndFlush(appartenanceCommissionPermanente);

        // Get all the appartenanceCommissionPermanentes
        restAppartenanceCommissionPermanenteMockMvc.perform(get("/api/appartenanceCommissionPermanentes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(appartenanceCommissionPermanente.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getAppartenanceCommissionPermanente() throws Exception {
        // Initialize the database
        appartenanceCommissionPermanenteRepository.saveAndFlush(appartenanceCommissionPermanente);

        // Get the appartenanceCommissionPermanente
        restAppartenanceCommissionPermanenteMockMvc.perform(get("/api/appartenanceCommissionPermanentes/{id}", appartenanceCommissionPermanente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(appartenanceCommissionPermanente.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppartenanceCommissionPermanente() throws Exception {
        // Get the appartenanceCommissionPermanente
        restAppartenanceCommissionPermanenteMockMvc.perform(get("/api/appartenanceCommissionPermanentes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppartenanceCommissionPermanente() throws Exception {
        // Initialize the database
        appartenanceCommissionPermanenteRepository.saveAndFlush(appartenanceCommissionPermanente);

		int databaseSizeBeforeUpdate = appartenanceCommissionPermanenteRepository.findAll().size();

        // Update the appartenanceCommissionPermanente
        appartenanceCommissionPermanente.setDateDebut(UPDATED_DATE_DEBUT);
        appartenanceCommissionPermanente.setDateFin(UPDATED_DATE_FIN);
        appartenanceCommissionPermanente.setMotifFin(UPDATED_MOTIF_FIN);

        restAppartenanceCommissionPermanenteMockMvc.perform(put("/api/appartenanceCommissionPermanentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appartenanceCommissionPermanente)))
                .andExpect(status().isOk());

        // Validate the AppartenanceCommissionPermanente in the database
        List<AppartenanceCommissionPermanente> appartenanceCommissionPermanentes = appartenanceCommissionPermanenteRepository.findAll();
        assertThat(appartenanceCommissionPermanentes).hasSize(databaseSizeBeforeUpdate);
        AppartenanceCommissionPermanente testAppartenanceCommissionPermanente = appartenanceCommissionPermanentes.get(appartenanceCommissionPermanentes.size() - 1);
        assertThat(testAppartenanceCommissionPermanente.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAppartenanceCommissionPermanente.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testAppartenanceCommissionPermanente.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteAppartenanceCommissionPermanente() throws Exception {
        // Initialize the database
        appartenanceCommissionPermanenteRepository.saveAndFlush(appartenanceCommissionPermanente);

		int databaseSizeBeforeDelete = appartenanceCommissionPermanenteRepository.findAll().size();

        // Get the appartenanceCommissionPermanente
        restAppartenanceCommissionPermanenteMockMvc.perform(delete("/api/appartenanceCommissionPermanentes/{id}", appartenanceCommissionPermanente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AppartenanceCommissionPermanente> appartenanceCommissionPermanentes = appartenanceCommissionPermanenteRepository.findAll();
        assertThat(appartenanceCommissionPermanentes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
