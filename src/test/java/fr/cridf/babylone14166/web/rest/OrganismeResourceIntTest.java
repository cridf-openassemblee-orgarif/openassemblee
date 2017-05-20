package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.Organisme;
import fr.cridf.babylone14166.repository.OrganismeRepository;
import fr.cridf.babylone14166.repository.search.OrganismeSearchRepository;

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
 * Test class for the OrganismeResource REST controller.
 *
 * @see OrganismeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrganismeResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_CODE_RNE = "AAAAA";
    private static final String UPDATED_CODE_RNE = "BBBBB";
    private static final String DEFAULT_SIGLE = "AAAAA";
    private static final String UPDATED_SIGLE = "BBBBB";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";
    private static final String DEFAULT_SECTEUR = "AAAAA";
    private static final String UPDATED_SECTEUR = "BBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_FIN = "AAAAA";
    private static final String UPDATED_MOTIF_FIN = "BBBBB";

    @Inject
    private OrganismeRepository organismeRepository;

    @Inject
    private OrganismeSearchRepository organismeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restOrganismeMockMvc;

    private Organisme organisme;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrganismeResource organismeResource = new OrganismeResource();
        ReflectionTestUtils.setField(organismeResource, "organismeRepository", organismeRepository);
        ReflectionTestUtils.setField(organismeResource, "organismeSearchRepository", organismeSearchRepository);
        ReflectionTestUtils.setField(organismeResource, "auditTrailService", auditTrailService);
        this.restOrganismeMockMvc = MockMvcBuilders.standaloneSetup(organismeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        organisme = new Organisme();
        organisme.setNom(DEFAULT_NOM);
        organisme.setCodeRNE(DEFAULT_CODE_RNE);
        organisme.setSigle(DEFAULT_SIGLE);
        organisme.setType(DEFAULT_TYPE);
        organisme.setSecteur(DEFAULT_SECTEUR);
        organisme.setDateDebut(DEFAULT_DATE_DEBUT);
        organisme.setDateFin(DEFAULT_DATE_FIN);
        organisme.setMotifFin(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void createOrganisme() throws Exception {
        int databaseSizeBeforeCreate = organismeRepository.findAll().size();

        // Create the Organisme

        restOrganismeMockMvc.perform(post("/api/organismes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organisme)))
                .andExpect(status().isCreated());

        // Validate the Organisme in the database
        List<Organisme> organismes = organismeRepository.findAll();
        assertThat(organismes).hasSize(databaseSizeBeforeCreate + 1);
        Organisme testOrganisme = organismes.get(organismes.size() - 1);
        assertThat(testOrganisme.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testOrganisme.getCodeRNE()).isEqualTo(DEFAULT_CODE_RNE);
        assertThat(testOrganisme.getSigle()).isEqualTo(DEFAULT_SIGLE);
        assertThat(testOrganisme.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOrganisme.getSecteur()).isEqualTo(DEFAULT_SECTEUR);
        assertThat(testOrganisme.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testOrganisme.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testOrganisme.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllOrganismes() throws Exception {
        // Initialize the database
        organismeRepository.saveAndFlush(organisme);

        // Get all the organismes
        restOrganismeMockMvc.perform(get("/api/organismes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(organisme.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].codeRNE").value(hasItem(DEFAULT_CODE_RNE.toString())))
                .andExpect(jsonPath("$.[*].sigle").value(hasItem(DEFAULT_SIGLE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].secteur").value(hasItem(DEFAULT_SECTEUR.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getOrganisme() throws Exception {
        // Initialize the database
        organismeRepository.saveAndFlush(organisme);

        // Get the organisme
        restOrganismeMockMvc.perform(get("/api/organismes/{id}", organisme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(organisme.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.codeRNE").value(DEFAULT_CODE_RNE.toString()))
            .andExpect(jsonPath("$.sigle").value(DEFAULT_SIGLE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.secteur").value(DEFAULT_SECTEUR.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganisme() throws Exception {
        // Get the organisme
        restOrganismeMockMvc.perform(get("/api/organismes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganisme() throws Exception {
        // Initialize the database
        organismeRepository.saveAndFlush(organisme);

		int databaseSizeBeforeUpdate = organismeRepository.findAll().size();

        // Update the organisme
        organisme.setNom(UPDATED_NOM);
        organisme.setCodeRNE(UPDATED_CODE_RNE);
        organisme.setSigle(UPDATED_SIGLE);
        organisme.setType(UPDATED_TYPE);
        organisme.setSecteur(UPDATED_SECTEUR);
        organisme.setDateDebut(UPDATED_DATE_DEBUT);
        organisme.setDateFin(UPDATED_DATE_FIN);
        organisme.setMotifFin(UPDATED_MOTIF_FIN);

        restOrganismeMockMvc.perform(put("/api/organismes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organisme)))
                .andExpect(status().isOk());

        // Validate the Organisme in the database
        List<Organisme> organismes = organismeRepository.findAll();
        assertThat(organismes).hasSize(databaseSizeBeforeUpdate);
        Organisme testOrganisme = organismes.get(organismes.size() - 1);
        assertThat(testOrganisme.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testOrganisme.getCodeRNE()).isEqualTo(UPDATED_CODE_RNE);
        assertThat(testOrganisme.getSigle()).isEqualTo(UPDATED_SIGLE);
        assertThat(testOrganisme.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrganisme.getSecteur()).isEqualTo(UPDATED_SECTEUR);
        assertThat(testOrganisme.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testOrganisme.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testOrganisme.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteOrganisme() throws Exception {
        // Initialize the database
        organismeRepository.saveAndFlush(organisme);

		int databaseSizeBeforeDelete = organismeRepository.findAll().size();

        // Get the organisme
        restOrganismeMockMvc.perform(delete("/api/organismes/{id}", organisme.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Organisme> organismes = organismeRepository.findAll();
        assertThat(organismes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
