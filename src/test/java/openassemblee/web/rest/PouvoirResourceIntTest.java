package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.Pouvoir;
import openassemblee.repository.PouvoirRepository;

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
 * Test class for the PouvoirResource REST controller.
 *
 * @see PouvoirResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PouvoirResourceIntTest {


    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_HEURE_DEBUT = "AAAAA";
    private static final String UPDATED_HEURE_DEBUT = "BBBBB";

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_HEURE_FIN = "AAAAA";
    private static final String UPDATED_HEURE_FIN = "BBBBB";

    @Inject
    private PouvoirRepository pouvoirRepository;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPouvoirMockMvc;

    private Pouvoir pouvoir;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PouvoirResource pouvoirResource = new PouvoirResource();
        ReflectionTestUtils.setField(pouvoirResource, "pouvoirRepository", pouvoirRepository);
        ReflectionTestUtils.setField(pouvoirResource, "auditTrailService", auditTrailService);
        this.restPouvoirMockMvc = MockMvcBuilders.standaloneSetup(pouvoirResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pouvoir = new Pouvoir();
        pouvoir.setDateDebut(DEFAULT_DATE_DEBUT);
        pouvoir.setHeureDebut(DEFAULT_HEURE_DEBUT);
        pouvoir.setDateFin(DEFAULT_DATE_FIN);
        pouvoir.setHeureFin(DEFAULT_HEURE_FIN);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(new User("admin", "admin", Collections.emptyList()), "admin"));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Transactional
    public void createPouvoir() throws Exception {
        int databaseSizeBeforeCreate = pouvoirRepository.findAll().size();

        // Create the Pouvoir

        restPouvoirMockMvc.perform(post("/api/pouvoirs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pouvoir)))
                .andExpect(status().isCreated());

        // Validate the Pouvoir in the database
        List<Pouvoir> pouvoirs = pouvoirRepository.findAll();
        assertThat(pouvoirs).hasSize(databaseSizeBeforeCreate + 1);
        Pouvoir testPouvoir = pouvoirs.get(pouvoirs.size() - 1);
        assertThat(testPouvoir.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testPouvoir.getHeureDebut()).isEqualTo(DEFAULT_HEURE_DEBUT);
        assertThat(testPouvoir.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testPouvoir.getHeureFin()).isEqualTo(DEFAULT_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllPouvoirs() throws Exception {
        // Initialize the database
        pouvoirRepository.saveAndFlush(pouvoir);

        // Get all the pouvoirs
        restPouvoirMockMvc.perform(get("/api/pouvoirs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pouvoir.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN.toString())));
    }

    @Test
    @Transactional
    public void getPouvoir() throws Exception {
        // Initialize the database
        pouvoirRepository.saveAndFlush(pouvoir);

        // Get the pouvoir
        restPouvoirMockMvc.perform(get("/api/pouvoirs/{id}", pouvoir.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pouvoir.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.heureDebut").value(DEFAULT_HEURE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.heureFin").value(DEFAULT_HEURE_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPouvoir() throws Exception {
        // Get the pouvoir
        restPouvoirMockMvc.perform(get("/api/pouvoirs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePouvoir() throws Exception {
        // Initialize the database
        pouvoirRepository.saveAndFlush(pouvoir);

		int databaseSizeBeforeUpdate = pouvoirRepository.findAll().size();

        // Update the pouvoir
        pouvoir.setDateDebut(UPDATED_DATE_DEBUT);
        pouvoir.setHeureDebut(UPDATED_HEURE_DEBUT);
        pouvoir.setDateFin(UPDATED_DATE_FIN);
        pouvoir.setHeureFin(UPDATED_HEURE_FIN);

        restPouvoirMockMvc.perform(put("/api/pouvoirs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pouvoir)))
                .andExpect(status().isOk());

        // Validate the Pouvoir in the database
        List<Pouvoir> pouvoirs = pouvoirRepository.findAll();
        assertThat(pouvoirs).hasSize(databaseSizeBeforeUpdate);
        Pouvoir testPouvoir = pouvoirs.get(pouvoirs.size() - 1);
        assertThat(testPouvoir.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testPouvoir.getHeureDebut()).isEqualTo(UPDATED_HEURE_DEBUT);
        assertThat(testPouvoir.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testPouvoir.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void deletePouvoir() throws Exception {
        // Initialize the database
        pouvoirRepository.saveAndFlush(pouvoir);

		int databaseSizeBeforeDelete = pouvoirRepository.findAll().size();

        // Get the pouvoir
        restPouvoirMockMvc.perform(delete("/api/pouvoirs/{id}", pouvoir.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pouvoir> pouvoirs = pouvoirRepository.findAll();
        assertThat(pouvoirs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
