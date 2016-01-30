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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.Application;
import fr.cridf.babylone14166.domain.AdressePostale;
import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.domain.enumeration.*;
import fr.cridf.babylone14166.repository.AdressePostaleRepository;
import fr.cridf.babylone14166.repository.EluRepository;
import fr.cridf.babylone14166.repository.search.EluSearchRepository;
import fr.cridf.babylone14166.service.EluService;

/**
 * Test class for the EluResource REST controller.
 *
 * @see EluResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EluResourceIntTest {

    private static final Civilite DEFAULT_CIVILITE = Civilite.MONSIEUR;
    private static final Civilite UPDATED_CIVILITE = Civilite.MADAME;
    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_PRENOM = "AAAAA";
    private static final String UPDATED_PRENOM = "BBBBB";
    private static final String DEFAULT_NOM_JEUNE_FILLE = "AAAAA";
    private static final String UPDATED_NOM_JEUNE_FILLE = "BBBBB";
    private static final String DEFAULT_PROFESSION = "AAAAA";
    private static final String UPDATED_PROFESSION = "BBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LIEU_NAISSANCE = "AAAAA";
    private static final String UPDATED_LIEU_NAISSANCE = "BBBBB";

    private static final NatureProPerso DEFAULT_NATURE_PRO_PERSO = NatureProPerso.PRO;
    private static final String DEFAULT_VOIE = "AAAAA";
    private static final String DEFAULT_CODE_POSTAL = "AAAAA";
    private static final String DEFAULT_VILLE = "AAAAA";

    private static final NiveauConfidentialite DEFAULT_NIVEAU_CONFIDENTIALITE = NiveauConfidentialite.PUBLIABLE;

    private static final Boolean DEFAULT_ADRESSE_DE_CORRESPONDANCE = false;

    private static final Boolean DEFAULT_PUBLICATION_ANNUAIRE = false;


    @Inject
    private EluRepository eluRepository;

    @Inject
    private EluSearchRepository eluSearchRepository;

    @Inject
    private AdressePostaleRepository adressePostaleRepository;

    @Inject
    private EluService eluService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEluMockMvc;

    private Elu elu;
    private AdressePostale adressePostale;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EluResource eluResource = new EluResource();
        ReflectionTestUtils.setField(eluResource, "eluRepository", eluRepository);
        ReflectionTestUtils.setField(eluResource, "eluSearchRepository", eluSearchRepository);
        ReflectionTestUtils.setField(eluResource, "eluService", eluService);
        this.restEluMockMvc = MockMvcBuilders.standaloneSetup(eluResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        elu = new Elu();
        elu.setCivilite(DEFAULT_CIVILITE);
        elu.setNom(DEFAULT_NOM);
        elu.setPrenom(DEFAULT_PRENOM);
        elu.setNomJeuneFille(DEFAULT_NOM_JEUNE_FILLE);
        elu.setProfession(DEFAULT_PROFESSION);
        elu.setDateNaissance(DEFAULT_DATE_NAISSANCE);
        elu.setLieuNaissance(DEFAULT_LIEU_NAISSANCE);
        List<AdressePostale> adressesPostales = new ArrayList<>();
        adressePostale = new AdressePostale();
        adressePostale = new AdressePostale();
        adressePostale.setNatureProPerso(DEFAULT_NATURE_PRO_PERSO);
        adressePostale.setVoie(DEFAULT_VOIE);
        adressePostale.setCodePostal(DEFAULT_CODE_POSTAL);
        adressePostale.setVille(DEFAULT_VILLE);
        adressePostale.setNiveauConfidentialite(DEFAULT_NIVEAU_CONFIDENTIALITE);
        adressePostale.setAdresseDeCorrespondance(DEFAULT_ADRESSE_DE_CORRESPONDANCE);
        adressePostale.setPublicationAnnuaire(DEFAULT_PUBLICATION_ANNUAIRE);
        adressesPostales.add(adressePostale);
        elu.setAdressesPostales(adressesPostales);
    }

    @Test
    @Transactional
    public void createElu() throws Exception {
        int databaseSizeBeforeCreate = eluRepository.findAll().size();

        // Create the Elu

        restEluMockMvc.perform(post("/api/elus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(elu)))
                .andExpect(status().isCreated());

        // Validate the Elu in the database
        List<Elu> elus = eluRepository.findAll();
        assertThat(elus).hasSize(databaseSizeBeforeCreate + 1);
        Elu testElu = elus.get(elus.size() - 1);
        assertThat(testElu.getCivilite()).isEqualTo(DEFAULT_CIVILITE);
        assertThat(testElu.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testElu.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testElu.getNomJeuneFille()).isEqualTo(DEFAULT_NOM_JEUNE_FILLE);
        assertThat(testElu.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testElu.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testElu.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElus() throws Exception {
        // Initialize the database
        adressePostaleRepository.saveAndFlush(adressePostale);
        eluRepository.saveAndFlush(elu);

        // Get all the elus
        restEluMockMvc.perform(get("/api/elus"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].elu.id").value(hasItem(elu.getId().intValue())))
                .andExpect(jsonPath("$.[*].elu.civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
                .andExpect(jsonPath("$.[*].elu.nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].elu.prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].elu.nomJeuneFille").value(hasItem(DEFAULT_NOM_JEUNE_FILLE.toString())))
                .andExpect(jsonPath("$.[*].elu.profession").value(hasItem(DEFAULT_PROFESSION.toString())))
                .andExpect(jsonPath("$.[*].elu.dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
                .andExpect(jsonPath("$.[*].elu.lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE.toString())));
    }

    @Test
    @Transactional
    public void getElu() throws Exception {
        // Initialize the database
        adressePostaleRepository.saveAndFlush(adressePostale);
        eluRepository.saveAndFlush(elu);

        // Get the elu
        restEluMockMvc.perform(get("/api/elus/{id}", elu.getId()))
            //            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.elu.id").value(elu.getId().intValue()))
            .andExpect(jsonPath("$.elu.civilite").value(DEFAULT_CIVILITE.toString()))
            .andExpect(jsonPath("$.elu.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.elu.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.elu.nomJeuneFille").value(DEFAULT_NOM_JEUNE_FILLE.toString()))
            .andExpect(jsonPath("$.elu.profession").value(DEFAULT_PROFESSION.toString()))
            .andExpect(jsonPath("$.elu.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.elu.lieuNaissance").value(DEFAULT_LIEU_NAISSANCE.toString()))
            .andExpect(jsonPath("$.elu.adressesPostales[0].voie").value(DEFAULT_VOIE));
        // TODO mlo test adresse + verifier ne vient pas avec les listes (?)
    }

    @Test
    @Transactional
    public void getNonExistingElu() throws Exception {
        // Get the elu
        restEluMockMvc.perform(get("/api/elus/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateElu() throws Exception {
        // Initialize the database
        adressePostaleRepository.saveAndFlush(adressePostale);
        eluRepository.saveAndFlush(elu);

		int databaseSizeBeforeUpdate = eluRepository.findAll().size();

        // Update the elu
        elu.setCivilite(UPDATED_CIVILITE);
        elu.setNom(UPDATED_NOM);
        elu.setPrenom(UPDATED_PRENOM);
        elu.setNomJeuneFille(UPDATED_NOM_JEUNE_FILLE);
        elu.setProfession(UPDATED_PROFESSION);
        elu.setDateNaissance(UPDATED_DATE_NAISSANCE);
        elu.setLieuNaissance(UPDATED_LIEU_NAISSANCE);

        restEluMockMvc.perform(put("/api/elus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(elu)))
                .andExpect(status().isOk());

        // Validate the Elu in the database
        List<Elu> elus = eluRepository.findAll();
        assertThat(elus).hasSize(databaseSizeBeforeUpdate);
        Elu testElu = elus.get(elus.size() - 1);
        assertThat(testElu.getCivilite()).isEqualTo(UPDATED_CIVILITE);
        assertThat(testElu.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testElu.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testElu.getNomJeuneFille()).isEqualTo(UPDATED_NOM_JEUNE_FILLE);
        assertThat(testElu.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testElu.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testElu.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void deleteElu() throws Exception {
        // Initialize the database
        adressePostaleRepository.saveAndFlush(adressePostale);
        eluRepository.saveAndFlush(elu);

		int databaseSizeBeforeDelete = eluRepository.findAll().size();

        // Get the elu
        restEluMockMvc.perform(delete("/api/elus/{id}", elu.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Elu> elus = eluRepository.findAll();
        assertThat(elus).hasSize(databaseSizeBeforeDelete - 1);
    }
}
