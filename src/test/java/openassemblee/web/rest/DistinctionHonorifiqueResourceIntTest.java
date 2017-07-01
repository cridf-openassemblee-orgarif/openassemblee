package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.DistinctionHonorifique;
import openassemblee.repository.DistinctionHonorifiqueRepository;
import openassemblee.repository.search.DistinctionHonorifiqueSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DistinctionHonorifiqueResource REST controller.
 *
 * @see DistinctionHonorifiqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DistinctionHonorifiqueResourceIntTest {

    private static final String DEFAULT_TITRE = "AAAAA";
    private static final String UPDATED_TITRE = "BBBBB";
    private static final String DEFAULT_DATE = "AAAAA";
    private static final String UPDATED_DATE = "BBBBB";

    @Inject
    private DistinctionHonorifiqueRepository distinctionHonorifiqueRepository;

    @Inject
    private DistinctionHonorifiqueSearchRepository distinctionHonorifiqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDistinctionHonorifiqueMockMvc;

    private DistinctionHonorifique distinctionHonorifique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DistinctionHonorifiqueResource distinctionHonorifiqueResource = new DistinctionHonorifiqueResource();
        ReflectionTestUtils.setField(distinctionHonorifiqueResource, "distinctionHonorifiqueRepository", distinctionHonorifiqueRepository);
        ReflectionTestUtils.setField(distinctionHonorifiqueResource, "distinctionHonorifiqueSearchRepository", distinctionHonorifiqueSearchRepository);
        this.restDistinctionHonorifiqueMockMvc = MockMvcBuilders.standaloneSetup(distinctionHonorifiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        distinctionHonorifique = new DistinctionHonorifique();
        distinctionHonorifique.setTitre(DEFAULT_TITRE);
        distinctionHonorifique.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createDistinctionHonorifique() throws Exception {
        int databaseSizeBeforeCreate = distinctionHonorifiqueRepository.findAll().size();

        // Create the DistinctionHonorifique

        restDistinctionHonorifiqueMockMvc.perform(post("/api/distinctionHonorifiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(distinctionHonorifique)))
                .andExpect(status().isCreated());

        // Validate the DistinctionHonorifique in the database
        List<DistinctionHonorifique> distinctionHonorifiques = distinctionHonorifiqueRepository.findAll();
        assertThat(distinctionHonorifiques).hasSize(databaseSizeBeforeCreate + 1);
        DistinctionHonorifique testDistinctionHonorifique = distinctionHonorifiques.get(distinctionHonorifiques.size() - 1);
        assertThat(testDistinctionHonorifique.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testDistinctionHonorifique.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void getAllDistinctionHonorifiques() throws Exception {
        // Initialize the database
        distinctionHonorifiqueRepository.saveAndFlush(distinctionHonorifique);

        // Get all the distinctionHonorifiques
        restDistinctionHonorifiqueMockMvc.perform(get("/api/distinctionHonorifiques"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(distinctionHonorifique.getId().intValue())))
                .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getDistinctionHonorifique() throws Exception {
        // Initialize the database
        distinctionHonorifiqueRepository.saveAndFlush(distinctionHonorifique);

        // Get the distinctionHonorifique
        restDistinctionHonorifiqueMockMvc.perform(get("/api/distinctionHonorifiques/{id}", distinctionHonorifique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(distinctionHonorifique.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDistinctionHonorifique() throws Exception {
        // Get the distinctionHonorifique
        restDistinctionHonorifiqueMockMvc.perform(get("/api/distinctionHonorifiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDistinctionHonorifique() throws Exception {
        // Initialize the database
        distinctionHonorifiqueRepository.saveAndFlush(distinctionHonorifique);

		int databaseSizeBeforeUpdate = distinctionHonorifiqueRepository.findAll().size();

        // Update the distinctionHonorifique
        distinctionHonorifique.setTitre(UPDATED_TITRE);
        distinctionHonorifique.setDate(UPDATED_DATE);

        restDistinctionHonorifiqueMockMvc.perform(put("/api/distinctionHonorifiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(distinctionHonorifique)))
                .andExpect(status().isOk());

        // Validate the DistinctionHonorifique in the database
        List<DistinctionHonorifique> distinctionHonorifiques = distinctionHonorifiqueRepository.findAll();
        assertThat(distinctionHonorifiques).hasSize(databaseSizeBeforeUpdate);
        DistinctionHonorifique testDistinctionHonorifique = distinctionHonorifiques.get(distinctionHonorifiques.size() - 1);
        assertThat(testDistinctionHonorifique.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testDistinctionHonorifique.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteDistinctionHonorifique() throws Exception {
        // Initialize the database
        distinctionHonorifiqueRepository.saveAndFlush(distinctionHonorifique);

		int databaseSizeBeforeDelete = distinctionHonorifiqueRepository.findAll().size();

        // Get the distinctionHonorifique
        restDistinctionHonorifiqueMockMvc.perform(delete("/api/distinctionHonorifiques/{id}", distinctionHonorifique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DistinctionHonorifique> distinctionHonorifiques = distinctionHonorifiqueRepository.findAll();
        assertThat(distinctionHonorifiques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
