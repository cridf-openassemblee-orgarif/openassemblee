package fr.cridf.babylone14166.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

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

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.AdressePostale;
import fr.cridf.babylone14166.domain.enumeration.NatureProPerso;
import fr.cridf.babylone14166.domain.enumeration.NiveauConfidentialite;
import fr.cridf.babylone14166.repository.AdressePostaleRepository;
import fr.cridf.babylone14166.repository.search.AdressePostaleSearchRepository;

/**
 * Test class for the AdressePostaleResource REST controller.
 *
 * @see AdressePostaleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AdressePostaleResourceIntTest {

    public static final NatureProPerso DEFAULT_NATURE_PRO_PERSO = NatureProPerso.PRO;
    private static final NatureProPerso UPDATED_NATURE_PRO_PERSO = NatureProPerso.PERSO;
    public static final String DEFAULT_RUE = "AAAAA";
    private static final String UPDATED_RUE = "BBBBB";
    public static final String DEFAULT_CODE_POSTAL = "AAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBB";
    public static final String DEFAULT_VILLE = "AAAAA";
    private static final String UPDATED_VILLE = "BBBBB";

    public static final NiveauConfidentialite DEFAULT_NIVEAU_CONFIDENTIALITE = NiveauConfidentialite.PUBLIABLE;
    private static final NiveauConfidentialite UPDATED_NIVEAU_CONFIDENTIALITE = NiveauConfidentialite.CONFIDENTIEL;

    public static final Boolean DEFAULT_ADRESSE_DE_CORRESPONDANCE = false;
    private static final Boolean UPDATED_ADRESSE_DE_CORRESPONDANCE = true;

    public static final Boolean DEFAULT_PUBLICATION_ANNUAIRE = false;
    private static final Boolean UPDATED_PUBLICATION_ANNUAIRE = true;

    @Inject
    private AdressePostaleRepository adressePostaleRepository;

    @Inject
    private AdressePostaleSearchRepository adressePostaleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAdressePostaleMockMvc;

    private AdressePostale adressePostale;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdressePostaleResource adressePostaleResource = new AdressePostaleResource();
        ReflectionTestUtils.setField(adressePostaleResource, "adressePostaleRepository", adressePostaleRepository);
        ReflectionTestUtils.setField(adressePostaleResource, "adressePostaleSearchRepository", adressePostaleSearchRepository);
        this.restAdressePostaleMockMvc = MockMvcBuilders.standaloneSetup(adressePostaleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        adressePostale = new AdressePostale();
        adressePostale.setNatureProPerso(DEFAULT_NATURE_PRO_PERSO);
        adressePostale.setRue(DEFAULT_RUE);
        adressePostale.setCodePostal(DEFAULT_CODE_POSTAL);
        adressePostale.setVille(DEFAULT_VILLE);
        adressePostale.setNiveauConfidentialite(DEFAULT_NIVEAU_CONFIDENTIALITE);
        adressePostale.setAdresseDeCorrespondance(DEFAULT_ADRESSE_DE_CORRESPONDANCE);
        adressePostale.setPublicationAnnuaire(DEFAULT_PUBLICATION_ANNUAIRE);
    }

    @Test
    @Transactional
    public void createAdressePostale() throws Exception {
        int databaseSizeBeforeCreate = adressePostaleRepository.findAll().size();

        // Create the AdressePostale

        restAdressePostaleMockMvc.perform(post("/api/adressePostales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adressePostale)))
                .andExpect(status().isCreated());

        // Validate the AdressePostale in the database
        List<AdressePostale> adressePostales = adressePostaleRepository.findAll();
        assertThat(adressePostales).hasSize(databaseSizeBeforeCreate + 1);
        AdressePostale testAdressePostale = adressePostales.get(adressePostales.size() - 1);
        assertThat(testAdressePostale.getNatureProPerso()).isEqualTo(DEFAULT_NATURE_PRO_PERSO);
        assertThat(testAdressePostale.getRue()).isEqualTo(DEFAULT_RUE);
        assertThat(testAdressePostale.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testAdressePostale.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testAdressePostale.getNiveauConfidentialite()).isEqualTo(DEFAULT_NIVEAU_CONFIDENTIALITE);
        assertThat(testAdressePostale.getAdresseDeCorrespondance()).isEqualTo(DEFAULT_ADRESSE_DE_CORRESPONDANCE);
        assertThat(testAdressePostale.getPublicationAnnuaire()).isEqualTo(DEFAULT_PUBLICATION_ANNUAIRE);
    }

    @Test
    @Transactional
    public void getAllAdressePostales() throws Exception {
        // Initialize the database
        adressePostaleRepository.saveAndFlush(adressePostale);

        // Get all the adressePostales
        restAdressePostaleMockMvc.perform(get("/api/adressePostales"))
                .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adressePostale.getId().intValue())))
            .andExpect(jsonPath("$.[*].natureProPerso").value(hasItem(DEFAULT_NATURE_PRO_PERSO.toString())))
                .andExpect(jsonPath("$.[*].rue").value(hasItem(DEFAULT_RUE.toString())))
                .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL.toString())))
                .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
                .andExpect(jsonPath("$.[*].niveauConfidentialite").value(hasItem(DEFAULT_NIVEAU_CONFIDENTIALITE.toString())))
                .andExpect(jsonPath("$.[*].adresseDeCorrespondance").value(hasItem(DEFAULT_ADRESSE_DE_CORRESPONDANCE.booleanValue())))
                .andExpect(jsonPath("$.[*].publicationAnnuaire").value(hasItem(DEFAULT_PUBLICATION_ANNUAIRE.booleanValue())));
    }

    @Test
    @Transactional
    public void getAdressePostale() throws Exception {
        // Initialize the database
        adressePostaleRepository.saveAndFlush(adressePostale);

        // Get the adressePostale
        restAdressePostaleMockMvc.perform(get("/api/adressePostales/{id}", adressePostale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(adressePostale.getId().intValue()))
            .andExpect(jsonPath("$.natureProPerso").value(DEFAULT_NATURE_PRO_PERSO.toString()))
            .andExpect(jsonPath("$.rue").value(DEFAULT_RUE.toString()))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()))
            .andExpect(jsonPath("$.niveauConfidentialite").value(DEFAULT_NIVEAU_CONFIDENTIALITE.toString()))
            .andExpect(jsonPath("$.adresseDeCorrespondance").value(DEFAULT_ADRESSE_DE_CORRESPONDANCE.booleanValue()))
            .andExpect(jsonPath("$.publicationAnnuaire").value(DEFAULT_PUBLICATION_ANNUAIRE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAdressePostale() throws Exception {
        // Get the adressePostale
        restAdressePostaleMockMvc.perform(get("/api/adressePostales/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdressePostale() throws Exception {
        // Initialize the database
        adressePostaleRepository.saveAndFlush(adressePostale);

		int databaseSizeBeforeUpdate = adressePostaleRepository.findAll().size();

        // Update the adressePostale
        adressePostale.setNatureProPerso(UPDATED_NATURE_PRO_PERSO);
        adressePostale.setRue(UPDATED_RUE);
        adressePostale.setCodePostal(UPDATED_CODE_POSTAL);
        adressePostale.setVille(UPDATED_VILLE);
        adressePostale.setNiveauConfidentialite(UPDATED_NIVEAU_CONFIDENTIALITE);
        adressePostale.setAdresseDeCorrespondance(UPDATED_ADRESSE_DE_CORRESPONDANCE);
        adressePostale.setPublicationAnnuaire(UPDATED_PUBLICATION_ANNUAIRE);

        restAdressePostaleMockMvc.perform(put("/api/adressePostales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adressePostale)))
                .andExpect(status().isOk());

        // Validate the AdressePostale in the database
        List<AdressePostale> adressePostales = adressePostaleRepository.findAll();
        assertThat(adressePostales).hasSize(databaseSizeBeforeUpdate);
        AdressePostale testAdressePostale = adressePostales.get(adressePostales.size() - 1);
        assertThat(testAdressePostale.getNatureProPerso()).isEqualTo(UPDATED_NATURE_PRO_PERSO);
        assertThat(testAdressePostale.getRue()).isEqualTo(UPDATED_RUE);
        assertThat(testAdressePostale.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testAdressePostale.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testAdressePostale.getNiveauConfidentialite()).isEqualTo(UPDATED_NIVEAU_CONFIDENTIALITE);
        assertThat(testAdressePostale.getAdresseDeCorrespondance()).isEqualTo(UPDATED_ADRESSE_DE_CORRESPONDANCE);
        assertThat(testAdressePostale.getPublicationAnnuaire()).isEqualTo(UPDATED_PUBLICATION_ANNUAIRE);
    }

    @Test
    @Transactional
    public void deleteAdressePostale() throws Exception {
        // Initialize the database
        adressePostaleRepository.saveAndFlush(adressePostale);

		int databaseSizeBeforeDelete = adressePostaleRepository.findAll().size();

        // Get the adressePostale
        restAdressePostaleMockMvc.perform(delete("/api/adressePostales/{id}", adressePostale.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AdressePostale> adressePostales = adressePostaleRepository.findAll();
        assertThat(adressePostales).hasSize(databaseSizeBeforeDelete - 1);
    }
}
