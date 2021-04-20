package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.Mandature;
import openassemblee.repository.MandatureRepository;
import openassemblee.repository.search.MandatureSearchRepository;

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
 * Test class for the MandatureResource REST controller.
 *
 * @see MandatureResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MandatureResourceIntTest {


    private static final Integer DEFAULT_ANNEE_DEBUT = 1;
    private static final Integer UPDATED_ANNEE_DEBUT = 2;

    private static final Integer DEFAULT_ANNEE_FIN = 1;
    private static final Integer UPDATED_ANNEE_FIN = 2;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_CURRENT = false;
    private static final Boolean UPDATED_CURRENT = true;

    @Inject
    private MandatureRepository mandatureRepository;

    @Inject
    private MandatureSearchRepository mandatureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMandatureMockMvc;

    private Mandature mandature;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MandatureResource mandatureResource = new MandatureResource();
        ReflectionTestUtils.setField(mandatureResource, "mandatureRepository", mandatureRepository);
        ReflectionTestUtils.setField(mandatureResource, "mandatureSearchRepository", mandatureSearchRepository);
        this.restMandatureMockMvc = MockMvcBuilders.standaloneSetup(mandatureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mandature = new Mandature();
        mandature.setAnneeDebut(DEFAULT_ANNEE_DEBUT);
        mandature.setAnneeFin(DEFAULT_ANNEE_FIN);
        mandature.setDateDebut(DEFAULT_DATE_DEBUT);
        mandature.setCurrent(DEFAULT_CURRENT);
    }

    @Test
    @Transactional
    public void createMandature() throws Exception {
        int databaseSizeBeforeCreate = mandatureRepository.findAll().size();

        // Create the Mandature

        restMandatureMockMvc.perform(post("/api/mandatures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mandature)))
                .andExpect(status().isCreated());

        // Validate the Mandature in the database
        List<Mandature> mandatures = mandatureRepository.findAll();
        assertThat(mandatures).hasSize(databaseSizeBeforeCreate + 1);
        Mandature testMandature = mandatures.get(mandatures.size() - 1);
        assertThat(testMandature.getAnneeDebut()).isEqualTo(DEFAULT_ANNEE_DEBUT);
        assertThat(testMandature.getAnneeFin()).isEqualTo(DEFAULT_ANNEE_FIN);
        assertThat(testMandature.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testMandature.getCurrent()).isEqualTo(DEFAULT_CURRENT);
    }

    @Test
    @Transactional
    public void getAllMandatures() throws Exception {
        // Initialize the database
        mandatureRepository.saveAndFlush(mandature);

        // Get all the mandatures
        restMandatureMockMvc.perform(get("/api/mandatures"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mandature.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneeDebut").value(hasItem(DEFAULT_ANNEE_DEBUT)))
                .andExpect(jsonPath("$.[*].anneeFin").value(hasItem(DEFAULT_ANNEE_FIN)))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT.booleanValue())));
    }

    @Test
    @Transactional
    public void getMandature() throws Exception {
        // Initialize the database
        mandatureRepository.saveAndFlush(mandature);

        // Get the mandature
        restMandatureMockMvc.perform(get("/api/mandatures/{id}", mandature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mandature.getId().intValue()))
            .andExpect(jsonPath("$.anneeDebut").value(DEFAULT_ANNEE_DEBUT))
            .andExpect(jsonPath("$.anneeFin").value(DEFAULT_ANNEE_FIN))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.current").value(DEFAULT_CURRENT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMandature() throws Exception {
        // Get the mandature
        restMandatureMockMvc.perform(get("/api/mandatures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMandature() throws Exception {
        // Initialize the database
        mandatureRepository.saveAndFlush(mandature);

		int databaseSizeBeforeUpdate = mandatureRepository.findAll().size();

        // Update the mandature
        mandature.setAnneeDebut(UPDATED_ANNEE_DEBUT);
        mandature.setAnneeFin(UPDATED_ANNEE_FIN);
        mandature.setDateDebut(UPDATED_DATE_DEBUT);
        mandature.setCurrent(UPDATED_CURRENT);

        restMandatureMockMvc.perform(put("/api/mandatures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mandature)))
                .andExpect(status().isOk());

        // Validate the Mandature in the database
        List<Mandature> mandatures = mandatureRepository.findAll();
        assertThat(mandatures).hasSize(databaseSizeBeforeUpdate);
        Mandature testMandature = mandatures.get(mandatures.size() - 1);
        assertThat(testMandature.getAnneeDebut()).isEqualTo(UPDATED_ANNEE_DEBUT);
        assertThat(testMandature.getAnneeFin()).isEqualTo(UPDATED_ANNEE_FIN);
        assertThat(testMandature.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testMandature.getCurrent()).isEqualTo(UPDATED_CURRENT);
    }

    @Test
    @Transactional
    public void deleteMandature() throws Exception {
        // Initialize the database
        mandatureRepository.saveAndFlush(mandature);

		int databaseSizeBeforeDelete = mandatureRepository.findAll().size();

        // Get the mandature
        restMandatureMockMvc.perform(delete("/api/mandatures/{id}", mandature.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Mandature> mandatures = mandatureRepository.findAll();
        assertThat(mandatures).hasSize(databaseSizeBeforeDelete - 1);
    }
}
