package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.AppartenanceOrganisme;
import fr.cridf.babylone14166.repository.AppartenanceOrganismeRepository;
import fr.cridf.babylone14166.repository.search.AppartenanceOrganismeSearchRepository;

import fr.cridf.babylone14166.service.AuditTrailService;
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
 * Test class for the AppartenanceOrganismeResource REST controller.
 *
 * @see AppartenanceOrganismeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AppartenanceOrganismeResourceIntTest {

    private static final String DEFAULT_STATUT = "AAAAA";
    private static final String UPDATED_STATUT = "BBBBB";
    private static final String DEFAULT_CODE_RNE = "AAAAA";
    private static final String UPDATED_CODE_RNE = "BBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    private static final LocalDate DEFAULT_DATE_NOMINATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NOMINATION = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_REFERENCE = "AAAAA";
    private static final String UPDATED_REFERENCE = "BBBBB";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";
    private static final String DEFAULT_LIEN_PIECE = "AAAAA";
    private static final String UPDATED_LIEN_PIECE = "BBBBB";

    @Inject
    private AppartenanceOrganismeRepository appartenanceOrganismeRepository;

    @Inject
    private AppartenanceOrganismeSearchRepository appartenanceOrganismeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restAppartenanceOrganismeMockMvc;

    private AppartenanceOrganisme appartenanceOrganisme;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppartenanceOrganismeResource appartenanceOrganismeResource = new AppartenanceOrganismeResource();
        ReflectionTestUtils.setField(appartenanceOrganismeResource, "appartenanceOrganismeRepository", appartenanceOrganismeRepository);
        ReflectionTestUtils.setField(appartenanceOrganismeResource, "appartenanceOrganismeSearchRepository", appartenanceOrganismeSearchRepository);
        ReflectionTestUtils.setField(appartenanceOrganismeResource, "auditTrailService", auditTrailService);
        this.restAppartenanceOrganismeMockMvc = MockMvcBuilders.standaloneSetup(appartenanceOrganismeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        appartenanceOrganisme = new AppartenanceOrganisme();
        appartenanceOrganisme.setStatut(DEFAULT_STATUT);
        appartenanceOrganisme.setCodeRNE(DEFAULT_CODE_RNE);
        appartenanceOrganisme.setDateDebut(DEFAULT_DATE_DEBUT);
        appartenanceOrganisme.setDateFin(DEFAULT_DATE_FIN);
        appartenanceOrganisme.setMotifFin(DEFAULT_MOTIF_FIN);
        appartenanceOrganisme.setDateNomination(DEFAULT_DATE_NOMINATION);
        appartenanceOrganisme.setReference(DEFAULT_REFERENCE);
        appartenanceOrganisme.setType(DEFAULT_TYPE);
        appartenanceOrganisme.setLienPiece(DEFAULT_LIEN_PIECE);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(new User("admin", "admin", Collections.emptyList()), "admin"));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Transactional
    public void createAppartenanceOrganisme() throws Exception {
        int databaseSizeBeforeCreate = appartenanceOrganismeRepository.findAll().size();

        // Create the AppartenanceOrganisme

        restAppartenanceOrganismeMockMvc.perform(post("/api/appartenanceOrganismes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appartenanceOrganisme)))
                .andExpect(status().isCreated());

        // Validate the AppartenanceOrganisme in the database
        List<AppartenanceOrganisme> appartenanceOrganismes = appartenanceOrganismeRepository.findAll();
        assertThat(appartenanceOrganismes).hasSize(databaseSizeBeforeCreate + 1);
        AppartenanceOrganisme testAppartenanceOrganisme = appartenanceOrganismes.get(appartenanceOrganismes.size() - 1);
        assertThat(testAppartenanceOrganisme.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testAppartenanceOrganisme.getCodeRNE()).isEqualTo(DEFAULT_CODE_RNE);
        assertThat(testAppartenanceOrganisme.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAppartenanceOrganisme.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testAppartenanceOrganisme.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
        assertThat(testAppartenanceOrganisme.getDateNomination()).isEqualTo(DEFAULT_DATE_NOMINATION);
        assertThat(testAppartenanceOrganisme.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testAppartenanceOrganisme.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAppartenanceOrganisme.getLienPiece()).isEqualTo(DEFAULT_LIEN_PIECE);
    }

    @Test
    @Transactional
    public void getAllAppartenanceOrganismes() throws Exception {
        // Initialize the database
        appartenanceOrganismeRepository.saveAndFlush(appartenanceOrganisme);

        // Get all the appartenanceOrganismes
        restAppartenanceOrganismeMockMvc.perform(get("/api/appartenanceOrganismes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(appartenanceOrganisme.getId().intValue())))
                .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
                .andExpect(jsonPath("$.[*].codeRNE").value(hasItem(DEFAULT_CODE_RNE.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())))
                .andExpect(jsonPath("$.[*].dateNomination").value(hasItem(DEFAULT_DATE_NOMINATION.toString())))
                .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].lienPiece").value(hasItem(DEFAULT_LIEN_PIECE.toString())));
    }

    @Test
    @Transactional
    public void getAppartenanceOrganisme() throws Exception {
        // Initialize the database
        appartenanceOrganismeRepository.saveAndFlush(appartenanceOrganisme);

        // Get the appartenanceOrganisme
        restAppartenanceOrganismeMockMvc.perform(get("/api/appartenanceOrganismes/{id}", appartenanceOrganisme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(appartenanceOrganisme.getId().intValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.codeRNE").value(DEFAULT_CODE_RNE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()))
            .andExpect(jsonPath("$.dateNomination").value(DEFAULT_DATE_NOMINATION.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.lienPiece").value(DEFAULT_LIEN_PIECE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppartenanceOrganisme() throws Exception {
        // Get the appartenanceOrganisme
        restAppartenanceOrganismeMockMvc.perform(get("/api/appartenanceOrganismes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppartenanceOrganisme() throws Exception {
        // Initialize the database
        appartenanceOrganismeRepository.saveAndFlush(appartenanceOrganisme);

		int databaseSizeBeforeUpdate = appartenanceOrganismeRepository.findAll().size();

        // Update the appartenanceOrganisme
        appartenanceOrganisme.setStatut(UPDATED_STATUT);
        appartenanceOrganisme.setCodeRNE(UPDATED_CODE_RNE);
        appartenanceOrganisme.setDateDebut(UPDATED_DATE_DEBUT);
        appartenanceOrganisme.setDateFin(UPDATED_DATE_FIN);
        appartenanceOrganisme.setMotifFin(UPDATED_MOTIF_FIN);
        appartenanceOrganisme.setDateNomination(UPDATED_DATE_NOMINATION);
        appartenanceOrganisme.setReference(UPDATED_REFERENCE);
        appartenanceOrganisme.setType(UPDATED_TYPE);
        appartenanceOrganisme.setLienPiece(UPDATED_LIEN_PIECE);

        restAppartenanceOrganismeMockMvc.perform(put("/api/appartenanceOrganismes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appartenanceOrganisme)))
                .andExpect(status().isOk());

        // Validate the AppartenanceOrganisme in the database
        List<AppartenanceOrganisme> appartenanceOrganismes = appartenanceOrganismeRepository.findAll();
        assertThat(appartenanceOrganismes).hasSize(databaseSizeBeforeUpdate);
        AppartenanceOrganisme testAppartenanceOrganisme = appartenanceOrganismes.get(appartenanceOrganismes.size() - 1);
        assertThat(testAppartenanceOrganisme.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testAppartenanceOrganisme.getCodeRNE()).isEqualTo(UPDATED_CODE_RNE);
        assertThat(testAppartenanceOrganisme.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAppartenanceOrganisme.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testAppartenanceOrganisme.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
        assertThat(testAppartenanceOrganisme.getDateNomination()).isEqualTo(UPDATED_DATE_NOMINATION);
        assertThat(testAppartenanceOrganisme.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testAppartenanceOrganisme.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAppartenanceOrganisme.getLienPiece()).isEqualTo(UPDATED_LIEN_PIECE);
    }

    @Test
    @Transactional
    public void deleteAppartenanceOrganisme() throws Exception {
        // Initialize the database
        appartenanceOrganismeRepository.saveAndFlush(appartenanceOrganisme);

		int databaseSizeBeforeDelete = appartenanceOrganismeRepository.findAll().size();

        // Get the appartenanceOrganisme
        restAppartenanceOrganismeMockMvc.perform(delete("/api/appartenanceOrganismes/{id}", appartenanceOrganisme.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AppartenanceOrganisme> appartenanceOrganismes = appartenanceOrganismeRepository.findAll();
        assertThat(appartenanceOrganismes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
