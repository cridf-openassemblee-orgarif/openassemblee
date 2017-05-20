package fr.cridf.babylone14166.web.rest;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.Signature;
import fr.cridf.babylone14166.repository.SignatureRepository;
import fr.cridf.babylone14166.repository.search.SignatureSearchRepository;

import fr.cridf.babylone14166.service.AuditTrailService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.cridf.babylone14166.domain.enumeration.SignatureStatus;

/**
 * Test class for the SignatureResource REST controller.
 *
 * @see SignatureResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SignatureResourceIntTest {


    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;


private static final SignatureStatus DEFAULT_STATUT = SignatureStatus.PRESENT;
    private static final SignatureStatus UPDATED_STATUT = SignatureStatus.ABSENT;

    @Inject
    private SignatureRepository signatureRepository;

    @Inject
    private SignatureSearchRepository signatureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private AuditTrailService auditTrailService;

    private MockMvc restSignatureMockMvc;

    private Signature signature;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SignatureResource signatureResource = new SignatureResource();
        ReflectionTestUtils.setField(signatureResource, "signatureRepository", signatureRepository);
        ReflectionTestUtils.setField(signatureResource, "signatureSearchRepository", signatureSearchRepository);
        ReflectionTestUtils.setField(signatureResource, "auditTrailService", auditTrailService);
        this.restSignatureMockMvc = MockMvcBuilders.standaloneSetup(signatureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        signature = new Signature();
        signature.setPosition(DEFAULT_POSITION);
        signature.setStatut(DEFAULT_STATUT);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(new User("admin", "admin", Collections.emptyList()), "admin"));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Transactional
    public void createSignature() throws Exception {
        int databaseSizeBeforeCreate = signatureRepository.findAll().size();

        // Create the Signature

        restSignatureMockMvc.perform(post("/api/signatures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signature)))
                .andExpect(status().isCreated());

        // Validate the Signature in the database
        List<Signature> signatures = signatureRepository.findAll();
        assertThat(signatures).hasSize(databaseSizeBeforeCreate + 1);
        Signature testSignature = signatures.get(signatures.size() - 1);
        assertThat(testSignature.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testSignature.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    public void getAllSignatures() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatures
        restSignatureMockMvc.perform(get("/api/signatures"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(signature.getId().intValue())))
                .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
                .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    public void getSignature() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get the signature
        restSignatureMockMvc.perform(get("/api/signatures/{id}", signature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(signature.getId().intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSignature() throws Exception {
        // Get the signature
        restSignatureMockMvc.perform(get("/api/signatures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignature() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

		int databaseSizeBeforeUpdate = signatureRepository.findAll().size();

        // Update the signature
        signature.setPosition(UPDATED_POSITION);
        signature.setStatut(UPDATED_STATUT);

        restSignatureMockMvc.perform(put("/api/signatures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signature)))
                .andExpect(status().isOk());

        // Validate the Signature in the database
        List<Signature> signatures = signatureRepository.findAll();
        assertThat(signatures).hasSize(databaseSizeBeforeUpdate);
        Signature testSignature = signatures.get(signatures.size() - 1);
        assertThat(testSignature.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testSignature.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void deleteSignature() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

		int databaseSizeBeforeDelete = signatureRepository.findAll().size();

        // Get the signature
        restSignatureMockMvc.perform(delete("/api/signatures/{id}", signature.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Signature> signatures = signatureRepository.findAll();
        assertThat(signatures).hasSize(databaseSizeBeforeDelete - 1);
    }
}
