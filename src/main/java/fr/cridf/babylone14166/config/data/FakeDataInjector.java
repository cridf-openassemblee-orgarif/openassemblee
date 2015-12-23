package fr.cridf.babylone14166.config.data;

import static fr.cridf.babylone14166.config.data.FakeDataLists.PROFESSIONS;
import static fr.cridf.babylone14166.config.data.FakeDataLists.VILLES;
import static fr.cridf.babylone14166.domain.enumeration.Civilite.MADAME;
import static fr.cridf.babylone14166.domain.enumeration.Civilite.MONSIEUR;

import java.time.LocalDate;
import java.util.*;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.domain.enumeration.Civilite;
import fr.cridf.babylone14166.repository.EluRepository;

@Component
public class FakeDataInjector {

    private Random random = new Random();

    @Autowired
    private EluRepository eluRepository;

    @PostConstruct
    public void init() {
        if (eluRepository.count() == 0) {
            eluRepository.save(getElus());
        }
    }

    public List<Elu> getElus() {
        List<Elu> elus = new ArrayList<>();
        elus.add(getElu(MONSIEUR, "ABSCHEN", "Jean"));
        elus.add(getElu(MONSIEUR, "ADAMO", "Stéphane"));
        elus.add(getElu(MADAME, "AMELLAL", "Viviane"));
        elus.add(getElu(MONSIEUR, "ANGONIN", "Jean-Pierre"));
        elus.add(getElu(MADAME, "AZOURA", "Marie-France"));
        elus.add(getElu(MADAME, "AZRIA", "Maryse"));
        elus.add(getElu(MADAME, "BACH", "Sylvie"));
        elus.add(getElu(MADAME, "BARNAUD", "Janine"));
        elus.add(getElu(MONSIEUR, "BENSIMHON", "Pascal"));
        elus.add(getElu(MONSIEUR, "BERTRAND", "Roger"));
        elus.add(getElu(MADAME, "BIDAULT", "Marie-Reine"));
        elus.add(getElu(MONSIEUR, "BINET", "Emmanuel"));
        elus.add(getElu(MADAME, "BLANC", "Giséle"));
        elus.add(getElu(MONSIEUR, "BLANCHOT", "Guy"));
        elus.add(getElu(MADAME, "BOUCHET", "Micheline"));
        elus.add(getElu(MADAME, "BOUDART", "Orianne"));
        elus.add(getElu(MONSIEUR, "BOULLICAUD", "Paul"));
        elus.add(getElu(MONSIEUR, "BOUSLAH", "Fabien"));
        elus.add(getElu(MADAME, "BOUZCKAR", "Ghislaine"));
        elus.add(getElu(MONSIEUR, "BOVERO", "Gilbert"));
        elus.add(getElu(MONSIEUR, "BRELEUR", "Christophe"));
        elus.add(getElu(MADAME, "CERCOTTE", "Marie-Isabelle"));
        elus.add(getElu(MADAME, "CHI", "Nicole"));
        elus.add(getElu(MONSIEUR, "CHICHE", "Vincent"));
        elus.add(getElu(MONSIEUR, "COBHEN", "Gaylor"));
        elus.add(getElu(MADAME, "COUDERC", "Marie-Louise"));
        elus.add(getElu(MONSIEUR, "CRIÉ", "Michel"));
        elus.add(getElu(MONSIEUR, "CYMBALIST", "Christophe"));
        elus.add(getElu(MADAME, "DESTAIN", "Roseline"));
        elus.add(getElu(MONSIEUR, "DORLEANS", "François-Xavier"));
        elus.add(getElu(MONSIEUR, "DORLEANS", "Jérémie"));
        elus.add(getElu(MADAME, "DUPRÉ", "Sophie"));
        elus.add(getElu(MONSIEUR, "EGREVE", "Jean-René"));
        elus.add(getElu(MONSIEUR, "EMILE-VICTOR", "Paul"));
        elus.add(getElu(MADAME, "FALZON", "Patricia"));
        elus.add(getElu(MONSIEUR, "FAUCHEUX", "Michel"));
        elus.add(getElu(MONSIEUR, "FEBVRE", "Denis"));
        elus.add(getElu(MADAME, "FERNANDEZ", "Yvette"));
        elus.add(getElu(MADAME, "FERRAND", "Sophie"));
        elus.add(getElu(MADAME, "FILLEAU", "Sylvie"));
        elus.add(getElu(MONSIEUR, "FITOUSSI", "Samuel"));
        elus.add(getElu(MADAME, "FOURNOL", "Nathalie"));
        elus.add(getElu(MONSIEUR, "FREYSSINET", "Jean-José"));
        elus.add(getElu(MADAME, "FREYSSINET", "Maud"));
        elus.add(getElu(MONSIEUR, "FREYSSINET", "Ludovic"));
        elus.add(getElu(MADAME, "GEIL", "Dominique"));
        elus.add(getElu(MADAME, "GENTIL", "Michelle"));
        elus.add(getElu(MONSIEUR, "GIRARD", "André"));
        elus.add(getElu(MONSIEUR, "GONDOUIN", "Bernard"));
        elus.add(getElu(MADAME, "GORZINSKY", "Odette"));
        elus.add(getElu(MADAME, "GUELT", "Monique"));
        elus.add(getElu(MONSIEUR, "GUITTON", "Francis"));
        elus.add(getElu(MONSIEUR, "HERMANT", "Jean-Pierre"));
        elus.add(getElu(MADAME, "ILARDO", "Sylvie"));
        elus.add(getElu(MADAME, "IMMEUBLE", "Sylvie"));
        elus.add(getElu(MADAME, "JOLIBOIS", "Michele"));
        elus.add(getElu(MADAME, "JULIENSE", "Marie-Thérèse"));
        elus.add(getElu(MONSIEUR, "JULIENSE", "Gautier"));
        elus.add(getElu(MONSIEUR, "JULIENSE", "Matthieu"));
        elus.add(getElu(MADAME, "KAC", "Christine"));
        elus.add(getElu(MONSIEUR, "LACIRE", "Vincent"));
        elus.add(getElu(MADAME, "LADD", "Claude"));
        elus.add(getElu(MONSIEUR, "LAFORET", "Hubert"));
        elus.add(getElu(MADAME, "LANDON", "Marie-Odile"));
        elus.add(getElu(MADAME, "LE LOCH", "Nicole"));
        elus.add(getElu(MADAME, "LE PREVOST", "Marie-Anne"));
        elus.add(getElu(MONSIEUR, "LEGRAND", "Stéphane"));
        elus.add(getElu(MONSIEUR, "LEMAIRE", "Philippe"));
        elus.add(getElu(MADAME, "LEURRE", "Denise"));
        elus.add(getElu(MONSIEUR, "LHERMITTE", "Bernard"));
        elus.add(getElu(MADAME, "LOUAPRE", "Louisette"));
        elus.add(getElu(MONSIEUR, "MARINIER", "Marcel"));
        elus.add(getElu(MADAME, "MAROTE", "Marie-José"));
        elus.add(getElu(MONSIEUR, "MARSHER", "Franz"));
        elus.add(getElu(MONSIEUR, "MARTAUD", "Daniel"));
        elus.add(getElu(MADAME, "MECHARD", "Véronique"));
        elus.add(getElu(MONSIEUR, "MIANET", "Georges"));
        elus.add(getElu(MONSIEUR, "MICELI", "Stéphane"));
        elus.add(getElu(MADAME, "MOITA", "Jeanne"));
        elus.add(getElu(MADAME, "NAIMI", "Lucienne"));
        elus.add(getElu(MONSIEUR, "PARTOUCHE", "Robert"));
        elus.add(getElu(MONSIEUR, "PERFETTO", "Pascal"));
        elus.add(getElu(MONSIEUR, "POISSON", "Daniel"));
        elus.add(getElu(MONSIEUR, "PUCCINI", "Ernesto"));
        elus.add(getElu(MONSIEUR, "RAMBEAUD", "Christian"));
        elus.add(getElu(MONSIEUR, "RAMOND", "Vincent"));
        elus.add(getElu(MADAME, "REMUND", "Françoise"));
        elus.add(getElu(MONSIEUR, "RIDEAU", "Bastien"));
        elus.add(getElu(MADAME, "RIEGERT", "Raymonde"));
        elus.add(getElu(MONSIEUR, "RODIER", "Régis"));
        elus.add(getElu(MONSIEUR, "ROGUET", "Laurent"));
        elus.add(getElu(MONSIEUR, "ROSSO", "Robert"));
        elus.add(getElu(MONSIEUR, "ROTENBERG", "Michel"));
        elus.add(getElu(MADAME, "ROUX", "Yveline"));
        elus.add(getElu(MONSIEUR, "SAINT DE FLER", "Quentin"));
        elus.add(getElu(MADAME, "SAINT DE FLER", "Elsa"));
        elus.add(getElu(MONSIEUR, "SAINT DE FLER", "Théo"));
        elus.add(getElu(MADAME, "SOK", "Vanessa"));
        elus.add(getElu(MADAME, "SONG", "Aline"));
        elus.add(getElu(MONSIEUR, "STOEFFLER", "Jean-Marc"));
        elus.add(getElu(MONSIEUR, "TAIEB", "Michel"));
        elus.add(getElu(MONSIEUR, "THAO", "Sylvain"));
        elus.add(getElu(MONSIEUR, "VANNAXAY", "Francis"));
        elus.add(getElu(MADAME, "VIDON", "Marie-Louise"));
        elus.add(getElu(MONSIEUR, "ZOUC", "Fred"));
        elus.parallelStream()
            .filter(elu -> elu.getCivilite() == MADAME)
            // une femme sur deux
            .filter(elu -> random.nextBoolean())
            .forEach(elu -> elu.setNomJeuneFille(elus.get(random.nextInt(elus.size())).getNom()));
        return elus;
    }

    private Elu getElu(Civilite civilite, String nom, String prenom) {
        Elu elu = new Elu();
        elu.setCivilite(civilite);
        elu.setNom(nom);
        elu.setPrenom(prenom);
        elu.setProfession(PROFESSIONS[random.nextInt(PROFESSIONS.length)]);
        elu.setLieuNaissance(VILLES[random.nextInt(VILLES.length)]);
        // TODO randomiser
        elu.setDateNaissance(LocalDate.of(1980, 6, 12));
        return elu;
    }

}
