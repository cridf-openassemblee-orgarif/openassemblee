package openassemblee.web;

import openassemblee.domain.*;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.domain.enumeration.TypeIdentiteInternet;
import openassemblee.publicdata.*;
import openassemblee.repository.*;
import org.elasticsearch.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoField.*;
import static openassemblee.domain.enumeration.NiveauConfidentialite.PUBLIABLE;

@RestController
@RequestMapping("/api/inject/v1")
public class InjectDataWebservice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
        .appendValue(DAY_OF_MONTH, 2)
        .appendLiteral('/')
        .appendValue(MONTH_OF_YEAR, 2)
        .appendLiteral('/')
        .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
        .toFormatter();

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private AdressePostaleRepository adressePostaleRepository;
    @Autowired
    private AdresseMailRepository adresseMailRepository;
    @Autowired
    private NumeroTelephoneRepository numeroTelephoneRepository;
    @Autowired
    private NumeroFaxRepository numeroFaxRepository;
    @Autowired
    private EluRepository eluRepository;
    @Autowired
    private GroupePolitiqueRepository groupePolitiqueRepository;
    @Autowired
    private OrganismeRepository organismeRepository;
    @Autowired
    private CommissionThematiqueRepository commissionThematiqueRepository;
    @Autowired
    private AppartenanceGroupePolitiqueRepository appartenanceGroupePolitiqueRepository;
    @Autowired
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;
    @Autowired
    private AppartenanceOrganismeRepository appartenanceOrganismeRepository;
    @Autowired
    private IdentiteInternetRepository identiteInternetRepository;

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String ping() {
        return "ping";
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @Transactional
    public void data(@RequestBody DataBag data) {
        identiteInternetRepository.deleteAll();
        organismeRepository.deleteAll();
        commissionThematiqueRepository.deleteAll();
        appartenanceOrganismeRepository.deleteAll();
        appartenanceCommissionThematiqueRepository.deleteAll();
        appartenanceGroupePolitiqueRepository.deleteAll();
        groupePolitiqueRepository.deleteAll();
        eluRepository.deleteAll();
        adressePostaleRepository.deleteAll();
        adresseMailRepository.deleteAll();
        numeroTelephoneRepository.deleteAll();
        numeroFaxRepository.deleteAll();
        eluRepository.deleteAll();

        Map<String, Elu> elus = data.getConseillers().stream()
            .map(this::buildElu)
            .collect(Collectors.toMap(Elu::getImportUid, it -> it));
        logger.info(elus.size() + " élus");
        Map<String, GroupePolitique> gps = data.getEnsembles().stream()
            .filter(e -> e.getType().equals("groupe politique"))
            .map(this::buildGroupePolitique)
            .collect(Collectors.toMap(GroupePolitique::getImportUid, it -> it));
        logger.info(gps.size() + " groupes politiques");
        Map<String, CommissionThematique> cts = data.getEnsembles().stream()
            .filter(e -> e.getType().equals("commission"))
            .filter(e -> e.getTypeCommission().equals("Thématique"))
            .map(this::buildCommissionThematique)
            .collect(Collectors.toMap(CommissionThematique::getImportUid, it -> it));
        logger.info(cts.size() + " commissions thématiques");
        Map<String, Organisme> organismes = data.getEnsembles().stream()
            .filter(e -> e.getType().equals("organisme"))
            .map(this::buildOrganisme)
            .collect(Collectors.toMap(Organisme::getImportUid, it -> it));
        logger.info(organismes.size() + " organismes");

        // appartenances commission permanente
//        String CP = data.getEnsembles().stream()
//            .filter(e -> e.getType().equals("commission"))
//            .filter(e -> e.getTypeCommission().equals("Permanente"))
//            .findAny().get().getUidEnsemble();
//
//        // appartenances executif
//        String executif = data.getEnsembles().stream()
//            .filter(e -> e.getType().equals("commission"))
//            .filter(e -> e.getTypeCommission().equals("Exécutif"))
//            .findAny().get().getUidEnsemble();
//
//        String deleguesSpeciaux = data.getEnsembles().stream()
//            .filter(e -> e.getType().equals("commission"))
//            .filter(e -> e.getTypeCommission().equals("Délégués spéciaux"))
//            .findAny().get().getUidEnsemble();

        // appartenances groupe politique
        data.getMembres().stream()
            .filter(m -> m.getType().equals("groupe politique"))
            .forEach(m -> buildAppartenanceGroupePolitique(m, elus, gps));

        // appartenances commission thématique
        data.getMembres().stream()
            .filter(m -> m.getType().equals("commission"))
            .filter(m -> gps.containsKey(m.getUidEnsemble()))
            .forEach(m -> buildAppartenanceCommissionThematique(m, elus, cts));

        // appartenances organismes
        data.getMembres().stream()
            .filter(m -> m.getType().equals("organisme"))
            .filter(m -> gps.containsKey(m.getUidEnsemble()))
            .forEach(m -> buildAppartenanceOrganisme(m, elus, organismes));
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    @Transactional
    public void data(@RequestBody ImportImage importImage) {
        Elu elu = eluRepository.findOneByImportUid(importImage.getConseillerUid());
        if (importImage.getImage() != null) {
            byte[] data = importImage.getImage();
            Image image = new Image(MediaType.IMAGE_JPEG_VALUE, data);
            try {
                Long imageId = imageRepository.saveImage(image);
                elu.setImage(imageId);
                eluRepository.save(elu);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Elu buildElu(ConseillerDto c) {
        Elu elu = new Elu();
        elu.setCivilite(c.getCivilite().equals("M.") ? Civilite.MONSIEUR : Civilite.MADAME);
        elu.setNom(c.getNom());
        elu.setPrenom(c.getPrenom());
        elu.setNomJeuneFille(c.getNomJeuneFille());
        elu.setProfession(c.getProfession());
        elu.setDateNaissance(parseDate(c.getDateNaissance()));
        elu.setLieuNaissance(c.getVilleNaissance().trim());
        elu.setListeCourt(c.getListeCourt());
        elu.setListeElectorale(c.getListeElectorale());
        elu.setDepartement(c.getDepElection());
        elu.setCodeDepartement(c.getCodeDepElection());

        AdressePostale ap = new AdressePostale();
        ap.setVoie(c.getAdresse());
        ap.setCodePostal(c.getCodePostal());
        ap.setVille(c.getVille());
        ap.setNiveauConfidentialite(PUBLIABLE);
        ap.setAdresseDeCorrespondance(true);
        ap.setPublicationAnnuaire(true);
        adressePostaleRepository.save(ap);
        elu.getAdressesPostales().add(ap);

        NumeroTelephone nt = new NumeroTelephone();
        nt.setNumero(c.getTelephone());
        nt.setNiveauConfidentialite(PUBLIABLE);
        nt.setPublicationAnnuaire(true);
        numeroTelephoneRepository.save(nt);
        elu.getNumerosTelephones().add(nt);

        NumeroFax nf = new NumeroFax();
        nf.setNumero(c.getFax());
        nf.setNiveauConfidentialite(PUBLIABLE);
        nf.setPublicationAnnuaire(true);
        numeroFaxRepository.save(nf);
        elu.getNumerosFax().add(nf);

        AdresseMail am = new AdresseMail();
        am.setMail(c.getMail());
        am.setAdresseDeCorrespondance(true);
        am.setNiveauConfidentialite(PUBLIABLE);
        am.setPublicationAnnuaire(true);
        adresseMailRepository.save(am);
        elu.getAdressesMail().add(am);
        if (!Strings.isNullOrEmpty(c.getTwitter())) {
            IdentiteInternet ii = new IdentiteInternet();
            ii.setTypeIdentiteInternet(TypeIdentiteInternet.Twitter);
            ii.setUrl(c.getTwitter());
            identiteInternetRepository.save(ii);
            elu.getIdentitesInternet().add(ii);
        }
        if (!Strings.isNullOrEmpty(c.getFacebook())) {
            IdentiteInternet ii = new IdentiteInternet();
            ii.setTypeIdentiteInternet(TypeIdentiteInternet.Facebook);
            ii.setUrl(c.getFacebook());
            identiteInternetRepository.save(ii);
            elu.getIdentitesInternet().add(ii);
        }
        if (!Strings.isNullOrEmpty(c.getSiteInternet())) {
            IdentiteInternet ii = new IdentiteInternet();
            ii.setTypeIdentiteInternet(TypeIdentiteInternet.SiteInternet);
            ii.setUrl(c.getSiteInternet());
            identiteInternetRepository.save(ii);
            elu.getIdentitesInternet().add(ii);
        }
        if (!Strings.isNullOrEmpty(c.getBlog())) {
            IdentiteInternet ii = new IdentiteInternet();
            ii.setTypeIdentiteInternet(TypeIdentiteInternet.Blog);
            ii.setUrl(c.getBlog());
            identiteInternetRepository.save(ii);
            elu.getIdentitesInternet().add(ii);
        }
        if (!Strings.isNullOrEmpty(c.getAutre())) {
            IdentiteInternet ii = new IdentiteInternet();
            ii.setTypeIdentiteInternet(TypeIdentiteInternet.Autre);
            ii.setUrl(c.getAutre());
            identiteInternetRepository.save(ii);
            elu.getIdentitesInternet().add(ii);
        }
        elu.setImportUid(c.getUidConseiller());
        eluRepository.save(elu);
        return elu;
    }

    private LocalDate parseDate(String date) {
        if (!Strings.isNullOrEmpty(date) && !date.equals(" ")) {
            return LocalDate.parse(date, DATE_FORMATTER);
        } else {
            return null;
        }
    }

    private GroupePolitique buildGroupePolitique(EnsembleDto e) {
        GroupePolitique gp = new GroupePolitique();
        gp.setNom(e.getLibLong());
        gp.setNomCourt(e.getLibCourt());
        gp.setDateDebut(parseDate(e.getDateCreation()));
        gp.setDateFin(parseDate(e.getDateFin()));
        gp.setMotifFin(e.getMotifFin().trim());

        AdressePostale ap = new AdressePostale();
        ap.setVoie(e.getAdresse());
        ap.setCodePostal(e.getCodePostal());
        ap.setVille(e.getVille());
        ap.setNiveauConfidentialite(PUBLIABLE);
        ap.setAdresseDeCorrespondance(true);
        ap.setPublicationAnnuaire(true);
        adressePostaleRepository.save(ap);
        gp.setAdressePostale(ap);

        gp.setFax(e.getFax());
        gp.setMail(e.getMail());
        gp.setPhone(e.getTelephone());

        gp.setImportUid(e.getUidEnsemble());
        // TODO faire les audit trails
        groupePolitiqueRepository.save(gp);
        return gp;
    }

    private Organisme buildOrganisme(EnsembleDto e) {
        Organisme o = new Organisme();
        o.setCodeRNE(e.getCodeRne());
        o.setNom(e.getLibLong());
        o.setSecteur(e.getSecteur());
        o.setDateDebut(parseDate(e.getDateCreation()));
        o.setDateFin(parseDate(e.getDateFin()));
        o.setMotifFin(e.getMotifFin());
        AdressePostale ap = new AdressePostale();
        ap.setVoie(e.getAdresse());
        ap.setCodePostal(e.getCodePostal());
        ap.setVille(e.getVille());
        ap.setNiveauConfidentialite(PUBLIABLE);
        ap.setAdresseDeCorrespondance(true);
        ap.setPublicationAnnuaire(true);
        adressePostaleRepository.save(ap);
        o.setAdressePostale(ap);

        o.setImportUid(e.getUidEnsemble());
        organismeRepository.save(o);
        return o;
    }

    private CommissionThematique buildCommissionThematique(EnsembleDto e) {
        CommissionThematique ct = new CommissionThematique();
        ct.setNom(e.getLibLong());
        ct.setNomCourt(e.getLibCourt());
        ct.setDateDebut(parseDate(e.getDateCreation()));
        ct.setDateFin(parseDate(e.getDateFin()));
        ct.setMotifFin(e.getMotifFin());

        ct.setImportUid(e.getUidEnsemble());
        commissionThematiqueRepository.save(ct);
        return ct;
    }

    private void buildAppartenanceGroupePolitique(MembreDto m, Map<String, Elu> elus, Map<String, GroupePolitique> gps) {
        AppartenanceGroupePolitique agp = new AppartenanceGroupePolitique();
        agp.setElu(elus.get(m.getUidConseiller()));
        agp.setGroupePolitique(gps.get(m.getUidEnsemble()));
        agp.setDateDebut(parseDate(m.getDateDebut()));
        agp.setDateFin(parseDate(m.getDateFin()));
        agp.setMotifFin(m.getMotifFin());
        agp.setImportUid(m.getUidMembre());
        appartenanceGroupePolitiqueRepository.save(agp);
    }

    private void buildAppartenanceCommissionThematique(MembreDto m, Map<String, Elu> elus, Map<String, CommissionThematique> cts) {
        AppartenanceCommissionThematique act = new AppartenanceCommissionThematique();
        act.setElu(elus.get(m.getUidConseiller()));
        act.setCommissionThematique(cts.get(m.getUidEnsemble()));
        act.setDateDebut(parseDate(m.getDateDebut()));
        act.setDateFin(parseDate(m.getDateFin()));
        act.setMotifFin(m.getMotifFin());
        act.setImportUid(m.getUidMembre());
        appartenanceCommissionThematiqueRepository.save(act);
    }

    private void buildAppartenanceOrganisme(MembreDto m, Map<String, Elu> elus, Map<String, Organisme> organismes) {
        Organisme o = organismes.get(m.getUidEnsemble());
        AppartenanceOrganisme ao = new AppartenanceOrganisme();
        ao.setElu(elus.get(m.getUidConseiller()));
        ao.setCodeRNE(o.getCodeRNE());
        ao.setOrganisme(o.getNom());
        ao.setDateNomination(parseDate(m.getDateNomination()));
        ao.setDateDebut(parseDate(m.getDateDebut()));
        ao.setDateFin(parseDate(m.getDateFin()));
        ao.setMotifFin(m.getMotifFin());
        ao.setImportUid(m.getUidMembre());
        appartenanceOrganismeRepository.save(ao);
    }
}
