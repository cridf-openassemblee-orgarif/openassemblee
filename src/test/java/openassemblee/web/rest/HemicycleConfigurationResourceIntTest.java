package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.HemicycleConfiguration;
import openassemblee.repository.HemicycleConfigurationRepository;
import openassemblee.repository.search.HemicycleConfigurationSearchRepository;

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
 * Test class for the HemicycleConfigurationResource REST controller.
 *
 * @see HemicycleConfigurationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HemicycleConfigurationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_LABEL = "AAAAA";
    private static final String UPDATED_LABEL = "BBBBB";
    private static final String DEFAULT_JSON_CONFIGURATION = "AAAAA";
    private static final String UPDATED_JSON_CONFIGURATION = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFICATION_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_MODIFICATION_DATE);

    private static final Boolean DEFAULT_FROZEN = false;
    private static final Boolean UPDATED_FROZEN = true;

    private static final ZonedDateTime DEFAULT_FROZEN_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FROZEN_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FROZEN_DATE_STR = dateTimeFormatter.format(DEFAULT_FROZEN_DATE);

    @Inject
    private HemicycleConfigurationRepository hemicycleConfigurationRepository;

    @Inject
    private HemicycleConfigurationSearchRepository hemicycleConfigurationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHemicycleConfigurationMockMvc;

    private HemicycleConfiguration hemicycleConfiguration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HemicycleConfigurationResource hemicycleConfigurationResource = new HemicycleConfigurationResource();
        ReflectionTestUtils.setField(hemicycleConfigurationResource, "hemicycleConfigurationRepository", hemicycleConfigurationRepository);
        ReflectionTestUtils.setField(hemicycleConfigurationResource, "hemicycleConfigurationSearchRepository", hemicycleConfigurationSearchRepository);
        this.restHemicycleConfigurationMockMvc = MockMvcBuilders.standaloneSetup(hemicycleConfigurationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hemicycleConfiguration = new HemicycleConfiguration();
        hemicycleConfiguration.setLabel(DEFAULT_LABEL);
        hemicycleConfiguration.setJsonConfiguration(DEFAULT_JSON_CONFIGURATION);
        hemicycleConfiguration.setCreationDate(DEFAULT_CREATION_DATE);
        hemicycleConfiguration.setLastModificationDate(DEFAULT_LAST_MODIFICATION_DATE);
        hemicycleConfiguration.setFrozen(DEFAULT_FROZEN);
        hemicycleConfiguration.setFrozenDate(DEFAULT_FROZEN_DATE);
    }

    @Test
    @Transactional
    public void createHemicycleConfiguration() throws Exception {
        int databaseSizeBeforeCreate = hemicycleConfigurationRepository.findAll().size();

        // Create the HemicycleConfiguration

        restHemicycleConfigurationMockMvc.perform(post("/api/hemicycleConfigurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemicycleConfiguration)))
                .andExpect(status().isCreated());

        // Validate the HemicycleConfiguration in the database
        List<HemicycleConfiguration> hemicycleConfigurations = hemicycleConfigurationRepository.findAll();
        assertThat(hemicycleConfigurations).hasSize(databaseSizeBeforeCreate + 1);
        HemicycleConfiguration testHemicycleConfiguration = hemicycleConfigurations.get(hemicycleConfigurations.size() - 1);
        assertThat(testHemicycleConfiguration.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testHemicycleConfiguration.getJsonConfiguration()).isEqualTo(DEFAULT_JSON_CONFIGURATION);
        assertThat(testHemicycleConfiguration.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testHemicycleConfiguration.getLastModificationDate()).isEqualTo(DEFAULT_LAST_MODIFICATION_DATE);
        assertThat(testHemicycleConfiguration.getFrozen()).isEqualTo(DEFAULT_FROZEN);
        assertThat(testHemicycleConfiguration.getFrozenDate()).isEqualTo(DEFAULT_FROZEN_DATE);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = hemicycleConfigurationRepository.findAll().size();
        // set the field null
        hemicycleConfiguration.setLabel(null);

        // Create the HemicycleConfiguration, which fails.

        restHemicycleConfigurationMockMvc.perform(post("/api/hemicycleConfigurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemicycleConfiguration)))
                .andExpect(status().isBadRequest());

        List<HemicycleConfiguration> hemicycleConfigurations = hemicycleConfigurationRepository.findAll();
        assertThat(hemicycleConfigurations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHemicycleConfigurations() throws Exception {
        // Initialize the database
        hemicycleConfigurationRepository.saveAndFlush(hemicycleConfiguration);

        // Get all the hemicycleConfigurations
        restHemicycleConfigurationMockMvc.perform(get("/api/hemicycleConfigurations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hemicycleConfiguration.getId().intValue())))
                .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
                .andExpect(jsonPath("$.[*].jsonConfiguration").value(hasItem(DEFAULT_JSON_CONFIGURATION.toString())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModificationDate").value(hasItem(DEFAULT_LAST_MODIFICATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].frozen").value(hasItem(DEFAULT_FROZEN.booleanValue())))
                .andExpect(jsonPath("$.[*].frozenDate").value(hasItem(DEFAULT_FROZEN_DATE_STR)));
    }

    @Test
    @Transactional
    public void getHemicycleConfiguration() throws Exception {
        // Initialize the database
        hemicycleConfigurationRepository.saveAndFlush(hemicycleConfiguration);

        // Get the hemicycleConfiguration
        restHemicycleConfigurationMockMvc.perform(get("/api/hemicycleConfigurations/{id}", hemicycleConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hemicycleConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.jsonConfiguration").value(DEFAULT_JSON_CONFIGURATION.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.lastModificationDate").value(DEFAULT_LAST_MODIFICATION_DATE_STR))
            .andExpect(jsonPath("$.frozen").value(DEFAULT_FROZEN.booleanValue()))
            .andExpect(jsonPath("$.frozenDate").value(DEFAULT_FROZEN_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingHemicycleConfiguration() throws Exception {
        // Get the hemicycleConfiguration
        restHemicycleConfigurationMockMvc.perform(get("/api/hemicycleConfigurations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHemicycleConfiguration() throws Exception {
        // Initialize the database
        hemicycleConfigurationRepository.saveAndFlush(hemicycleConfiguration);

		int databaseSizeBeforeUpdate = hemicycleConfigurationRepository.findAll().size();

        // Update the hemicycleConfiguration
        hemicycleConfiguration.setLabel(UPDATED_LABEL);
        hemicycleConfiguration.setJsonConfiguration(UPDATED_JSON_CONFIGURATION);
        hemicycleConfiguration.setCreationDate(UPDATED_CREATION_DATE);
        hemicycleConfiguration.setLastModificationDate(UPDATED_LAST_MODIFICATION_DATE);
        hemicycleConfiguration.setFrozen(UPDATED_FROZEN);
        hemicycleConfiguration.setFrozenDate(UPDATED_FROZEN_DATE);

        restHemicycleConfigurationMockMvc.perform(put("/api/hemicycleConfigurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemicycleConfiguration)))
                .andExpect(status().isOk());

        // Validate the HemicycleConfiguration in the database
        List<HemicycleConfiguration> hemicycleConfigurations = hemicycleConfigurationRepository.findAll();
        assertThat(hemicycleConfigurations).hasSize(databaseSizeBeforeUpdate);
        HemicycleConfiguration testHemicycleConfiguration = hemicycleConfigurations.get(hemicycleConfigurations.size() - 1);
        assertThat(testHemicycleConfiguration.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testHemicycleConfiguration.getJsonConfiguration()).isEqualTo(UPDATED_JSON_CONFIGURATION);
        assertThat(testHemicycleConfiguration.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testHemicycleConfiguration.getLastModificationDate()).isEqualTo(UPDATED_LAST_MODIFICATION_DATE);
        assertThat(testHemicycleConfiguration.getFrozen()).isEqualTo(UPDATED_FROZEN);
        assertThat(testHemicycleConfiguration.getFrozenDate()).isEqualTo(UPDATED_FROZEN_DATE);
    }

    @Test
    @Transactional
    public void deleteHemicycleConfiguration() throws Exception {
        // Initialize the database
        hemicycleConfigurationRepository.saveAndFlush(hemicycleConfiguration);

		int databaseSizeBeforeDelete = hemicycleConfigurationRepository.findAll().size();

        // Get the hemicycleConfiguration
        restHemicycleConfigurationMockMvc.perform(delete("/api/hemicycleConfigurations/{id}", hemicycleConfiguration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HemicycleConfiguration> hemicycleConfigurations = hemicycleConfigurationRepository.findAll();
        assertThat(hemicycleConfigurations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
