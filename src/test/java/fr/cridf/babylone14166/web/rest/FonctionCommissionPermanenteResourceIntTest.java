package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.FonctionCommissionPermanente;
import fr.cridf.babylone14166.repository.FonctionCommissionPermanenteRepository;
import fr.cridf.babylone14166.repository.search.FonctionCommissionPermanenteSearchRepository;

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
 * Test class for the FonctionCommissionPermanenteResource REST controller.
 *
 * @see FonctionCommissionPermanenteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FonctionCommissionPermanenteResourceIntTest {


    @Inject
    private FonctionCommissionPermanenteRepository fonctionCommissionPermanenteRepository;

    @Inject
    private FonctionCommissionPermanenteSearchRepository fonctionCommissionPermanenteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFonctionCommissionPermanenteMockMvc;

    private FonctionCommissionPermanente fonctionCommissionPermanente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FonctionCommissionPermanenteResource fonctionCommissionPermanenteResource = new FonctionCommissionPermanenteResource();
        ReflectionTestUtils.setField(fonctionCommissionPermanenteResource, "fonctionCommissionPermanenteRepository", fonctionCommissionPermanenteRepository);
        ReflectionTestUtils.setField(fonctionCommissionPermanenteResource, "fonctionCommissionPermanenteSearchRepository", fonctionCommissionPermanenteSearchRepository);
        this.restFonctionCommissionPermanenteMockMvc = MockMvcBuilders.standaloneSetup(fonctionCommissionPermanenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fonctionCommissionPermanente = new FonctionCommissionPermanente();
    }

    @Test
    @Transactional
    public void createFonctionCommissionPermanente() throws Exception {
        int databaseSizeBeforeCreate = fonctionCommissionPermanenteRepository.findAll().size();

        // Create the FonctionCommissionPermanente

        restFonctionCommissionPermanenteMockMvc.perform(post("/api/fonctionCommissionPermanentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionCommissionPermanente)))
                .andExpect(status().isCreated());

        // Validate the FonctionCommissionPermanente in the database
        List<FonctionCommissionPermanente> fonctionCommissionPermanentes = fonctionCommissionPermanenteRepository.findAll();
        assertThat(fonctionCommissionPermanentes).hasSize(databaseSizeBeforeCreate + 1);
        FonctionCommissionPermanente testFonctionCommissionPermanente = fonctionCommissionPermanentes.get(fonctionCommissionPermanentes.size() - 1);
    }

    @Test
    @Transactional
    public void getAllFonctionCommissionPermanentes() throws Exception {
        // Initialize the database
        fonctionCommissionPermanenteRepository.saveAndFlush(fonctionCommissionPermanente);

        // Get all the fonctionCommissionPermanentes
        restFonctionCommissionPermanenteMockMvc.perform(get("/api/fonctionCommissionPermanentes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fonctionCommissionPermanente.getId().intValue())));
    }

    @Test
    @Transactional
    public void getFonctionCommissionPermanente() throws Exception {
        // Initialize the database
        fonctionCommissionPermanenteRepository.saveAndFlush(fonctionCommissionPermanente);

        // Get the fonctionCommissionPermanente
        restFonctionCommissionPermanenteMockMvc.perform(get("/api/fonctionCommissionPermanentes/{id}", fonctionCommissionPermanente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fonctionCommissionPermanente.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFonctionCommissionPermanente() throws Exception {
        // Get the fonctionCommissionPermanente
        restFonctionCommissionPermanenteMockMvc.perform(get("/api/fonctionCommissionPermanentes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFonctionCommissionPermanente() throws Exception {
        // Initialize the database
        fonctionCommissionPermanenteRepository.saveAndFlush(fonctionCommissionPermanente);

		int databaseSizeBeforeUpdate = fonctionCommissionPermanenteRepository.findAll().size();

        // Update the fonctionCommissionPermanente

        restFonctionCommissionPermanenteMockMvc.perform(put("/api/fonctionCommissionPermanentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionCommissionPermanente)))
                .andExpect(status().isOk());

        // Validate the FonctionCommissionPermanente in the database
        List<FonctionCommissionPermanente> fonctionCommissionPermanentes = fonctionCommissionPermanenteRepository.findAll();
        assertThat(fonctionCommissionPermanentes).hasSize(databaseSizeBeforeUpdate);
        FonctionCommissionPermanente testFonctionCommissionPermanente = fonctionCommissionPermanentes.get(fonctionCommissionPermanentes.size() - 1);
    }

    @Test
    @Transactional
    public void deleteFonctionCommissionPermanente() throws Exception {
        // Initialize the database
        fonctionCommissionPermanenteRepository.saveAndFlush(fonctionCommissionPermanente);

		int databaseSizeBeforeDelete = fonctionCommissionPermanenteRepository.findAll().size();

        // Get the fonctionCommissionPermanente
        restFonctionCommissionPermanenteMockMvc.perform(delete("/api/fonctionCommissionPermanentes/{id}", fonctionCommissionPermanente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FonctionCommissionPermanente> fonctionCommissionPermanentes = fonctionCommissionPermanenteRepository.findAll();
        assertThat(fonctionCommissionPermanentes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
