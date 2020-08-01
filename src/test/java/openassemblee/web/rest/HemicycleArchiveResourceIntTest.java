package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.HemicycleArchive;
import openassemblee.repository.HemicycleArchiveRepository;
import openassemblee.repository.search.HemicycleArchiveSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the HemicycleArchiveResource REST controller.
 *
 * @see HemicycleArchiveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HemicycleArchiveResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_JSON_PLAN = "AAAAA";
    private static final String UPDATED_JSON_PLAN = "BBBBB";
    private static final String DEFAULT_SVG_PLAN = "AAAAA";
    private static final String UPDATED_SVG_PLAN = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    @Inject
    private HemicycleArchiveRepository hemicycleArchiveRepository;

    @Inject
    private HemicycleArchiveSearchRepository hemicycleArchiveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHemicycleArchiveMockMvc;

    private HemicycleArchive hemicycleArchive;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HemicycleArchiveResource hemicycleArchiveResource = new HemicycleArchiveResource();
        ReflectionTestUtils.setField(hemicycleArchiveResource, "hemicycleArchiveRepository", hemicycleArchiveRepository);
        ReflectionTestUtils.setField(hemicycleArchiveResource, "hemicycleArchiveSearchRepository", hemicycleArchiveSearchRepository);
        this.restHemicycleArchiveMockMvc = MockMvcBuilders.standaloneSetup(hemicycleArchiveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hemicycleArchive = new HemicycleArchive();
        hemicycleArchive.setJsonPlan(DEFAULT_JSON_PLAN);
        hemicycleArchive.setSvgPlan(DEFAULT_SVG_PLAN);
        hemicycleArchive.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createHemicycleArchive() throws Exception {
        int databaseSizeBeforeCreate = hemicycleArchiveRepository.findAll().size();

        // Create the HemicycleArchive

        restHemicycleArchiveMockMvc.perform(post("/api/hemicycleArchives")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemicycleArchive)))
                .andExpect(status().isCreated());

        // Validate the HemicycleArchive in the database
        List<HemicycleArchive> hemicycleArchives = hemicycleArchiveRepository.findAll();
        assertThat(hemicycleArchives).hasSize(databaseSizeBeforeCreate + 1);
        HemicycleArchive testHemicycleArchive = hemicycleArchives.get(hemicycleArchives.size() - 1);
        assertThat(testHemicycleArchive.getJsonPlan()).isEqualTo(DEFAULT_JSON_PLAN);
        assertThat(testHemicycleArchive.getSvgPlan()).isEqualTo(DEFAULT_SVG_PLAN);
        assertThat(testHemicycleArchive.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void getAllHemicycleArchives() throws Exception {
        // Initialize the database
        hemicycleArchiveRepository.saveAndFlush(hemicycleArchive);

        // Get all the hemicycleArchives
        restHemicycleArchiveMockMvc.perform(get("/api/hemicycleArchives"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hemicycleArchive.getId().intValue())))
                .andExpect(jsonPath("$.[*].jsonPlan").value(hasItem(DEFAULT_JSON_PLAN.toString())))
                .andExpect(jsonPath("$.[*].svgPlan").value(hasItem(DEFAULT_SVG_PLAN.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)));
    }

    @Test
    @Transactional
    public void getHemicycleArchive() throws Exception {
        // Initialize the database
        hemicycleArchiveRepository.saveAndFlush(hemicycleArchive);

        // Get the hemicycleArchive
        restHemicycleArchiveMockMvc.perform(get("/api/hemicycleArchives/{id}", hemicycleArchive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hemicycleArchive.getId().intValue()))
            .andExpect(jsonPath("$.jsonPlan").value(DEFAULT_JSON_PLAN.toString()))
            .andExpect(jsonPath("$.svgPlan").value(DEFAULT_SVG_PLAN.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingHemicycleArchive() throws Exception {
        // Get the hemicycleArchive
        restHemicycleArchiveMockMvc.perform(get("/api/hemicycleArchives/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHemicycleArchive() throws Exception {
        // Initialize the database
        hemicycleArchiveRepository.saveAndFlush(hemicycleArchive);

		int databaseSizeBeforeUpdate = hemicycleArchiveRepository.findAll().size();

        // Update the hemicycleArchive
        hemicycleArchive.setJsonPlan(UPDATED_JSON_PLAN);
        hemicycleArchive.setSvgPlan(UPDATED_SVG_PLAN);
        hemicycleArchive.setDate(UPDATED_DATE);

        restHemicycleArchiveMockMvc.perform(put("/api/hemicycleArchives")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemicycleArchive)))
                .andExpect(status().isOk());

        // Validate the HemicycleArchive in the database
        List<HemicycleArchive> hemicycleArchives = hemicycleArchiveRepository.findAll();
        assertThat(hemicycleArchives).hasSize(databaseSizeBeforeUpdate);
        HemicycleArchive testHemicycleArchive = hemicycleArchives.get(hemicycleArchives.size() - 1);
        assertThat(testHemicycleArchive.getJsonPlan()).isEqualTo(UPDATED_JSON_PLAN);
        assertThat(testHemicycleArchive.getSvgPlan()).isEqualTo(UPDATED_SVG_PLAN);
        assertThat(testHemicycleArchive.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteHemicycleArchive() throws Exception {
        // Initialize the database
        hemicycleArchiveRepository.saveAndFlush(hemicycleArchive);

		int databaseSizeBeforeDelete = hemicycleArchiveRepository.findAll().size();

        // Get the hemicycleArchive
        restHemicycleArchiveMockMvc.perform(delete("/api/hemicycleArchives/{id}", hemicycleArchive.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HemicycleArchive> hemicycleArchives = hemicycleArchiveRepository.findAll();
        assertThat(hemicycleArchives).hasSize(databaseSizeBeforeDelete - 1);
    }
}
