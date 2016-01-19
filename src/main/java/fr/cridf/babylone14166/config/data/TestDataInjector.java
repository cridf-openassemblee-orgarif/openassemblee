package fr.cridf.babylone14166.config.data;

import static fr.cridf.babylone14166.config.data.TestDataLists.PHOTOS;
import static fr.cridf.babylone14166.config.data.TestDataLists.PROFESSIONS;
import static fr.cridf.babylone14166.config.data.TestDataLists.VILLES;
import static fr.cridf.babylone14166.domain.enumeration.Civilite.MADAME;
import static fr.cridf.babylone14166.domain.enumeration.Civilite.MONSIEUR;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.elasticsearch.common.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.domain.enumeration.Civilite;
import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.service.IndexService;
import liquibase.util.csv.opencsv.CSVReader;

@Component
@Transactional
public class TestDataInjector {

    private final Logger logger = LoggerFactory.getLogger(TestDataInjector.class);

    @Autowired
    private EluRepository eluRepository;

    @Autowired
    private GroupePolitiqueRepository groupePolitiqueRepository;

    @Autowired
    private AppartenanceGroupePolitiqueRepository appartenanceGroupePolitiqueRepository;

    @Autowired
    private IndexService indexService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AdressePostaleRepository adressePostaleRepository;

    @Autowired
    private OrganismeRepository organismeRepository;

    private Random random;

    public void injectTestData() {
        if (eluRepository.count() == 0) {
            random = new Random();
            List<GroupePolitique> gps = initGroupesPolitiques();
            groupePolitiqueRepository.save(gps);
            List<Elu> elus = initElus(gps);
            eluRepository.save(elus);
            List<AppartenanceGroupePolitique> agps = initAppartenanceGroupePolitiques(elus, gps);
            appartenanceGroupePolitiqueRepository.save(agps);
            List<CommissionThematique> cts = initCommissionsThematiques();
            indexService.resetIndex();
        }
    }

