package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.ReunionCao;
import openassemblee.repository.ReunionCaoRepository;
import openassemblee.repository.search.ReunionCaoSearchRepository;

import openassemblee.service.AuditTrailService;
import openassemblee.service.ReunionCaoService;
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
 * Test class for the ReunionCaoResource REST controller.
 *
 * @see ReunionCaoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ReunionCaoResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_HEURE_DEBUT = "AAAAA";
    private static final String UPDATED_HEURE_DEBUT = "BBBBB";
    private static final String DEFAULT_HEURE_FIN = "AAAAA";
    private static final String UPDATED_HEURE_FIN = "BBBBB";

    @Inject
    private ReunionCaoRepository reunionCaoRepository;

    @Inject
    private ReunionCaoSearchRepository reunionCaoSearchRepository;

    @Inject
    private ReunionCaoService reunionCaoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restReunionCaoMockMvc;

    private ReunionCao reunionCao;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReunionCaoResource reunionCaoResource = new ReunionCaoResource();
        ReflectionTestUtils.setField(reunionCaoResource, "reunionCaoRepository", reunionCaoRepository);
        ReflectionTestUtils.setField(reunionCaoResource, "reunionCaoSearchRepository", reunionCaoSearchRepository);
        ReflectionTestUtils.setField(reunionCaoResource, "reunionCaoService", reunionCaoService);
        ReflectionTestUtils.setField(reunionCaoResource, "auditTrailService", auditTrailService);
        this.restReunionCaoMockMvc = MockMvcBuilders.standaloneSetup(reunionCaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        reunionCao = new ReunionCao();
        reunionCao.setLibelle(DEFAULT_LIBELLE);
        reunionCao.setDate(DEFAULT_DATE);
        reunionCao.setHeureDebut(DEFAULT_HEURE_DEBUT);
        reunionCao.setHeureFin(DEFAULT_HEURE_FIN);
    }

    @Test
    @Transactional
    public void createReunionCao() throws Exception {
        int databaseSizeBeforeCreate = reunionCaoRepository.findAll().size();

        // Create the ReunionCao

        restReunionCaoMockMvc.perform(post("/api/reunionCaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reunionCao)))
                .andExpect(status().isCreated());

        // Validate the ReunionCao in the database
        List<ReunionCao> reunionCaos = reunionCaoRepository.findAll();
        assertThat(reunionCaos).hasSize(databaseSizeBeforeCreate + 1);
        ReunionCao testReunionCao = reunionCaos.get(reunionCaos.size() - 1);
        assertThat(testReunionCao.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testReunionCao.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testReunionCao.getHeureDebut()).isEqualTo(DEFAULT_HEURE_DEBUT);
        assertThat(testReunionCao.getHeureFin()).isEqualTo(DEFAULT_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllReunionCaos() throws Exception {
        // Initialize the database
        reunionCaoRepository.saveAndFlush(reunionCao);

        // Get all the reunionCaos
        restReunionCaoMockMvc.perform(get("/api/reunionCaos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reunionCao.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN.toString())));
    }

    @Test
    @Transactional
    public void getReunionCao() throws Exception {
        // Initialize the database
        reunionCaoRepository.saveAndFlush(reunionCao);

        // Get the reunionCao
        restReunionCaoMockMvc.perform(get("/api/reunionCaos/{id}", reunionCao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(reunionCao.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.heureDebut").value(DEFAULT_HEURE_DEBUT.toString()))
            .andExpect(jsonPath("$.heureFin").value(DEFAULT_HEURE_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReunionCao() throws Exception {
        // Get the reunionCao
        restReunionCaoMockMvc.perform(get("/api/reunionCaos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReunionCao() throws Exception {
        // Initialize the database
        reunionCaoRepository.saveAndFlush(reunionCao);

		int databaseSizeBeforeUpdate = reunionCaoRepository.findAll().size();

        // Update the reunionCao
        reunionCao.setLibelle(UPDATED_LIBELLE);
        reunionCao.setDate(UPDATED_DATE);
        reunionCao.setHeureDebut(UPDATED_HEURE_DEBUT);
        reunionCao.setHeureFin(UPDATED_HEURE_FIN);

        restReunionCaoMockMvc.perform(put("/api/reunionCaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reunionCao)))
                .andExpect(status().isOk());

        // Validate the ReunionCao in the database
        List<ReunionCao> reunionCaos = reunionCaoRepository.findAll();
        assertThat(reunionCaos).hasSize(databaseSizeBeforeUpdate);
        ReunionCao testReunionCao = reunionCaos.get(reunionCaos.size() - 1);
        assertThat(testReunionCao.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testReunionCao.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReunionCao.getHeureDebut()).isEqualTo(UPDATED_HEURE_DEBUT);
        assertThat(testReunionCao.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void deleteReunionCao() throws Exception {
        // Initialize the database
        reunionCaoRepository.saveAndFlush(reunionCao);

		int databaseSizeBeforeDelete = reunionCaoRepository.findAll().size();

        // Get the reunionCao
        restReunionCaoMockMvc.perform(delete("/api/reunionCaos/{id}", reunionCao.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ReunionCao> reunionCaos = reunionCaoRepository.findAll();
        assertThat(reunionCaos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
