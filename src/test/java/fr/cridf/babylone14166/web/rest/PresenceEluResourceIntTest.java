package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.PresenceElu;
import fr.cridf.babylone14166.repository.PresenceEluRepository;
import fr.cridf.babylone14166.repository.search.PresenceEluSearchRepository;

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
 * Test class for the PresenceEluResource REST controller.
 *
 * @see PresenceEluResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PresenceEluResourceIntTest {


    @Inject
    private PresenceEluRepository presenceEluRepository;

    @Inject
    private PresenceEluSearchRepository presenceEluSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPresenceEluMockMvc;

    private PresenceElu presenceElu;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PresenceEluResource presenceEluResource = new PresenceEluResource();
        ReflectionTestUtils.setField(presenceEluResource, "presenceEluRepository", presenceEluRepository);
        ReflectionTestUtils.setField(presenceEluResource, "presenceEluSearchRepository", presenceEluSearchRepository);
        this.restPresenceEluMockMvc = MockMvcBuilders.standaloneSetup(presenceEluResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        presenceElu = new PresenceElu();
    }

    @Test
    @Transactional
    public void createPresenceElu() throws Exception {
        int databaseSizeBeforeCreate = presenceEluRepository.findAll().size();

        // Create the PresenceElu

        restPresenceEluMockMvc.perform(post("/api/presenceElus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(presenceElu)))
                .andExpect(status().isCreated());

        // Validate the PresenceElu in the database
        List<PresenceElu> presenceElus = presenceEluRepository.findAll();
        assertThat(presenceElus).hasSize(databaseSizeBeforeCreate + 1);
        PresenceElu testPresenceElu = presenceElus.get(presenceElus.size() - 1);
    }

    @Test
    @Transactional
    public void getAllPresenceElus() throws Exception {
        // Initialize the database
        presenceEluRepository.saveAndFlush(presenceElu);

        // Get all the presenceElus
        restPresenceEluMockMvc.perform(get("/api/presenceElus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(presenceElu.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPresenceElu() throws Exception {
        // Initialize the database
        presenceEluRepository.saveAndFlush(presenceElu);

        // Get the presenceElu
        restPresenceEluMockMvc.perform(get("/api/presenceElus/{id}", presenceElu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(presenceElu.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPresenceElu() throws Exception {
        // Get the presenceElu
        restPresenceEluMockMvc.perform(get("/api/presenceElus/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePresenceElu() throws Exception {
        // Initialize the database
        presenceEluRepository.saveAndFlush(presenceElu);

		int databaseSizeBeforeUpdate = presenceEluRepository.findAll().size();

        // Update the presenceElu

        restPresenceEluMockMvc.perform(put("/api/presenceElus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(presenceElu)))
                .andExpect(status().isOk());

        // Validate the PresenceElu in the database
        List<PresenceElu> presenceElus = presenceEluRepository.findAll();
        assertThat(presenceElus).hasSize(databaseSizeBeforeUpdate);
        PresenceElu testPresenceElu = presenceElus.get(presenceElus.size() - 1);
    }

    @Test
    @Transactional
    public void deletePresenceElu() throws Exception {
        // Initialize the database
        presenceEluRepository.saveAndFlush(presenceElu);

		int databaseSizeBeforeDelete = presenceEluRepository.findAll().size();

        // Get the presenceElu
        restPresenceEluMockMvc.perform(delete("/api/presenceElus/{id}", presenceElu.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PresenceElu> presenceElus = presenceEluRepository.findAll();
        assertThat(presenceElus).hasSize(databaseSizeBeforeDelete - 1);
    }
}
