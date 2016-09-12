package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.AuditTrail;
import fr.cridf.babylone14166.repository.AuditTrailRepository;
import fr.cridf.babylone14166.repository.search.AuditTrailSearchRepository;

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

import fr.cridf.babylone14166.domain.enumeration.AuditLogAction;

/**
 * Test class for the AuditTrailResource REST controller.
 *
 * @see AuditTrailResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AuditTrailResourceIntTest {

    private static final String DEFAULT_ENTITY = "AAAAA";
    private static final String UPDATED_ENTITY = "BBBBB";
    private static final String DEFAULT_ENTITY_ID = "AAAAA";
    private static final String UPDATED_ENTITY_ID = "BBBBB";
    private static final String DEFAULT_PARENT_ENTITY = "AAAAA";
    private static final String UPDATED_PARENT_ENTITY = "BBBBB";
    private static final String DEFAULT_PARENT_ENTITY_ID = "AAAAA";
    private static final String UPDATED_PARENT_ENTITY_ID = "BBBBB";


private static final AuditLogAction DEFAULT_ACTION = AuditLogAction.CREATE;
    private static final AuditLogAction UPDATED_ACTION = AuditLogAction.UPDATE;
    private static final String DEFAULT_USER = "AAAAA";
    private static final String UPDATED_USER = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_DETAILS = "AAAAA";
    private static final String UPDATED_DETAILS = "BBBBB";
    private static final String DEFAULT_REASON = "AAAAA";
    private static final String UPDATED_REASON = "BBBBB";

    @Inject
    private AuditTrailRepository auditTrailRepository;

    @Inject
    private AuditTrailSearchRepository auditTrailSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAuditTrailMockMvc;

    private AuditTrail auditTrail;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuditTrailResource auditTrailResource = new AuditTrailResource();
        ReflectionTestUtils.setField(auditTrailResource, "auditTrailRepository", auditTrailRepository);
        ReflectionTestUtils.setField(auditTrailResource, "auditTrailSearchRepository", auditTrailSearchRepository);
        this.restAuditTrailMockMvc = MockMvcBuilders.standaloneSetup(auditTrailResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        auditTrail = new AuditTrail();
        auditTrail.setEntity(DEFAULT_ENTITY);
        auditTrail.setEntityId(DEFAULT_ENTITY_ID);
        auditTrail.setParentEntity(DEFAULT_PARENT_ENTITY);
        auditTrail.setParentEntityId(DEFAULT_PARENT_ENTITY_ID);
        auditTrail.setAction(DEFAULT_ACTION);
        auditTrail.setUser(DEFAULT_USER);
        auditTrail.setDate(DEFAULT_DATE);
        auditTrail.setDetails(DEFAULT_DETAILS);
        auditTrail.setReason(DEFAULT_REASON);
    }

    @Test
    @Transactional
    public void createAuditTrail() throws Exception {
        int databaseSizeBeforeCreate = auditTrailRepository.findAll().size();

        // Create the AuditTrail

        restAuditTrailMockMvc.perform(post("/api/auditTrails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auditTrail)))
                .andExpect(status().isCreated());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrails = auditTrailRepository.findAll();
        assertThat(auditTrails).hasSize(databaseSizeBeforeCreate + 1);
        AuditTrail testAuditTrail = auditTrails.get(auditTrails.size() - 1);
        assertThat(testAuditTrail.getEntity()).isEqualTo(DEFAULT_ENTITY);
        assertThat(testAuditTrail.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
        assertThat(testAuditTrail.getParentEntity()).isEqualTo(DEFAULT_PARENT_ENTITY);
        assertThat(testAuditTrail.getParentEntityId()).isEqualTo(DEFAULT_PARENT_ENTITY_ID);
        assertThat(testAuditTrail.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testAuditTrail.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testAuditTrail.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAuditTrail.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testAuditTrail.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    public void getAllAuditTrails() throws Exception {
        // Initialize the database
        auditTrailRepository.saveAndFlush(auditTrail);

        // Get all the auditTrails
        restAuditTrailMockMvc.perform(get("/api/auditTrails"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(auditTrail.getId().intValue())))
                .andExpect(jsonPath("$.[*].entity").value(hasItem(DEFAULT_ENTITY.toString())))
                .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.toString())))
                .andExpect(jsonPath("$.[*].parentEntity").value(hasItem(DEFAULT_PARENT_ENTITY.toString())))
                .andExpect(jsonPath("$.[*].parentEntityId").value(hasItem(DEFAULT_PARENT_ENTITY_ID.toString())))
                .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
                .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
                .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())));
    }

    @Test
    @Transactional
    public void getAuditTrail() throws Exception {
        // Initialize the database
        auditTrailRepository.saveAndFlush(auditTrail);

        // Get the auditTrail
        restAuditTrailMockMvc.perform(get("/api/auditTrails/{id}", auditTrail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(auditTrail.getId().intValue()))
            .andExpect(jsonPath("$.entity").value(DEFAULT_ENTITY.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.toString()))
            .andExpect(jsonPath("$.parentEntity").value(DEFAULT_PARENT_ENTITY.toString()))
            .andExpect(jsonPath("$.parentEntityId").value(DEFAULT_PARENT_ENTITY_ID.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuditTrail() throws Exception {
        // Get the auditTrail
        restAuditTrailMockMvc.perform(get("/api/auditTrails/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditTrail() throws Exception {
        // Initialize the database
        auditTrailRepository.saveAndFlush(auditTrail);

		int databaseSizeBeforeUpdate = auditTrailRepository.findAll().size();

        // Update the auditTrail
        auditTrail.setEntity(UPDATED_ENTITY);
        auditTrail.setEntityId(UPDATED_ENTITY_ID);
        auditTrail.setParentEntity(UPDATED_PARENT_ENTITY);
        auditTrail.setParentEntityId(UPDATED_PARENT_ENTITY_ID);
        auditTrail.setAction(UPDATED_ACTION);
        auditTrail.setUser(UPDATED_USER);
        auditTrail.setDate(UPDATED_DATE);
        auditTrail.setDetails(UPDATED_DETAILS);
        auditTrail.setReason(UPDATED_REASON);

        restAuditTrailMockMvc.perform(put("/api/auditTrails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auditTrail)))
                .andExpect(status().isOk());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrails = auditTrailRepository.findAll();
        assertThat(auditTrails).hasSize(databaseSizeBeforeUpdate);
        AuditTrail testAuditTrail = auditTrails.get(auditTrails.size() - 1);
        assertThat(testAuditTrail.getEntity()).isEqualTo(UPDATED_ENTITY);
        assertThat(testAuditTrail.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
        assertThat(testAuditTrail.getParentEntity()).isEqualTo(UPDATED_PARENT_ENTITY);
        assertThat(testAuditTrail.getParentEntityId()).isEqualTo(UPDATED_PARENT_ENTITY_ID);
        assertThat(testAuditTrail.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testAuditTrail.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testAuditTrail.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAuditTrail.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testAuditTrail.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    public void deleteAuditTrail() throws Exception {
        // Initialize the database
        auditTrailRepository.saveAndFlush(auditTrail);

		int databaseSizeBeforeDelete = auditTrailRepository.findAll().size();

        // Get the auditTrail
        restAuditTrailMockMvc.perform(delete("/api/auditTrails/{id}", auditTrail.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AuditTrail> auditTrails = auditTrailRepository.findAll();
        assertThat(auditTrails).hasSize(databaseSizeBeforeDelete - 1);
    }
}