    public void injectOrganismes() {
        if (organismeRepository.count() == 0) {
            CSVReader reader = new CSVReader(
                new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream("test-data-images/organismes.csv")), ';');
            List<Organisme> organismes = new ArrayList<>();
            try {
                String[] line = reader.readNext();
                while (line != null) {
                    Organisme o = new Organisme();
                    o.setCodeRNE(line[0].trim());
                    o.setNom(line[2].trim());
                    o.setSecteur(line[5].trim());
                    o.setType(line[1].trim());
                    o.setAdressePostale(new AdressePostale(line[6].trim(), line[7].trim(), line[8].trim()));
                    organismes.add(o);
                    adressePostaleRepository.save(o.getAdressePostale());
                    line = reader.readNext();
                }
                organismeRepository.save(organismes);
                reader.close();
            } catch (IOException e) {
                logger.debug("Can't read organismes CSV file", e);
            }
            indexService.resetIndex();
        }
    }

    private List<GroupePolitique> initGroupesPolitiques() {
        List<GroupePolitique> gps = new ArrayList<>();
        gps.add(initGroupePolitique("LR", "Les Républicains", "logo-des-Republicains.jpg"));
        gps.add(initGroupePolitique("PSR et app", "Socialistes et Républicains et apparentés", "logops.jpg"));
        gps.add(initGroupePolitique("UDI", "Union des démocrates indépendants", "LogoUDI.png"));
        gps.add(initGroupePolitique("FN", "FN - IDF Bleu Marine", null));
        gps.add(initGroupePolitique("EELVA", "Europe Ecologie Les Verts et apparentés", "logo_eelv_fondblanc.png"));
        gps.add(initGroupePolitique("CD", "Centre et démocrates", null));
        gps.add(initGroupePolitique("FDG", "Front de Gauche, Parti Communiste français, Parti de " +
            "gauche, Ensemble et République et Socialisme", "front-gauche.png"));
        gps.add(initGroupePolitique("RCDE", "Radical Citoyen Démocrate Ecologiste et apparentés", "ude.png"));
        return gps;
    }

    private GroupePolitique initGroupePolitique(String nomCourt, String nom, String image) {
        return null;
    }

    private List<Elu> initElus(List<GroupePolitique> gps) {
        List<Elu> elus = new ArrayList<>();
        elus.add(initElu(MONSIEUR, "ABSCHEN", "Jean"));
        elus.add(initElu(MONSIEUR, "ADAMO", "Stéphane"));
        elus.add(initElu(MADAME, "AMELLAL", "Viviane"));
        elus.add(initElu(MONSIEUR, "ANGONIN", "Jean-Pierre"));
        elus.add(initElu(MONSIEUR, "NOMSUPERLONGPARCEQUECOMMECAONVOITSIONACASSELESECRANSOUSICAVA", "Bob"));
        elus.add(initElu(MADAME, "AZOURA", "Marie-France"));
        elus.add(initElu(MADAME, "AZRIA", "Maryse"));
        elus.add(initElu(MADAME, "BACH", "Sylvie"));
        elus.add(initElu(MADAME, "BARNAUD", "Janine"));
        elus.add(initElu(MONSIEUR, "BENSIMHON", "Pascal"));
        elus.add(initElu(MONSIEUR, "BERTRAND", "Roger"));
        elus.add(initElu(MADAME, "BIDAULT", "Marie-Reine"));
        elus.add(initElu(MONSIEUR, "BINET", "Emmanuel"));
        elus.add(initElu(MADAME, "BLANC", "Giséle"));
        elus.add(initElu(MONSIEUR, "BLANCHOT", "Guy"));
        elus.add(initElu(MADAME, "BOUCHET", "Micheline"));
        elus.add(initElu(MADAME, "BOUDART", "Orianne"));
        elus.add(initElu(MONSIEUR, "BOULLICAUD", "Paul"));
        elus.add(initElu(MONSIEUR, "BOUSLAH", "Fabien"));
        elus.add(initElu(MADAME, "BOUZCKAR", "Ghislaine"));
        elus.add(initElu(MONSIEUR, "BOVERO", "Gilbert"));
        elus.add(initElu(MONSIEUR, "BRELEUR", "Christophe"));
        elus.add(initElu(MADAME, "CERCOTTE", "Marie-Isabelle"));
        elus.add(initElu(MADAME, "CHI", "Nicole"));
        elus.add(initElu(MONSIEUR, "CHICHE", "Vincent"));
        elus.add(initElu(MONSIEUR, "COBHEN", "Gaylor"));
        elus.add(initElu(MADAME, "COUDERC", "Marie-Louise"));
        elus.add(initElu(MONSIEUR, "CRIÉ", "Michel"));
        elus.add(initElu(MONSIEUR, "CYMBALIST", "Christophe"));
        elus.add(initElu(MADAME, "DESTAIN", "Roseline"));
        elus.add(initElu(MONSIEUR, "DORLEANS", "François-Xavier"));
        elus.add(initElu(MONSIEUR, "DORLEANS", "Jérémie"));
        elus.add(initElu(MADAME, "DUPRÉ", "Sophie"));
        elus.add(initElu(MONSIEUR, "EGREVE", "Jean-René"));
        elus.add(initElu(MONSIEUR, "EMILE-VICTOR", "Paul"));
        elus.add(initElu(MADAME, "FALZON", "Patricia"));
        elus.add(initElu(MONSIEUR, "FAUCHEUX", "Michel"));
        elus.add(initElu(MONSIEUR, "FEBVRE", "Denis"));
        elus.add(initElu(MADAME, "FERNANDEZ", "Yvette"));
        elus.add(initElu(MADAME, "FERRAND", "Sophie"));
        elus.add(initElu(MADAME, "FILLEAU", "Sylvie"));
        elus.add(initElu(MONSIEUR, "FITOUSSI", "Samuel"));
        elus.add(initElu(MADAME, "FOURNOL", "Nathalie"));
        elus.add(initElu(MONSIEUR, "FREYSSINET", "Jean-José"));
        elus.add(initElu(MADAME, "FREYSSINET", "Maud"));
        elus.add(initElu(MONSIEUR, "FREYSSINET", "Ludovic"));
        elus.add(initElu(MADAME, "GEIL", "Dominique"));
        elus.add(initElu(MADAME, "GENTIL", "Michelle"));
        elus.add(initElu(MONSIEUR, "GIRARD", "André"));
        elus.add(initElu(MONSIEUR, "GONDOUIN", "Bernard"));
        elus.add(initElu(MADAME, "GORZINSKY", "Odette"));
        elus.add(initElu(MADAME, "GUELT", "Monique"));
        elus.add(initElu(MONSIEUR, "GUITTON", "Francis"));
        elus.add(initElu(MONSIEUR, "HERMANT", "Jean-Pierre"));
        elus.add(initElu(MADAME, "ILARDO", "Sylvie"));
        elus.add(initElu(MADAME, "IMMEUBLE", "Sylvie"));
        elus.add(initElu(MADAME, "JOLIBOIS", "Michele"));
        elus.add(initElu(MADAME, "JULIENSE", "Marie-Thérèse"));
        elus.add(initElu(MONSIEUR, "JULIENSE", "Gautier"));
        elus.add(initElu(MONSIEUR, "JULIENSE", "Matthieu"));
        elus.add(initElu(MADAME, "KAC", "Christine"));
        elus.add(initElu(MONSIEUR, "LACIRE", "Vincent"));
        elus.add(initElu(MADAME, "LADD", "Claude"));
        elus.add(initElu(MONSIEUR, "LAFORET", "Hubert"));
        elus.add(initElu(MADAME, "LANDON", "Marie-Odile"));
        elus.add(initElu(MADAME, "LE LOCH", "Nicole"));
        elus.add(initElu(MADAME, "LE PREVOST", "Marie-Anne"));
        elus.add(initElu(MONSIEUR, "LEGRAND", "Stéphane"));
        elus.add(initElu(MONSIEUR, "LEMAIRE", "Philippe"));
        elus.add(initElu(MADAME, "LEURRE", "Denise"));
        elus.add(initElu(MONSIEUR, "LHERMITTE", "Bernard"));
        elus.add(initElu(MADAME, "LOUAPRE", "Louisette"));
        elus.add(initElu(MONSIEUR, "MARINIER", "Marcel"));
        elus.add(initElu(MADAME, "MAROTE", "Marie-José"));
        elus.add(initElu(MONSIEUR, "MARSHER", "Franz"));
        elus.add(initElu(MONSIEUR, "MARTAUD", "Daniel"));
        elus.add(initElu(MADAME, "MECHARD", "Véronique"));
        elus.add(initElu(MONSIEUR, "MIANET", "Georges"));
        elus.add(initElu(MONSIEUR, "MICELI", "Stéphane"));
        elus.add(initElu(MADAME, "MOITA", "Jeanne"));
        elus.add(initElu(MADAME, "NAIMI", "Lucienne"));
        elus.add(initElu(MONSIEUR, "PARTOUCHE", "Robert"));
        elus.add(initElu(MONSIEUR, "PERFETTO", "Pascal"));
        elus.add(initElu(MONSIEUR, "POISSON", "Daniel"));
        elus.add(initElu(MONSIEUR, "PUCCINI", "Ernesto"));
        elus.add(initElu(MONSIEUR, "RAMBEAUD", "Christian"));
        elus.add(initElu(MONSIEUR, "RAMOND", "Vincent"));
        elus.add(initElu(MADAME, "REMUND", "Françoise"));
        elus.add(initElu(MONSIEUR, "RIDEAU", "Bastien"));
        elus.add(initElu(MADAME, "RIEGERT", "Raymonde"));
        elus.add(initElu(MONSIEUR, "RODIER", "Régis"));
        elus.add(initElu(MONSIEUR, "ROGUET", "Laurent"));
        elus.add(initElu(MONSIEUR, "ROSSO", "Robert"));
        elus.add(initElu(MONSIEUR, "ROTENBERG", "Michel"));
        elus.add(initElu(MADAME, "ROUX", "Yveline"));
        elus.add(initElu(MONSIEUR, "SAINT DE FLER", "Quentin"));
        elus.add(initElu(MADAME, "SAINT DE FLER", "Elsa"));
        elus.add(initElu(MONSIEUR, "SAINT DE FLER", "Théo"));
        elus.add(initElu(MADAME, "SOK", "Vanessa"));
        elus.add(initElu(MADAME, "SONG", "Aline"));
        elus.add(initElu(MONSIEUR, "STOEFFLER", "Jean-Marc"));
        elus.add(initElu(MONSIEUR, "TAIEB", "Michel"));
        elus.add(initElu(MONSIEUR, "THAO", "Sylvain"));
        elus.add(initElu(MONSIEUR, "VANNAXAY", "Francis"));
        elus.add(initElu(MADAME, "VIDON", "Marie-Louise"));
        elus.add(initElu(MONSIEUR, "ZOUC", "Fred"));
        elus.parallelStream()
            .filter(elu -> elu.getCivilite() == MADAME)
            // une femme sur deux
            .filter(elu -> random.nextBoolean())
            .forEach(elu -> elu.setNomJeuneFille(elus.get(random.nextInt(elus.size())).getNom()));
        return elus;
    }

    private Elu initElu(Civilite civilite, String nom, String prenom) {
        Elu elu = new Elu();
        elu.setCivilite(civilite);
        elu.setNom(nom);
        elu.setPrenom(prenom);
        elu.setProfession(PROFESSIONS[random.nextInt(PROFESSIONS.length)]);
        elu.setLieuNaissance(VILLES[random.nextInt(VILLES.length)]);
        elu.setDateNaissance(randomDate(LocalDate.of(1950, 6, 12), LocalDate.of(1980, 6, 12)));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String photo = PHOTOS[random.nextInt(PHOTOS.length)];
        try {
            Streams.copy(getClass().getClassLoader().getResourceAsStream("test-data-images/" + photo), baos);
            Long imageId = imageRepository.saveImage(new Image("image/jpeg", baos.toByteArray()));
            elu.setImage(imageId);
        } catch (SQLException e) {
            logger.error("Error while saving image : " + photo + "'", e);
        } catch (IOException e) {
            logger.error("Error while copying file : " + photo + "'", e);
        }
        return elu;
    }

    private List<AppartenanceGroupePolitique> initAppartenanceGroupePolitiques(List<Elu> elus,
        List<GroupePolitique> gps) {
        return elus.stream().map(elu -> {
            GroupePolitique gp = gps.get(random.nextInt(gps.size()));
            AppartenanceGroupePolitique agp = new AppartenanceGroupePolitique();
            agp.setElu(elu);
            agp.setGroupePolitique(gp);
            agp.setDateDebut(randomDate(gp.getDateDebut(), LocalDate.now()));
            return agp;
        }).collect(Collectors.toList());
    }

    private List<CommissionThematique> initCommissionsThematiques() {
        List<CommissionThematique> cts = new ArrayList<>();
        cts.add(initCommissionThematique("Commission du Règlement", "CR"));
        cts.add(initCommissionThematique("Commission des Finances", "CF"));
        cts.add(initCommissionThematique("Commission des Transports", "CT"));
        return cts;
    }

    private CommissionThematique initCommissionThematique(String nom, String nomCourt) {
        CommissionThematique ct = new CommissionThematique();
        ct.setNom(nom);
        ct.setNomCourt(nomCourt);
        ct.setDateDebut(randomDate(LocalDate.of(2015, 3, 1), LocalDate.of(2015, 12, 1)));
        return ct;
    }

    private LocalDate randomDate(LocalDate minDay, LocalDate maxDay) {
        long minEpochDay = minDay.toEpochDay();
        return LocalDate.ofEpochDay(minEpochDay + random.nextInt((int) (maxDay.toEpochDay() - minEpochDay)));
    }

}
