package fr.cridf.babylone14166.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.GroupePolitique;
import fr.cridf.babylone14166.repository.GroupePolitiqueRepository;
import fr.cridf.babylone14166.repository.search.GroupePolitiqueSearchRepository;
import fr.cridf.babylone14166.service.GroupePolitiqueService;

/**
 * Test class for the GroupePolitiqueResource REST controller.
 *
 * @see GroupePolitiqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
// TODO test adresse
public class GroupePolitiqueResourceIntTest {

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
    private GroupePolitiqueRepository groupePolitiqueRepository;

    @Inject
    private GroupePolitiqueSearchRepository groupePolitiqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private GroupePolitiqueService groupePolitiqueService;

    private MockMvc restGroupePolitiqueMockMvc;

    private GroupePolitique groupePolitique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GroupePolitiqueResource groupePolitiqueResource = new GroupePolitiqueResource();
        ReflectionTestUtils.setField(groupePolitiqueResource, "groupePolitiqueRepository", groupePolitiqueRepository);
        ReflectionTestUtils.setField(groupePolitiqueResource, "groupePolitiqueSearchRepository",
            groupePolitiqueSearchRepository);
        ReflectionTestUtils.setField(groupePolitiqueResource, "groupePolitiqueService", groupePolitiqueService);
        this.restGroupePolitiqueMockMvc = MockMvcBuilders.standaloneSetup(groupePolitiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        groupePolitique = new GroupePolitique();
        groupePolitique.setNom(DEFAULT_NOM);
        groupePolitique.setNomCourt(DEFAULT_NOM_COURT);
        groupePolitique.setDateDebut(DEFAULT_DATE_DEBUT);
        groupePolitique.setDateFin(DEFAULT_DATE_FIN);
        groupePolitique.setMotifFin(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void createGroupePolitique() throws Exception {
        int databaseSizeBeforeCreate = groupePolitiqueRepository.findAll().size();

        // Create the GroupePolitique

        restGroupePolitiqueMockMvc.perform(post("/api/groupePolitiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupePolitique)))
            .andExpect(status().isCreated());

        // Validate the GroupePolitique in the database
        List<GroupePolitique> groupePolitiques = groupePolitiqueRepository.findAll();
        assertThat(groupePolitiques).hasSize(databaseSizeBeforeCreate + 1);
        GroupePolitique testGroupePolitique = groupePolitiques.get(groupePolitiques.size() - 1);
        assertThat(testGroupePolitique.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testGroupePolitique.getNomCourt()).isEqualTo(DEFAULT_NOM_COURT);
        assertThat(testGroupePolitique.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testGroupePolitique.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testGroupePolitique.getMotifFin()).isEqualTo(DEFAULT_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void getAllGroupePolitiques() throws Exception {
        // Initialize the database
        groupePolitiqueRepository.saveAndFlush(groupePolitique);

        // Get all the groupePolitiques
        restGroupePolitiqueMockMvc.perform(get("/api/groupePolitiques"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupePolitique.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].nomCourt").value(hasItem(DEFAULT_NOM_COURT.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].motifFin").value(hasItem(DEFAULT_MOTIF_FIN.toString())));
    }

    @Test
    @Transactional
    public void getGroupePolitique() throws Exception {
        // Initialize the database
        groupePolitiqueRepository.saveAndFlush(groupePolitique);

        // Get the groupePolitique
        restGroupePolitiqueMockMvc.perform(get("/api/groupePolitiques/{id}", groupePolitique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.groupePolitique.id").value(groupePolitique.getId().intValue()))
            .andExpect(jsonPath("$.groupePolitique.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.groupePolitique.nomCourt").value(DEFAULT_NOM_COURT.toString()))
            .andExpect(jsonPath("$.groupePolitique.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.groupePolitique.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.groupePolitique.motifFin").value(DEFAULT_MOTIF_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGroupePolitique() throws Exception {
        // Get the groupePolitique
        restGroupePolitiqueMockMvc.perform(get("/api/groupePolitiques/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroupePolitique() throws Exception {
        // Initialize the database
        groupePolitiqueRepository.saveAndFlush(groupePolitique);

        int databaseSizeBeforeUpdate = groupePolitiqueRepository.findAll().size();

        // Update the groupePolitique
        groupePolitique.setNom(UPDATED_NOM);
        groupePolitique.setNomCourt(UPDATED_NOM_COURT);
        groupePolitique.setDateDebut(UPDATED_DATE_DEBUT);
        groupePolitique.setDateFin(UPDATED_DATE_FIN);
        groupePolitique.setMotifFin(UPDATED_MOTIF_FIN);

        restGroupePolitiqueMockMvc.perform(put("/api/groupePolitiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupePolitique)))
            .andExpect(status().isOk());

        // Validate the GroupePolitique in the database
        List<GroupePolitique> groupePolitiques = groupePolitiqueRepository.findAll();
        assertThat(groupePolitiques).hasSize(databaseSizeBeforeUpdate);
        GroupePolitique testGroupePolitique = groupePolitiques.get(groupePolitiques.size() - 1);
        assertThat(testGroupePolitique.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testGroupePolitique.getNomCourt()).isEqualTo(UPDATED_NOM_COURT);
        assertThat(testGroupePolitique.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testGroupePolitique.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testGroupePolitique.getMotifFin()).isEqualTo(UPDATED_MOTIF_FIN);
    }

    @Test
    @Transactional
    public void deleteGroupePolitique() throws Exception {
        // Initialize the database
        groupePolitiqueRepository.saveAndFlush(groupePolitique);

        int databaseSizeBeforeDelete = groupePolitiqueRepository.findAll().size();

        // Get the groupePolitique
        restGroupePolitiqueMockMvc.perform(delete("/api/groupePolitiques/{id}", groupePolitique.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GroupePolitique> groupePolitiques = groupePolitiqueRepository.findAll();
        assertThat(groupePolitiques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
