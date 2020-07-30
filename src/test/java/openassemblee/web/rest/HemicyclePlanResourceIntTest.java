package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.HemicyclePlan;
import openassemblee.repository.HemicyclePlanRepository;
import openassemblee.repository.search.HemicyclePlanSearchRepository;

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
 * Test class for the HemicyclePlanResource REST controller.
 *
 * @see HemicyclePlanResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HemicyclePlanResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_LABEL = "AAAAA";
    private static final String UPDATED_LABEL = "BBBBB";
    private static final String DEFAULT_JSON_PLAN = "AAAAA";
    private static final String UPDATED_JSON_PLAN = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFICATION_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_MODIFICATION_DATE);

    @Inject
    private HemicyclePlanRepository hemicyclePlanRepository;

    @Inject
    private HemicyclePlanSearchRepository hemicyclePlanSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHemicyclePlanMockMvc;

    private HemicyclePlan hemicyclePlan;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HemicyclePlanResource hemicyclePlanResource = new HemicyclePlanResource();
        ReflectionTestUtils.setField(hemicyclePlanResource, "hemicyclePlanRepository", hemicyclePlanRepository);
        ReflectionTestUtils.setField(hemicyclePlanResource, "hemicyclePlanSearchRepository", hemicyclePlanSearchRepository);
        this.restHemicyclePlanMockMvc = MockMvcBuilders.standaloneSetup(hemicyclePlanResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hemicyclePlan = new HemicyclePlan();
        hemicyclePlan.setLabel(DEFAULT_LABEL);
        hemicyclePlan.setJsonPlan(DEFAULT_JSON_PLAN);
        hemicyclePlan.setCreationDate(DEFAULT_CREATION_DATE);
        hemicyclePlan.setLastModificationDate(DEFAULT_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void createHemicyclePlan() throws Exception {
        int databaseSizeBeforeCreate = hemicyclePlanRepository.findAll().size();

        // Create the HemicyclePlan

        restHemicyclePlanMockMvc.perform(post("/api/hemicyclePlans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemicyclePlan)))
                .andExpect(status().isCreated());

        // Validate the HemicyclePlan in the database
        List<HemicyclePlan> hemicyclePlans = hemicyclePlanRepository.findAll();
        assertThat(hemicyclePlans).hasSize(databaseSizeBeforeCreate + 1);
        HemicyclePlan testHemicyclePlan = hemicyclePlans.get(hemicyclePlans.size() - 1);
        assertThat(testHemicyclePlan.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testHemicyclePlan.getJsonPlan()).isEqualTo(DEFAULT_JSON_PLAN);
        assertThat(testHemicyclePlan.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testHemicyclePlan.getLastModificationDate()).isEqualTo(DEFAULT_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = hemicyclePlanRepository.findAll().size();
        // set the field null
        hemicyclePlan.setLabel(null);

        // Create the HemicyclePlan, which fails.

        restHemicyclePlanMockMvc.perform(post("/api/hemicyclePlans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemicyclePlan)))
                .andExpect(status().isBadRequest());

        List<HemicyclePlan> hemicyclePlans = hemicyclePlanRepository.findAll();
        assertThat(hemicyclePlans).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHemicyclePlans() throws Exception {
        // Initialize the database
        hemicyclePlanRepository.saveAndFlush(hemicyclePlan);

        // Get all the hemicyclePlans
        restHemicyclePlanMockMvc.perform(get("/api/hemicyclePlans"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hemicyclePlan.getId().intValue())))
                .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
                .andExpect(jsonPath("$.[*].jsonPlan").value(hasItem(DEFAULT_JSON_PLAN.toString())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModificationDate").value(hasItem(DEFAULT_LAST_MODIFICATION_DATE_STR)));
    }

    @Test
    @Transactional
    public void getHemicyclePlan() throws Exception {
        // Initialize the database
        hemicyclePlanRepository.saveAndFlush(hemicyclePlan);

        // Get the hemicyclePlan
        restHemicyclePlanMockMvc.perform(get("/api/hemicyclePlans/{id}", hemicyclePlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hemicyclePlan.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.jsonPlan").value(DEFAULT_JSON_PLAN.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.lastModificationDate").value(DEFAULT_LAST_MODIFICATION_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingHemicyclePlan() throws Exception {
        // Get the hemicyclePlan
        restHemicyclePlanMockMvc.perform(get("/api/hemicyclePlans/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHemicyclePlan() throws Exception {
        // Initialize the database
        hemicyclePlanRepository.saveAndFlush(hemicyclePlan);

		int databaseSizeBeforeUpdate = hemicyclePlanRepository.findAll().size();

        // Update the hemicyclePlan
        hemicyclePlan.setLabel(UPDATED_LABEL);
        hemicyclePlan.setJsonPlan(UPDATED_JSON_PLAN);
        hemicyclePlan.setCreationDate(UPDATED_CREATION_DATE);
        hemicyclePlan.setLastModificationDate(UPDATED_LAST_MODIFICATION_DATE);

        restHemicyclePlanMockMvc.perform(put("/api/hemicyclePlans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemicyclePlan)))
                .andExpect(status().isOk());

        // Validate the HemicyclePlan in the database
        List<HemicyclePlan> hemicyclePlans = hemicyclePlanRepository.findAll();
        assertThat(hemicyclePlans).hasSize(databaseSizeBeforeUpdate);
        HemicyclePlan testHemicyclePlan = hemicyclePlans.get(hemicyclePlans.size() - 1);
        assertThat(testHemicyclePlan.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testHemicyclePlan.getJsonPlan()).isEqualTo(UPDATED_JSON_PLAN);
        assertThat(testHemicyclePlan.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testHemicyclePlan.getLastModificationDate()).isEqualTo(UPDATED_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void deleteHemicyclePlan() throws Exception {
        // Initialize the database
        hemicyclePlanRepository.saveAndFlush(hemicyclePlan);

		int databaseSizeBeforeDelete = hemicyclePlanRepository.findAll().size();

        // Get the hemicyclePlan
        restHemicyclePlanMockMvc.perform(delete("/api/hemicyclePlans/{id}", hemicyclePlan.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HemicyclePlan> hemicyclePlans = hemicyclePlanRepository.findAll();
        assertThat(hemicyclePlans).hasSize(databaseSizeBeforeDelete - 1);
    }
}
