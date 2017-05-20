package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.AutreMandat;
import openassemblee.repository.AutreMandatRepository;
import openassemblee.repository.search.AutreMandatSearchRepository;

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
 * Test class for the AutreMandatResource REST controller.
 *
 * @see AutreMandatResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AutreMandatResourceIntTest {

    private static final String DEFAULT_COLLECTIVITE_OU_ORGANISME = "AAAAA";
    private static final String UPDATED_COLLECTIVITE_OU_ORGANISME = "BBBBB";
    private static final String DEFAULT_FONCTION = "AAAAA";
    private static final String UPDATED_FONCTION = "BBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    @Inject
    private AutreMandatRepository autreMandatRepository;

    @Inject
    private AutreMandatSearchRepository autreMandatSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restAutreMandatMockMvc;

    private AutreMandat autreMandat;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AutreMandatResource autreMandatResource = new AutreMandatResource();
        ReflectionTestUtils.setField(autreMandatResource, "autreMandatRepository", autreMandatRepository);
        ReflectionTestUtils.setField(autreMandatResource, "autreMandatSearchRepository", autreMandatSearchRepository);
        ReflectionTestUtils.setField(autreMandatResource, "auditTrailService", auditTrailService);
        this.restAutreMandatMockMvc = MockMvcBuilders.standaloneSetup(autreMandatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        autreMandat = new AutreMandat();
        autreMandat.setCollectiviteOuOrganisme(DEFAULT_COLLECTIVITE_OU_ORGANISME);
        autreMandat.setFonction(DEFAULT_FONCTION);
        autreMandat.setDateDebut(DEFAULT_DATE_DEBUT);
        autreMandat.setDateFin(DEFAULT_DATE_FIN);
        autreMandat.setMotifFin(DEFAULT_MOTIF_FIN);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(new User("admin", "admin", Collections.emptyList()), "admin"));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Transactional
    public void createAutreMandat() throws Exception {
        int databaseSizeBeforeCreate = autreMandatRepository.findAll().size();

        // Create the AutreMandat

        restAutreMandatMockMvc.perform(post("/api/autreMandats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(autreMandat)))
                .andExpect(status().isCreated());

        // Validate the AutreMandat in the database
        List<AutreMandat> autreMandats = autreMandatRepository.findAll();
        assertThat(autreMandats).hasSize(databaseSizeBeforeCreate + 1);
        AutreMandat testAutreMandat = autreMandats.get(autreMandats.size() - 1);
        assertThat(testAutreMandat.getCollectiviteOuOrganisme()).isEqualTo(DEFAULT_COLLECTIVITE_OU_ORGANISME);
        assertThat(testAutreMandat.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testAutreMandat.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAutreMandat.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testAutreMandat.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllAutreMandats() throws Exception {
        // Initialize the database
        autreMandatRepository.saveAndFlush(autreMandat);

        // Get all the autreMandats
        restAutreMandatMockMvc.perform(get("/api/autreMandats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(autreMandat.getId().intValue())))
                .andExpect(jsonPath("$.[*].collectiviteOuOrganisme").value(hasItem(DEFAULT_COLLECTIVITE_OU_ORGANISME.toString())))
                .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getAutreMandat() throws Exception {
        // Initialize the database
        autreMandatRepository.saveAndFlush(autreMandat);

        // Get the autreMandat
        restAutreMandatMockMvc.perform(get("/api/autreMandats/{id}", autreMandat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(autreMandat.getId().intValue()))
            .andExpect(jsonPath("$.collectiviteOuOrganisme").value(DEFAULT_COLLECTIVITE_OU_ORGANISME.toString()))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAutreMandat() throws Exception {
        // Get the autreMandat
        restAutreMandatMockMvc.perform(get("/api/autreMandats/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutreMandat() throws Exception {
        // Initialize the database
        autreMandatRepository.saveAndFlush(autreMandat);

		int databaseSizeBeforeUpdate = autreMandatRepository.findAll().size();

        // Update the autreMandat
        autreMandat.setCollectiviteOuOrganisme(UPDATED_COLLECTIVITE_OU_ORGANISME);
        autreMandat.setFonction(UPDATED_FONCTION);
        autreMandat.setDateDebut(UPDATED_DATE_DEBUT);
        autreMandat.setDateFin(UPDATED_DATE_FIN);
        autreMandat.setMotifFin(UPDATED_MOTIF_FIN);

        restAutreMandatMockMvc.perform(put("/api/autreMandats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(autreMandat)))
                .andExpect(status().isOk());

        // Validate the AutreMandat in the database
        List<AutreMandat> autreMandats = autreMandatRepository.findAll();
        assertThat(autreMandats).hasSize(databaseSizeBeforeUpdate);
        AutreMandat testAutreMandat = autreMandats.get(autreMandats.size() - 1);
        assertThat(testAutreMandat.getCollectiviteOuOrganisme()).isEqualTo(UPDATED_COLLECTIVITE_OU_ORGANISME);
        assertThat(testAutreMandat.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testAutreMandat.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAutreMandat.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testAutreMandat.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteAutreMandat() throws Exception {
        // Initialize the database
        autreMandatRepository.saveAndFlush(autreMandat);

		int databaseSizeBeforeDelete = autreMandatRepository.findAll().size();

        // Get the autreMandat
        restAutreMandatMockMvc.perform(delete("/api/autreMandats/{id}", autreMandat.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AutreMandat> autreMandats = autreMandatRepository.findAll();
        assertThat(autreMandats).hasSize(databaseSizeBeforeDelete - 1);
    }
}
