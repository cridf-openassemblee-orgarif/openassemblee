package openassemblee.web;

import openassemblee.domain.*;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.domain.enumeration.TypeIdentiteInternet;
import openassemblee.publicdata.*;
import openassemblee.repository.*;
import openassemblee.service.AuditTrailService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @Autowired
    private AppartenanceCommissionPermanenteRepository appartenanceCommissionPermanenteRepository;
    @Autowired
    private FonctionExecutiveRepository fonctionExecutiveRepository;
    @Autowired
    private FonctionCommissionPermanenteRepository fonctionCommissionPermanenteRepository;
    @Autowired
    private FonctionGroupePolitiqueRepository fonctionGroupePolitiqueRepository;
    @Autowired
    private FonctionCommissionThematiqueRepository fonctionCommissionThematiqueRepository;
    @Autowired
    private AutreMandatRepository autreMandatRepository;
    @Autowired
    private DistinctionHonorifiqueRepository distinctionHonorifiqueRepository;
    @Autowired
    private AuditTrailService auditTrailService;

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String ping() {
        return "ping";
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @Transactional
    public void data(@RequestBody DataBag data) {
        autreMandatRepository.deleteAll();
        distinctionHonorifiqueRepository.deleteAll();
        identiteInternetRepository.deleteAll();
        organismeRepository.deleteAll();
        commissionThematiqueRepository.deleteAll();
        appartenanceOrganismeRepository.deleteAll();
        appartenanceCommissionThematiqueRepository.deleteAll();
        appartenanceGroupePolitiqueRepository.deleteAll();
        appartenanceCommissionPermanenteRepository.deleteAll();
        fonctionCommissionPermanenteRepository.deleteAll();
        fonctionExecutiveRepository.deleteAll();
        fonctionGroupePolitiqueRepository.deleteAll();
        fonctionCommissionThematiqueRepository.deleteAll();
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
        String cp = data.getEnsembles().stream()
            .filter(e -> e.getType().equals("commission"))
            .filter(e -> e.getTypeCommission().equals("Permanente"))
            .findAny().get().getUidEnsemble();

        // appartenances executif
//        String executif = data.getEnsembles().stream()
//            .filter(e -> e.getType().equals("commission"))
//            .filter(e -> e.getTypeCommission().equals("Exécutif"))
//            .findAny().get().getUidEnsemble();

        String deleguesSpeciaux = data.getEnsembles().stream()
            .filter(e -> e.getType().equals("commission"))
            .filter(e -> e.getTypeCommission().equals("Délégués spéciaux"))
            .findAny().get().getUidEnsemble();

        List<String> uidsTraites = new ArrayList<>();
        // appartenances commission permanent
        List<MembreDto> m1 = data.getMembres().stream()
            .filter(m -> m.getType().equals("Commissions"))
            .filter(m -> m.getUidEnsemble().equals(cp))
            .filter(m -> trimToNull(m.getFonction()) == null)
            .collect(Collectors.toList());
        uidsTraites.addAll(m1.stream().map(MembreDto::getUidMembre).collect(Collectors.toList()));
        long nbCp = m1.stream()
            .map(m -> buildAppartenanceCommissionPermanente(m, elus))
            .count();
        System.out.println("Commission permanente membres : " + nbCp);

        // fonctions executives
        List<MembreDto> m2 = data.getMembres().stream()
            .filter(m -> m.getType().equals("Exécutif"))
            .collect(Collectors.toList());
        uidsTraites.addAll(m2.stream().map(MembreDto::getUidMembre).collect(Collectors.toList()));
        long nbE = m2.stream()
            .map(m -> buildFonctionExecutive(m, elus))
            .count();
        System.out.println("Exécutif : " + nbE);

        // fonctions CP
        List<MembreDto> m3 = data.getMembres().stream()
            .filter(m -> m.getType().equals("Commissions"))
            .filter(m -> m.getUidEnsemble().equals(deleguesSpeciaux))
            .collect(Collectors.toList());
        uidsTraites.addAll(m3.stream().map(MembreDto::getUidMembre).collect(Collectors.toList()));
        long nbFcp = m3.stream()
            .map(m -> buildFonctionCommissionPermanente(m, elus))
            .count();
        System.out.println("Fonctions CP : " + nbFcp);

        // fonctions CP présidents etc
        List<MembreDto> m3bis = data.getMembres().stream()
            .filter(m -> m.getType().equals("Commissions"))
            .filter(m -> m.getUidEnsemble().equals(cp))
            .filter(m -> trimToNull(m.getFonction()) != null)
            .collect(Collectors.toList());
        uidsTraites.addAll(m3bis.stream().map(MembreDto::getUidMembre).collect(Collectors.toList()));
        long nbFcpBis = m3bis.stream()
            .map(m -> buildFonctionCommissionPermanente(m, elus))
            .count();
        System.out.println("Fonctions CP (présidents) : " + nbFcpBis);

        // appartenances groupe politique
        List<MembreDto> m4 = data.getMembres().stream()
            .filter(m -> m.getType().equals("Groupe politique"))
//            .filter(m -> trimToNull(m.getFonction()) == null)
            .collect(Collectors.toList());
        uidsTraites.addAll(m4.stream().map(MembreDto::getUidMembre).collect(Collectors.toList()));
        long nbAgp = m4.stream()
            .map(m -> buildAppartenanceGroupePolitique(m, elus, gps))
            .count();
        System.out.println("Appartenances GP : " + nbAgp);

        // fonctions groupe politique
        List<MembreDto> m5 = data.getMembres().stream()
            .filter(m -> m.getType().equals("Groupe politique"))
            .filter(m -> trimToNull(m.getFonction()) != null)
            .collect(Collectors.toList());
        uidsTraites.addAll(m5.stream().map(MembreDto::getUidMembre).collect(Collectors.toList()));
        long nbFgp = m5.stream()
            .map(m -> buildFonctionGroupePolitigue(m, elus, gps))
            .count();
        System.out.println("Fonctions GP : " + nbFgp);

        assert data.getMembres().stream()
            .filter(m -> m.getType().equals("Groupe politique"))
            .count() == m4.size() + m5.size();

        // appartenances commission thématique
        List<MembreDto> m6 = data.getMembres().stream()
            .filter(m -> m.getType().equals("Commissions"))
            .filter(m -> cts.containsKey(m.getUidEnsemble()))
//            .filter(m -> trimToNull(m.getFonction()) == null)
            .collect(Collectors.toList());
        uidsTraites.addAll(m6.stream().map(MembreDto::getUidMembre).collect(Collectors.toList()));
        long nbAct = m6.stream()
            .map(m -> buildAppartenanceCommissionThematique(m, elus, cts))
            .count();
        System.out.println("Appartenances CT : " + nbAct);

        // fonctions commission thématique
        List<MembreDto> m7 = data.getMembres().stream()
            .filter(m -> m.getType().equals("Commissions"))
            .filter(m -> cts.containsKey(m.getUidEnsemble()))
            .filter(m -> trimToNull(m.getFonction()) != null)
            .collect(Collectors.toList());
        uidsTraites.addAll(m7.stream().map(MembreDto::getUidMembre).collect(Collectors.toList()));
        long nbFct = m7.stream()
            .map(m -> buildFonctionCommissionThematique(m, elus, cts))
            .count();
        System.out.println("Fonctions CT : " + nbFct);

        assert data.getMembres().stream()
            .filter(m -> m.getType().equals("Commissions"))
            .filter(m -> cts.containsKey(m.getUidEnsemble()))
            .count() == m4.size() + m5.size();

        // appartenances organismes
        List<MembreDto> m8 = data.getMembres().stream()
            .filter(m -> m.getType().equals("Organisme"))
            .collect(Collectors.toList());
        uidsTraites.addAll(m8.stream().map(MembreDto::getUidMembre).collect(Collectors.toList()));
        long nbAo = m8.stream()
            .map(m -> buildAppartenanceOrganisme(m, elus, organismes))
            .count();
        System.out.println("Appartenances organismes : " + nbAo);

//        List<String> manquants = data.getMembres().stream()
//            .map(MembreDto::getUidMembre)
//            .filter(id -> !uidsTraites.contains(id))
//            .collect(Collectors.toList());

        System.out.println("Membres : " + data.getMembres().size());
//        System.out.println("Membres reconstitués : " + uidsTraites.size());
//        System.out.println(manquants);
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
                auditTrailService.logUpdate(elu, elu.getId());
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
        elu.setNomJeuneFille(trimOrNull(c.getNomJeuneFille()));
        elu.setProfession(trimOrNull(c.getProfession()));
        elu.setDateNaissance(parseDate(c.getDateNaissance()));
        elu.setLieuNaissance(trimOrNull(c.getVilleNaissance()));
        elu.setListeCourt(trimOrNull(c.getListeCourt()));
        elu.setListeElectorale(trimOrNull(c.getListeElectorale()));
        elu.setDepartement(trimOrNull(c.getDepElection()));
        elu.setCodeDepartement(trimOrNull(c.getCodeDepElection()));

        AdressePostale ap = new AdressePostale();
        ap.setVoie(trimOrNull(c.getAdresse()));
        ap.setCodePostal(trimOrNull(c.getCodePostal()));
        ap.setVille(trimOrNull(c.getVille()));
        ap.setNiveauConfidentialite(PUBLIABLE);
        ap.setAdresseDeCorrespondance(true);
        ap.setPublicationAnnuaire(true);
        adressePostaleRepository.save(ap);
        auditTrailService.logCreation(ap, ap.getId());
        elu.getAdressesPostales().add(ap);

        if (trimOrNull(c.getTelephone()) != null) {
            NumeroTelephone nt = new NumeroTelephone();
            nt.setNumero(trimOrNull(c.getTelephone()));
            nt.setNiveauConfidentialite(PUBLIABLE);
            nt.setPublicationAnnuaire(true);
            numeroTelephoneRepository.save(nt);
            auditTrailService.logCreation(nt, nt.getId());
            elu.getNumerosTelephones().add(nt);
        }

        if (trimOrNull(c.getFax()) != null) {
            NumeroFax nf = new NumeroFax();
            nf.setNumero(trimOrNull(c.getFax()));
            nf.setNiveauConfidentialite(PUBLIABLE);
            nf.setPublicationAnnuaire(true);
            numeroFaxRepository.save(nf);
            auditTrailService.logCreation(nf, nf.getId());
            elu.getNumerosFax().add(nf);
        }

        AdresseMail am = new AdresseMail();
        am.setMail(c.getMail());
        am.setAdresseDeCorrespondance(true);
        am.setNiveauConfidentialite(PUBLIABLE);
        am.setPublicationAnnuaire(true);
        adresseMailRepository.save(am);
        auditTrailService.logCreation(am, am.getId());
        elu.getAdressesMail().add(am);
        if (trimToNull(c.getTwitter()) != null) {
            IdentiteInternet ii = new IdentiteInternet();
            ii.setTypeIdentiteInternet(TypeIdentiteInternet.Twitter);
            ii.setUrl(trimOrNull(c.getTwitter()));
            identiteInternetRepository.save(ii);
            auditTrailService.logCreation(ii, ii.getId());
            elu.getIdentitesInternet().add(ii);
        }
        if (trimToNull(c.getFacebook()) != null) {
            IdentiteInternet ii = new IdentiteInternet();
            ii.setTypeIdentiteInternet(TypeIdentiteInternet.Facebook);
            ii.setUrl(trimOrNull(c.getFacebook()));
            identiteInternetRepository.save(ii);
            auditTrailService.logCreation(ii, ii.getId());
            elu.getIdentitesInternet().add(ii);
        }
        if (trimToNull(c.getSiteInternet()) != null) {
            IdentiteInternet ii = new IdentiteInternet();
            ii.setTypeIdentiteInternet(TypeIdentiteInternet.SiteInternet);
            ii.setUrl(trimOrNull(c.getSiteInternet()));
            identiteInternetRepository.save(ii);
            auditTrailService.logCreation(ii, ii.getId());
            elu.getIdentitesInternet().add(ii);
        }
        if (trimToNull(c.getBlog()) != null) {
            IdentiteInternet ii = new IdentiteInternet();
            ii.setTypeIdentiteInternet(TypeIdentiteInternet.Blog);
            ii.setUrl(trimOrNull(c.getBlog()));
            identiteInternetRepository.save(ii);
            auditTrailService.logCreation(ii, ii.getId());
            elu.getIdentitesInternet().add(ii);
        }
        if (trimToNull(c.getAutre()) != null) {
            IdentiteInternet ii = new IdentiteInternet();
            ii.setTypeIdentiteInternet(TypeIdentiteInternet.Autre);
            ii.setUrl(trimOrNull(c.getAutre()));
            identiteInternetRepository.save(ii);
            auditTrailService.logCreation(ii, ii.getId());
            elu.getIdentitesInternet().add(ii);
        }

        elu.setImportUid(c.getUidConseiller());
        eluRepository.save(elu);
        auditTrailService.logCreation(elu, elu.getId());

        List<List<String>> autresMandats = parseItems(c.getAutresMandats(), 4);
        List<List<String>> distinctions = parseItems(c.getDistinctions(), 2);
        autresMandats.forEach(mandat -> {
            // format étonnant, du coup
            if (mandat.size() > 3) {
                System.out.println(mandat.get(3));
            }
        });
        if (autresMandats.size() > 0) {
            autresMandats.stream()
                .map(mandat -> new AutreMandat(getFromListOrNull(mandat, 1), getFromListOrNull(mandat, 0), getFromListOrNull(mandat, 2), elu))
                .forEach(mandat -> {
                    autreMandatRepository.save(mandat);
                    auditTrailService.logCreation(mandat, mandat.getId());
                });
        }
        if (distinctions.size() > 0) {
            distinctions.stream()
                .map(d -> new DistinctionHonorifique(getFromListOrNull(d, 0), getFromListOrNull(d, 1), elu))
                .forEach(d -> {
                    distinctionHonorifiqueRepository.save(d);
                    auditTrailService.logCreation(d, d.getId());
                });
        }

        return elu;
    }

    private static List<List<String>> parseItems(String asString, int fieldsNumber) {
        asString = asString.trim();
        if (asString.equals("")) {
            return new ArrayList<>();
        }
        int itemsNumber = StringUtils.countMatches(asString, "|");
        int subItemsNumber = StringUtils.countMatches(asString, "$");
        if (subItemsNumber != itemsNumber * (fieldsNumber - 1)) {
            throw new IllegalArgumentException("pb de parseItems " + itemsNumber + " parties et " + subItemsNumber + " : " + asString);
        }
        return Arrays.asList(asString.split("\\|"))
            .stream()
            .filter(s -> !s.equals(""))
            .map(s -> Arrays.asList(s.split("\\$")))
            .collect(Collectors.toList());
    }

    private String getFromListOrNull(List<String> list, int item) {
        if (list.size() > item) {
            return list.get(item);
        }
        return null;
    }

    private LocalDate parseDate(String date) {
        if (trimToNull(date) != null) {
            return LocalDate.parse(date, DATE_FORMATTER);
        } else {
            return null;
        }
    }

    private GroupePolitique buildGroupePolitique(EnsembleDto e) {
        GroupePolitique gp = new GroupePolitique();
        gp.setNom(trimOrNull(e.getLibLong()));
        gp.setNomCourt(trimOrNull(e.getLibCourt()));
        gp.setDateDebut(parseDate(e.getDateCreation()));
        gp.setDateFin(parseDate(e.getDateFin()));
        gp.setMotifFin(trimOrNull(e.getMotifFin()));

        if (trimOrNull(e.getAdresse()) != null || trimOrNull(e.getVille()) != null || trimOrNull(e.getCodePostal()) != null) {
            AdressePostale ap = new AdressePostale();
            ap.setVoie(trimOrNull(e.getAdresse()));
            ap.setCodePostal(trimOrNull(e.getCodePostal()));
            ap.setVille(trimOrNull(e.getVille()));
            ap.setNiveauConfidentialite(PUBLIABLE);
            ap.setAdresseDeCorrespondance(true);
            ap.setPublicationAnnuaire(true);
            adressePostaleRepository.save(ap);
            auditTrailService.logCreation(ap, ap.getId());
            gp.setAdressePostale(ap);
        }

        gp.setFax(trimOrNull(e.getFax()));
        gp.setMail(trimOrNull(e.getMail()));
        gp.setPhone(trimOrNull(e.getTelephone()));

        gp.setImportUid(e.getUidEnsemble());
        // TODO faire les audit trails
        groupePolitiqueRepository.save(gp);
        auditTrailService.logCreation(gp, gp.getId());
        return gp;
    }

    private Organisme buildOrganisme(EnsembleDto e) {
        Organisme o = new Organisme();
        o.setCodeRNE(trimOrNull(e.getCodeRne()));
        o.setSigle(trimOrNull(e.getLibCourt()));
        o.setNom(trimOrNull(e.getLibLong()));
        // TODO type vs secteur
        o.setSecteur(trimOrNull(e.getSecteur()));
        o.setDateDebut(parseDate(e.getDateCreation()));
        o.setDateFin(parseDate(e.getDateFin()));
        o.setMotifFin(trimOrNull(e.getMotifFin()));
        o.setTelephone(trimOrNull(e.getTelephone()));
        o.setFax(trimOrNull(e.getFax()));
        o.setPhonetique(trimOrNull(e.getPhonetique()));
        o.setDepartement(trimOrNull(e.getDepartement()));
        o.setDescription(trimOrNull(e.getDescription()));
        o.setStatus(trimOrNull(e.getStatus()));

        AdressePostale ap = new AdressePostale();
        ap.setVoie(trimOrNull(e.getAdresse()));
        ap.setCodePostal(trimOrNull(e.getCodePostal()));
        ap.setVille(trimOrNull(e.getVille()));
        ap.setNiveauConfidentialite(PUBLIABLE);
        ap.setAdresseDeCorrespondance(true);
        ap.setPublicationAnnuaire(true);
        adressePostaleRepository.save(ap);
        auditTrailService.logCreation(ap, ap.getId());
        o.setAdressePostale(ap);

        o.setImportUid(e.getUidEnsemble());
        organismeRepository.save(o);
        auditTrailService.logCreation(o, o.getId());
        return o;
    }

    private CommissionThematique buildCommissionThematique(EnsembleDto e) {
        CommissionThematique ct = new CommissionThematique();
        ct.setNom(e.getLibLong());
        ct.setNomCourt(e.getLibCourt());
        ct.setDateDebut(parseDate(e.getDateCreation()));
        ct.setDateFin(parseDate(e.getDateFin()));
        ct.setMotifFin(trimOrNull(e.getMotifFin()));

        ct.setImportUid(e.getUidEnsemble());
        commissionThematiqueRepository.save(ct);
        auditTrailService.logCreation(ct, ct.getId());
        return ct;
    }

    private AppartenanceCommissionPermanente buildAppartenanceCommissionPermanente(MembreDto m, Map<String, Elu> elus) {
        AppartenanceCommissionPermanente acp = new AppartenanceCommissionPermanente();
        acp.setElu(elus.get(m.getUidConseiller()));
        acp.setDateDebut(parseDate(m.getDateDebut()));
        acp.setDateFin(parseDate(m.getDateFin()));
        acp.setMotifFin(trimOrNull(m.getMotifFin()));
        acp.setImportUid(m.getUidMembre());
        appartenanceCommissionPermanenteRepository.save(acp);
        auditTrailService.logCreation(acp, acp.getId());
        return acp;
    }

    private FonctionExecutive buildFonctionExecutive(MembreDto m, Map<String, Elu> elus) {
        FonctionExecutive fe = new FonctionExecutive();
        fe.setElu(elus.get(m.getUidConseiller()));
        fe.setDateDebut(parseDate(m.getDateDebut()));
        fe.setDateFin(parseDate(m.getDateFin()));
        fe.setMotifFin(trimOrNull(m.getMotifFin()));
        fe.setImportUid(m.getUidMembre());
        fe.setFonction(m.getFonction());
        fonctionExecutiveRepository.save(fe);
        auditTrailService.logCreation(fe, fe.getId());
        return fe;
    }

    private FonctionCommissionPermanente buildFonctionCommissionPermanente(MembreDto m, Map<String, Elu> elus) {
        FonctionCommissionPermanente fcp = new FonctionCommissionPermanente();
        fcp.setElu(elus.get(m.getUidConseiller()));
        fcp.setDateDebut(parseDate(m.getDateDebut()));
        fcp.setDateFin(parseDate(m.getDateFin()));
        fcp.setMotifFin(trimOrNull(m.getMotifFin()));
        fcp.setImportUid(m.getUidMembre());
        fcp.setFonction(m.getFonction());
        fonctionCommissionPermanenteRepository.save(fcp);
        auditTrailService.logCreation(fcp, fcp.getId());
        return fcp;
    }

    private AppartenanceGroupePolitique buildAppartenanceGroupePolitique(MembreDto m, Map<String, Elu> elus, Map<String, GroupePolitique> gps) {
        AppartenanceGroupePolitique agp = new AppartenanceGroupePolitique();
        agp.setElu(elus.get(m.getUidConseiller()));
        agp.setGroupePolitique(gps.get(m.getUidEnsemble()));
        agp.setDateDebut(parseDate(m.getDateDebut()));
        agp.setDateFin(parseDate(m.getDateFin()));
        agp.setMotifFin(trimOrNull(m.getMotifFin()));
        agp.setImportUid(m.getUidMembre());
        appartenanceGroupePolitiqueRepository.save(agp);
        auditTrailService.logCreation(agp, agp.getId());
        return agp;
    }

    private FonctionGroupePolitique buildFonctionGroupePolitigue(MembreDto m, Map<String, Elu> elus, Map<String, GroupePolitique> gps) {
        FonctionGroupePolitique fgp = new FonctionGroupePolitique();
        fgp.setElu(elus.get(m.getUidConseiller()));
        fgp.setGroupePolitique(gps.get(m.getUidEnsemble()));
        fgp.setDateDebut(parseDate(m.getDateDebut()));
        fgp.setDateFin(parseDate(m.getDateFin()));
        fgp.setMotifFin(trimOrNull(m.getMotifFin()));
        fgp.setImportUid(m.getUidMembre());
        fgp.setFonction(m.getFonction());
        fonctionGroupePolitiqueRepository.save(fgp);
        auditTrailService.logCreation(fgp, fgp.getId());
        return fgp;
    }

    private AppartenanceCommissionThematique buildAppartenanceCommissionThematique(MembreDto m, Map<String, Elu> elus, Map<String, CommissionThematique> cts) {
        AppartenanceCommissionThematique act = new AppartenanceCommissionThematique();
        act.setElu(elus.get(m.getUidConseiller()));
        act.setCommissionThematique(cts.get(m.getUidEnsemble()));
        act.setDateDebut(parseDate(m.getDateDebut()));
        act.setDateFin(parseDate(m.getDateFin()));
        act.setMotifFin(trimOrNull(m.getMotifFin()));
        act.setImportUid(m.getUidMembre());
        appartenanceCommissionThematiqueRepository.save(act);
        auditTrailService.logCreation(act, act.getId());
        return act;
    }

    private FonctionCommissionThematique buildFonctionCommissionThematique(MembreDto m, Map<String, Elu> elus, Map<String, CommissionThematique> cts) {
        FonctionCommissionThematique fct = new FonctionCommissionThematique();
        fct.setElu(elus.get(m.getUidConseiller()));
        fct.setCommissionThematique(cts.get(m.getUidEnsemble()));
        fct.setDateDebut(parseDate(m.getDateDebut()));
        fct.setDateFin(parseDate(m.getDateFin()));
        fct.setMotifFin(trimOrNull(m.getMotifFin()));
        fct.setImportUid(m.getUidMembre());
        fct.setFonction(trimOrNull(m.getFonction()));
        fonctionCommissionThematiqueRepository.save(fct);
        auditTrailService.logCreation(fct, fct.getId());
        return fct;
    }

    private AppartenanceOrganisme buildAppartenanceOrganisme(MembreDto m, Map<String, Elu> elus, Map<String, Organisme> organismes) {
        Organisme o = organismes.get(m.getUidEnsemble());
        AppartenanceOrganisme ao = new AppartenanceOrganisme();
        ao.setElu(elus.get(m.getUidConseiller()));
        ao.setCodeRNE(trimOrNull(o.getCodeRNE()));
        ao.setOrganisme(trimOrNull(o.getNom()));
        ao.setDateNomination(parseDate(m.getDateNomination()));
        ao.setDateDebut(parseDate(m.getDateDebut()));
        ao.setDateFin(parseDate(m.getDateFin()));
        ao.setMotifFin(trimOrNull(m.getMotifFin()));
        ao.setImportUid(m.getUidMembre());

        ao.setFonction(trimOrNull(m.getFonction()));

        // TODO Solveig reference == numero nomination ?
        ao.setReference(trimOrNull(m.getNumeroNomination()));
        ao.setStatut(trimOrNull(m.getStatus()));
        // TODO Solveig type == nomination ?
        ao.setType(trimOrNull(m.getNomination()));
        ao.setDateNomination(parseDate(m.getDateNomination()));
        appartenanceOrganismeRepository.save(ao);
        auditTrailService.logCreation(ao, ao.getId());
        return ao;
    }

    private String trimOrNull(String label) {
        return label != null && !label.trim().equals("") ? label : null;
    }

    private String trimToNull(String label) {
        if (label == null) {
            return null;
        }
        String trim = label.trim();
        if (trim.equals("")) {
            return null;
        }
        return trim;
    }
}
