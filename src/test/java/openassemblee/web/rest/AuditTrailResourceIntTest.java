package openassemblee.web.rest;

import openassemblee.Application;
import openassemblee.domain.AuditTrail;
import openassemblee.domain.enumeration.AuditTrailAction;
import openassemblee.repository.AuditTrailRepository;
import openassemblee.repository.search.AuditTrailSearchRepository;
import openassemblee.web.rest.mapper.AuditTrailMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private static final int DEFAULT_ENTITY_ID = 111;
    private static final String DEFAULT_PARENT_ENTITY = "AAAAA";
    private static final int DEFAULT_PARENT_ENTITY_ID = 333;


    private static final AuditTrailAction DEFAULT_ACTION = AuditTrailAction.CREATE;
    private static final String DEFAULT_USER = "AAAAA";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofLocal(
        LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC), ZoneId.systemDefault(), ZoneOffset.UTC);
    private static final String DEFAULT_DETAILS = "AAAAA";
    private static final String DEFAULT_REASON = "AAAAA";

    @Inject
    private AuditTrailRepository auditTrailRepository;

    @Inject
    private AuditTrailSearchRepository auditTrailSearchRepository;

    @Inject
    private AuditTrailMapper auditTrailMapper;

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
        ReflectionTestUtils.setField(auditTrailResource, "auditTrailMapper", auditTrailMapper);
        this.restAuditTrailMockMvc = MockMvcBuilders.standaloneSetup(auditTrailResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        auditTrail = new AuditTrail();
        auditTrail.setEntity(DEFAULT_ENTITY);
        auditTrail.setEntityId(new Long(DEFAULT_ENTITY_ID));
        auditTrail.setParentEntity(DEFAULT_PARENT_ENTITY);
        auditTrail.setParentEntityId(new Long(DEFAULT_PARENT_ENTITY_ID));
        auditTrail.setAction(DEFAULT_ACTION);
        auditTrail.setUser(DEFAULT_USER);
        auditTrail.setDate(DEFAULT_DATE);
        auditTrail.setDetails(DEFAULT_DETAILS);
        auditTrail.setReason(DEFAULT_REASON);
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
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].parentEntity").value(hasItem(DEFAULT_PARENT_ENTITY.toString())))
            .andExpect(jsonPath("$.[*].parentEntityId").value(hasItem(DEFAULT_PARENT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.toString())))
            .andExpect(jsonPath("$.[*].date").value(auditTrailMapper.formatDate(DEFAULT_DATE)))
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
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID))
            .andExpect(jsonPath("$.parentEntity").value(DEFAULT_PARENT_ENTITY.toString()))
            .andExpect(jsonPath("$.parentEntityId").value(DEFAULT_PARENT_ENTITY_ID))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.toString()))
            .andExpect(jsonPath("$.date").value(auditTrailMapper.formatDate(DEFAULT_DATE)))
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

}
