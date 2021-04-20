package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.Mandat;
import openassemblee.repository.MandatRepository;
import openassemblee.repository.search.MandatSearchRepository;

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
 * Test class for the MandatResource REST controller.
 *
 * @see MandatResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MandatResourceIntTest {


    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_CODE_DEPARTEMENT = "AAAAA";
    private static final String UPDATED_CODE_DEPARTEMENT = "BBBBB";
    private static final String DEFAULT_DEPARTEMENT = "AAAAA";
    private static final String UPDATED_DEPARTEMENT = "BBBBB";

    private static final LocalDate DEFAULT_DATE_DEMISSION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEMISSION = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MOTIF_DEMISSION = "AAAAA";
    private static final String UPDATED_MOTIF_DEMISSION = "BBBBB";

    @Inject
    private MandatRepository mandatRepository;

    @Inject
    private MandatSearchRepository mandatSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMandatMockMvc;

    private Mandat mandat;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MandatResource mandatResource = new MandatResource();
        ReflectionTestUtils.setField(mandatResource, "mandatRepository", mandatRepository);
        ReflectionTestUtils.setField(mandatResource, "mandatSearchRepository", mandatSearchRepository);
        this.restMandatMockMvc = MockMvcBuilders.standaloneSetup(mandatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mandat = new Mandat();
        mandat.setDateDebut(DEFAULT_DATE_DEBUT);
        mandat.setCodeDepartement(DEFAULT_CODE_DEPARTEMENT);
        mandat.setDepartement(DEFAULT_DEPARTEMENT);
        mandat.setDateDemission(DEFAULT_DATE_DEMISSION);
        mandat.setMotifDemission(DEFAULT_MOTIF_DEMISSION);
    }

    @Test
    @Transactional
    public void createMandat() throws Exception {
        int databaseSizeBeforeCreate = mandatRepository.findAll().size();

        // Create the Mandat

        restMandatMockMvc.perform(post("/api/mandats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mandat)))
                .andExpect(status().isCreated());

        // Validate the Mandat in the database
        List<Mandat> mandats = mandatRepository.findAll();
        assertThat(mandats).hasSize(databaseSizeBeforeCreate + 1);
        Mandat testMandat = mandats.get(mandats.size() - 1);
        assertThat(testMandat.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testMandat.getCodeDepartement()).isEqualTo(DEFAULT_CODE_DEPARTEMENT);
        assertThat(testMandat.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testMandat.getDateDemission()).isEqualTo(DEFAULT_DATE_DEMISSION);
        assertThat(testMandat.getMotifDemission()).isEqualTo(DEFAULT_MOTIF_DEMISSION);
    }

    @Test
    @Transactional
    public void getAllMandats() throws Exception {
        // Initialize the database
        mandatRepository.saveAndFlush(mandat);

        // Get all the mandats
        restMandatMockMvc.perform(get("/api/mandats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mandat.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].codeDepartement").value(hasItem(DEFAULT_CODE_DEPARTEMENT.toString())))
                .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
                .andExpect(jsonPath("$.[*].dateDemission").value(hasItem(DEFAULT_DATE_DEMISSION.toString())))
                .andExpect(jsonPath("$.[*].motifDemission").value(hasItem(DEFAULT_MOTIF_DEMISSION.toString())));
    }

    @Test
    @Transactional
    public void getMandat() throws Exception {
        // Initialize the database
        mandatRepository.saveAndFlush(mandat);

        // Get the mandat
        restMandatMockMvc.perform(get("/api/mandats/{id}", mandat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mandat.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.codeDepartement").value(DEFAULT_CODE_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.dateDemission").value(DEFAULT_DATE_DEMISSION.toString()))
            .andExpect(jsonPath("$.motifDemission").value(DEFAULT_MOTIF_DEMISSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMandat() throws Exception {
        // Get the mandat
        restMandatMockMvc.perform(get("/api/mandats/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMandat() throws Exception {
        // Initialize the database
        mandatRepository.saveAndFlush(mandat);

		int databaseSizeBeforeUpdate = mandatRepository.findAll().size();

        // Update the mandat
        mandat.setDateDebut(UPDATED_DATE_DEBUT);
        mandat.setCodeDepartement(UPDATED_CODE_DEPARTEMENT);
        mandat.setDepartement(UPDATED_DEPARTEMENT);
        mandat.setDateDemission(UPDATED_DATE_DEMISSION);
        mandat.setMotifDemission(UPDATED_MOTIF_DEMISSION);

        restMandatMockMvc.perform(put("/api/mandats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mandat)))
                .andExpect(status().isOk());

        // Validate the Mandat in the database
        List<Mandat> mandats = mandatRepository.findAll();
        assertThat(mandats).hasSize(databaseSizeBeforeUpdate);
        Mandat testMandat = mandats.get(mandats.size() - 1);
        assertThat(testMandat.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testMandat.getCodeDepartement()).isEqualTo(UPDATED_CODE_DEPARTEMENT);
        assertThat(testMandat.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testMandat.getDateDemission()).isEqualTo(UPDATED_DATE_DEMISSION);
        assertThat(testMandat.getMotifDemission()).isEqualTo(UPDATED_MOTIF_DEMISSION);
    }

    @Test
    @Transactional
    public void deleteMandat() throws Exception {
        // Initialize the database
        mandatRepository.saveAndFlush(mandat);

		int databaseSizeBeforeDelete = mandatRepository.findAll().size();

        // Get the mandat
        restMandatMockMvc.perform(delete("/api/mandats/{id}", mandat.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Mandat> mandats = mandatRepository.findAll();
        assertThat(mandats).hasSize(databaseSizeBeforeDelete - 1);
    }
}
