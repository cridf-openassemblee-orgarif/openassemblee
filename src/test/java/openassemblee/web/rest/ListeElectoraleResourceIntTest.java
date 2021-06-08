package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.ListeElectorale;
import openassemblee.repository.ListeElectoraleRepository;
import openassemblee.repository.search.ListeElectoraleSearchRepository;

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
 * Test class for the ListeElectoraleResource REST controller.
 *
 * @see ListeElectoraleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ListeElectoraleResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_NOM_COURT = "AAAAA";
    private static final String UPDATED_NOM_COURT = "BBBBB";

    @Inject
    private ListeElectoraleRepository listeElectoraleRepository;

    @Inject
    private ListeElectoraleSearchRepository listeElectoraleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restListeElectoraleMockMvc;

    private ListeElectorale listeElectorale;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ListeElectoraleResource listeElectoraleResource = new ListeElectoraleResource();
        ReflectionTestUtils.setField(listeElectoraleResource, "listeElectoraleRepository", listeElectoraleRepository);
        ReflectionTestUtils.setField(listeElectoraleResource, "listeElectoraleSearchRepository", listeElectoraleSearchRepository);
        this.restListeElectoraleMockMvc = MockMvcBuilders.standaloneSetup(listeElectoraleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        listeElectorale = new ListeElectorale();
        listeElectorale.setNom(DEFAULT_NOM);
        listeElectorale.setNomCourt(DEFAULT_NOM_COURT);
    }

    @Test
    @Transactional
    public void createListeElectorale() throws Exception {
        int databaseSizeBeforeCreate = listeElectoraleRepository.findAll().size();

        // Create the ListeElectorale

        restListeElectoraleMockMvc.perform(post("/api/listeElectorales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(listeElectorale)))
                .andExpect(status().isCreated());

        // Validate the ListeElectorale in the database
        List<ListeElectorale> listeElectorales = listeElectoraleRepository.findAll();
        assertThat(listeElectorales).hasSize(databaseSizeBeforeCreate + 1);
        ListeElectorale testListeElectorale = listeElectorales.get(listeElectorales.size() - 1);
        assertThat(testListeElectorale.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testListeElectorale.getNomCourt()).isEqualTo(DEFAULT_NOM_COURT);
    }

    @Test
    @Transactional
    public void getAllListeElectorales() throws Exception {
        // Initialize the database
        listeElectoraleRepository.saveAndFlush(listeElectorale);

        // Get all the listeElectorales
        restListeElectoraleMockMvc.perform(get("/api/listeElectorales"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(listeElectorale.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].nomCourt").value(hasItem(DEFAULT_NOM_COURT.toString())));
    }

    @Test
    @Transactional
    public void getListeElectorale() throws Exception {
        // Initialize the database
        listeElectoraleRepository.saveAndFlush(listeElectorale);

        // Get the listeElectorale
        restListeElectoraleMockMvc.perform(get("/api/listeElectorales/{id}", listeElectorale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(listeElectorale.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.nomCourt").value(DEFAULT_NOM_COURT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingListeElectorale() throws Exception {
        // Get the listeElectorale
        restListeElectoraleMockMvc.perform(get("/api/listeElectorales/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateListeElectorale() throws Exception {
        // Initialize the database
        listeElectoraleRepository.saveAndFlush(listeElectorale);

		int databaseSizeBeforeUpdate = listeElectoraleRepository.findAll().size();

        // Update the listeElectorale
        listeElectorale.setNom(UPDATED_NOM);
        listeElectorale.setNomCourt(UPDATED_NOM_COURT);

        restListeElectoraleMockMvc.perform(put("/api/listeElectorales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(listeElectorale)))
                .andExpect(status().isOk());

        // Validate the ListeElectorale in the database
        List<ListeElectorale> listeElectorales = listeElectoraleRepository.findAll();
        assertThat(listeElectorales).hasSize(databaseSizeBeforeUpdate);
        ListeElectorale testListeElectorale = listeElectorales.get(listeElectorales.size() - 1);
        assertThat(testListeElectorale.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testListeElectorale.getNomCourt()).isEqualTo(UPDATED_NOM_COURT);
    }

    @Test
    @Transactional
    public void deleteListeElectorale() throws Exception {
        // Initialize the database
        listeElectoraleRepository.saveAndFlush(listeElectorale);

		int databaseSizeBeforeDelete = listeElectoraleRepository.findAll().size();

        // Get the listeElectorale
        restListeElectoraleMockMvc.perform(delete("/api/listeElectorales/{id}", listeElectorale.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ListeElectorale> listeElectorales = listeElectoraleRepository.findAll();
        assertThat(listeElectorales).hasSize(databaseSizeBeforeDelete - 1);
    }
}
