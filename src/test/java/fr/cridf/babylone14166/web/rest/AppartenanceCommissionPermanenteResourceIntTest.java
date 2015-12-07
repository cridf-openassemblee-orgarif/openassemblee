package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.AppartenanceCommissionPermanente;
import fr.cridf.babylone14166.repository.AppartenanceCommissionPermanenteRepository;
import fr.cridf.babylone14166.repository.search.AppartenanceCommissionPermanenteSearchRepository;

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
 * Test class for the AppartenanceCommissionPermanenteResource REST controller.
 *
 * @see AppartenanceCommissionPermanenteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AppartenanceCommissionPermanenteResourceIntTest {


    @Inject
    private AppartenanceCommissionPermanenteRepository appartenanceCommissionPermanenteRepository;

    @Inject
    private AppartenanceCommissionPermanenteSearchRepository appartenanceCommissionPermanenteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAppartenanceCommissionPermanenteMockMvc;

    private AppartenanceCommissionPermanente appartenanceCommissionPermanente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppartenanceCommissionPermanenteResource appartenanceCommissionPermanenteResource = new AppartenanceCommissionPermanenteResource();
        ReflectionTestUtils.setField(appartenanceCommissionPermanenteResource, "appartenanceCommissionPermanenteRepository", appartenanceCommissionPermanenteRepository);
        ReflectionTestUtils.setField(appartenanceCommissionPermanenteResource, "appartenanceCommissionPermanenteSearchRepository", appartenanceCommissionPermanenteSearchRepository);
        this.restAppartenanceCommissionPermanenteMockMvc = MockMvcBuilders.standaloneSetup(appartenanceCommissionPermanenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        appartenanceCommissionPermanente = new AppartenanceCommissionPermanente();
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
                .andExpect(jsonPath("$.[*].id").value(hasItem(appartenanceCommissionPermanente.getId().intValue())));
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
            .andExpect(jsonPath("$.id").value(appartenanceCommissionPermanente.getId().intValue()));
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

        restAppartenanceCommissionPermanenteMockMvc.perform(put("/api/appartenanceCommissionPermanentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appartenanceCommissionPermanente)))
                .andExpect(status().isOk());

        // Validate the AppartenanceCommissionPermanente in the database
        List<AppartenanceCommissionPermanente> appartenanceCommissionPermanentes = appartenanceCommissionPermanenteRepository.findAll();
        assertThat(appartenanceCommissionPermanentes).hasSize(databaseSizeBeforeUpdate);
        AppartenanceCommissionPermanente testAppartenanceCommissionPermanente = appartenanceCommissionPermanentes.get(appartenanceCommissionPermanentes.size() - 1);
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
