package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.FonctionExecutive;
import fr.cridf.babylone14166.repository.FonctionExecutiveRepository;
import fr.cridf.babylone14166.repository.search.FonctionExecutiveSearchRepository;

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
 * Test class for the FonctionExecutiveResource REST controller.
 *
 * @see FonctionExecutiveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FonctionExecutiveResourceIntTest {


    @Inject
    private FonctionExecutiveRepository fonctionExecutiveRepository;

    @Inject
    private FonctionExecutiveSearchRepository fonctionExecutiveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFonctionExecutiveMockMvc;

    private FonctionExecutive fonctionExecutive;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FonctionExecutiveResource fonctionExecutiveResource = new FonctionExecutiveResource();
        ReflectionTestUtils.setField(fonctionExecutiveResource, "fonctionExecutiveRepository", fonctionExecutiveRepository);
        ReflectionTestUtils.setField(fonctionExecutiveResource, "fonctionExecutiveSearchRepository", fonctionExecutiveSearchRepository);
        this.restFonctionExecutiveMockMvc = MockMvcBuilders.standaloneSetup(fonctionExecutiveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fonctionExecutive = new FonctionExecutive();
    }

    @Test
    @Transactional
    public void createFonctionExecutive() throws Exception {
        int databaseSizeBeforeCreate = fonctionExecutiveRepository.findAll().size();

        // Create the FonctionExecutive

        restFonctionExecutiveMockMvc.perform(post("/api/fonctionExecutives")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionExecutive)))
                .andExpect(status().isCreated());

        // Validate the FonctionExecutive in the database
        List<FonctionExecutive> fonctionExecutives = fonctionExecutiveRepository.findAll();
        assertThat(fonctionExecutives).hasSize(databaseSizeBeforeCreate + 1);
        FonctionExecutive testFonctionExecutive = fonctionExecutives.get(fonctionExecutives.size() - 1);
    }

    @Test
    @Transactional
    public void getAllFonctionExecutives() throws Exception {
        // Initialize the database
        fonctionExecutiveRepository.saveAndFlush(fonctionExecutive);

        // Get all the fonctionExecutives
        restFonctionExecutiveMockMvc.perform(get("/api/fonctionExecutives"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fonctionExecutive.getId().intValue())));
    }

    @Test
    @Transactional
    public void getFonctionExecutive() throws Exception {
        // Initialize the database
        fonctionExecutiveRepository.saveAndFlush(fonctionExecutive);

        // Get the fonctionExecutive
        restFonctionExecutiveMockMvc.perform(get("/api/fonctionExecutives/{id}", fonctionExecutive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fonctionExecutive.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFonctionExecutive() throws Exception {
        // Get the fonctionExecutive
        restFonctionExecutiveMockMvc.perform(get("/api/fonctionExecutives/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFonctionExecutive() throws Exception {
        // Initialize the database
        fonctionExecutiveRepository.saveAndFlush(fonctionExecutive);

		int databaseSizeBeforeUpdate = fonctionExecutiveRepository.findAll().size();

        // Update the fonctionExecutive

        restFonctionExecutiveMockMvc.perform(put("/api/fonctionExecutives")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionExecutive)))
                .andExpect(status().isOk());

        // Validate the FonctionExecutive in the database
        List<FonctionExecutive> fonctionExecutives = fonctionExecutiveRepository.findAll();
        assertThat(fonctionExecutives).hasSize(databaseSizeBeforeUpdate);
        FonctionExecutive testFonctionExecutive = fonctionExecutives.get(fonctionExecutives.size() - 1);
    }

    @Test
    @Transactional
    public void deleteFonctionExecutive() throws Exception {
        // Initialize the database
        fonctionExecutiveRepository.saveAndFlush(fonctionExecutive);

		int databaseSizeBeforeDelete = fonctionExecutiveRepository.findAll().size();

        // Get the fonctionExecutive
        restFonctionExecutiveMockMvc.perform(delete("/api/fonctionExecutives/{id}", fonctionExecutive.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FonctionExecutive> fonctionExecutives = fonctionExecutiveRepository.findAll();
        assertThat(fonctionExecutives).hasSize(databaseSizeBeforeDelete - 1);
    }
}
