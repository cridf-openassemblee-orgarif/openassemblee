package openassemblee.web;

import openassemblee.domain.*;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.domain.enumeration.TypeIdentiteInternet;
import openassemblee.publicdata.ConseillerDto;
import openassemblee.publicdata.EnsembleDto;
import openassemblee.publicdata.MembreDto;
import openassemblee.repository.*;
import openassemblee.service.SessionMandatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoField.*;
import static openassemblee.domain.enumeration.NiveauConfidentialite.PUBLIABLE;
import static openassemblee.domain.enumeration.TypeIdentiteInternet.*;
import static openassemblee.service.EluService.getOnlyCurrentMandat;
import static openassemblee.service.EluService.isCurrentMandat;

@RestController
@RequestMapping("/api/publicdata/v1")
public class PublicDataWebservice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
        .appendValue(DAY_OF_MONTH, 2)
        .appendLiteral('/')
        .appendValue(MONTH_OF_YEAR, 2)
        .appendLiteral('/')
        .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
        .toFormatter();

    private static final String NON_RENSEIGNE = "- non renseigné -";
    private static final String MANDATURE = "18";
    private static final String COMMISSION_PERMANENTE = "COM002";
    private static final String EXECUTIF = "COM003";
    private static final String DELEGUES_SPECIAUX = "COM004";

    private static final boolean IS_TEST_IMPORT = true;

    // est obligatoire en fait, champs ne peuvent être null
    private static final String EMPTY_STRING = "";
    private static final boolean USE_SPACE = IS_TEST_IMPORT;
    private static final String SPACE = USE_SPACE ? " " : EMPTY_STRING;
    private static final boolean USE_DOT = IS_TEST_IMPORT;
    private static final String DOT = USE_DOT ? "." : EMPTY_STRING;

    @Autowired
    private EluRepository eluRepository;

    @Autowired
    private GroupePolitiqueRepository groupePolitiqueRepository;

    @Autowired
    private OrganismeRepository organismeRepository;

    @Autowired
    private CommissionThematiqueRepository commissionThematiqueRepository;

    @Autowired
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;

    @Autowired
    private AppartenanceOrganismeRepository appartenanceOrganismeRepository;

    @Autowired
    private FonctionCommissionThematiqueRepository fonctionCommissionThematiqueRepository;

    @Inject
    private SessionMandatureService sessionMandatureService;

    @RequestMapping(value = "/websitedata", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public Map<String, Object> websitedata() {
        // TODO attention pour tous les trucs où on compte, à bien utiliser les trucs filtrés !
        // une solution pour implem simple des demissionnaire est d'utiliser un boolean
        // ou un systeme ou tu dois préciser la data, WS envoie tjs avec la data d'ajd
        Mandature mandature = sessionMandatureService.getMandature();
        List<Elu> elus = eluRepository.findAll()
            .stream()
            .filter(e -> isCurrentMandat(e.getMandats(), mandature))
            .collect(Collectors.toList());
        List<GroupePolitique> groupesPolitiques = groupePolitiqueRepository.findByMandature(mandature);
        List<CommissionThematique> commissionsThematiques = commissionThematiqueRepository.findByMandature(mandature);

        Map<String, Object> result = new HashMap<>();
        result.put("conseillers", getConseillers(elus));

        List<EnsembleDto> ensembles = new ArrayList<>();
        ensembles.addAll(getCommissionPermanente());
        ensembles.addAll(getEnsemblesCommissionsThematiques(commissionsThematiques));
        ensembles.addAll(getEnsemblesGroupesPolitiques(groupesPolitiques));
        result.put("ensembles", ensembles);

        result.put("membres", getMembres(elus));
        return result;
    }

    private List<ConseillerDto> getConseillers(List<Elu> elus) {
        return elus.stream()
            .map(e -> {
                Mandat mandat = getOnlyCurrentMandat(e.getMandats(), sessionMandatureService.getMandature());
                if (mandat == null) {
                    return null;
                }
                ConseillerDto d = new ConseillerDto();
                d.setUidConseiller(e.exportUid());
                d.setMandature(MANDATURE);
                d.setNom(e.getNom());
                d.setActiviteProf(stringOrSpace(null));
                d.setProfession(stringOrSpace(e.getProfession()));
                getPublishable(e.getAdressesPostales()).ifPresent(ap -> {
                    d.setAdresse(stringOrSpace(ap.getVoie()));
                    d.setVille(stringOrSpace(ap.getVille()));
                    d.setCodePostal(stringOrSpace(ap.getCodePostal()));
                });
                d.setDateNaissance(formatDate(e.getDateNaissance()));
                Optional<AppartenanceGroupePolitique> agp = e.getAppartenancesGroupePolitique().stream()
                    .filter(a -> a.getDateFin() == null)
                    .findFirst();
                Optional<FonctionGroupePolitique> fgp = e.getFonctionsGroupePolitique().stream()
                    .filter(a -> a.getDateFin() == null)
                    .findFirst();
                // first set to empty
                d.setGroupeCourt(SPACE);
                d.setGroupePolitique(SPACE);
                agp.ifPresent(g -> {
                    GroupePolitique gp = agp.get().getGroupePolitique();
                    d.setGroupeCourt(gp.getNomCourt());
                    d.setGroupePolitique(gp.getNom());
                });
                if (!agp.isPresent()) {
                    fgp.ifPresent(g -> {
                        GroupePolitique gp = fgp.get().getGroupePolitique();
                        d.setGroupeCourt(gp.getNomCourt());
                        d.setGroupePolitique(gp.getNom());
                    });
                }
                d.setListeCourt(mandat.getListeElectorale().getNomCourt());
                d.setListeElectorale(mandat.getListeElectorale().getNom());
                d.setNbEnfants(EMPTY_STRING);
                d.setNomJeuneFille(EMPTY_STRING);
                d.setSituationFamiliale(EMPTY_STRING);
                d.setTelephone(getPublishable(e.getNumerosTelephones()).map(NumeroTelephone::getNumero).orElse(SPACE));
                d.setFax(getPublishable(e.getNumerosFax()).map(NumeroFax::getNumero).orElse(SPACE));
                d.setMail(getPublishable(e.getAdressesMail()).map(AdresseMail::getMail).orElse(SPACE));
                d.setValid('1');
                d.setDepElection(mandat.getDepartement());
                d.setCodeDepElection(mandat.getCodeDepartement());
                d.setPrenom(e.getPrenom());
                if (e.getCivilite() != null) {
                    d.setCivilite(e.getCivilite() == Civilite.MONSIEUR ? "M." : "Mme");
                }
                d.setVilleNaissance(stringOrSpace(e.getLieuNaissance()));
                d.setDescription(DOT);
                d.setJpegphotoId(String.valueOf(e.getImage()));
                d.setSt(0F);
                // TODO putain de non cohérence entre les membres et ce truc
                StringBuilder commissionsStringBuilder = new StringBuilder();
                e.getAppartenancesCommissionPermanente()
                    .stream()
                    .filter(a -> a.getDateFin() == null)
                    .forEach(a -> commissionsStringBuilder.append("|").append(COMMISSION_PERMANENTE));
//                e.getFonctionsExecutives()
//                    .forEach(a -> commissionsStringBuilder.append("|").append(EXECUTIF));
                e.getFonctionsCommissionPermanente()
                    .stream()
                    .filter(a -> a.getDateFin() == null)
                    .forEach(a -> commissionsStringBuilder.append("|").append(DELEGUES_SPECIAUX));
                e.getAppartenancesCommissionsThematiques()
                    .stream()
                    .filter(a -> a.getDateFin() == null)
                    .filter(a -> a.getCommissionThematique() != null)
//                    .sorted(Comparator.comparing(AppartenanceCommissionThematique::getImportUid))
                    .forEach(a -> commissionsStringBuilder.append("|").append(a.getCommissionThematique().exportUid()));
                d.setCommissions(IS_TEST_IMPORT ? EMPTY_STRING : commissionsStringBuilder.toString());
                d.setDesignations(EMPTY_STRING);

                d.setDistinctions(SPACE);
                if (e.getDistinctionHonorifiques().size() > 0 && !IS_TEST_IMPORT) {
                    d.setDistinctions("|" + String.join("|", e.getDistinctionHonorifiques()
                        .stream()
                        .map(dh -> stringOrEmpty(dh.getTitre()) + "$" + stringOrEmpty(dh.getDate()))
                        .collect(Collectors.toSet())));
                }
                d.setAutresMandats(SPACE);
                if (e.getAutreMandats().size() > 0 && !IS_TEST_IMPORT) {
                    d.setAutresMandats("|" + String.join("|", e.getAutreMandats()
                        .stream()
                        .map(m -> stringOrEmpty(m.getFonction()) + "$"
                            + stringOrEmpty(m.getCollectiviteOuOrganisme()) + "$"
                            + stringOrEmpty(m.getDateDebutString()) + "$")
                        .collect(Collectors.toSet())));
                }

                Optional<FonctionExecutive> fe = e.getFonctionsExecutives().stream()
                    .filter(f -> f.getDateFin() == null)
                    .findFirst();
                d.setFonctionExecutif(stringOrSpace(fe.map(fonctionExecutive -> fonctionExecutive.getFonction()).orElse(null)));
                d.setPhonetique(null);
                d.setTwitter(getUrl(e, Twitter));
                d.setFacebook(getUrl(e, Facebook));
                d.setSiteInternet(getUrl(e, SiteInternet));
                d.setBlog(getUrl(e, Blog));
                d.setAutre(getUrl(e, Autre));

                return d;
            })
            .filter(c -> c != null)
            .collect(Collectors.toList());
    }

    private List<EnsembleDto> getCommissionPermanente() {
        List<EnsembleDto> result = new ArrayList<>();
        EnsembleDto cp = new EnsembleDto();
        cp.setMandature(MANDATURE);
        cp.setUidEnsemble(COMMISSION_PERMANENTE);
        cp.setLibCourt("CP");
        cp.setDateCreation("18/12/2015");
        cp.setType("commission");
        cp.setTypeCommission("Permanente");
        // TODO
        cp.setNbMembre(60L);
        cp.setNbTitulaire(60L);
        cp.setNbSuppleant(0L);
        cp.setLibLong("Commission permanente");
        cp.setSt(1f);
        cp.setValid('1');

        cp.setStatus(stringOrSpace(null));
        cp.setDescription(NON_RENSEIGNE);
        cp.setDateFin(stringOrSpace(null));
        cp.setCodeRne(stringOrSpace(null));
        cp.setDepartement(stringOrSpace(null));
        cp.setMotifFin(stringOrSpace(null));
        cp.setSecteur(stringOrSpace(null));
        cp.setTelephone(stringOrSpace(null));
        cp.setFax(stringOrSpace(null));
        cp.setAdresse(stringOrSpace(null));
        cp.setCodePostal(stringOrSpace(null));
        cp.setVille(stringOrSpace(null));
        cp.setMail(stringOrSpace(null));
        cp.setPhonetique(stringOrSpace(null));

        result.add(cp);

        EnsembleDto exec = new EnsembleDto();
        exec.setMandature(MANDATURE);
        exec.setUidEnsemble(EXECUTIF);
        exec.setLibCourt("VP");
        exec.setDateCreation("18/12/2015");
        exec.setType("commission");
        exec.setTypeCommission("Exécutif");
        // TODO
        exec.setNbMembre(0L);
        exec.setNbTitulaire(16L);
        exec.setNbSuppleant(0L);
        exec.setLibLong("Vice-président(e)s");
        exec.setSt(2f);
        exec.setValid('1');
        exec.setDescription(NON_RENSEIGNE);

        exec.setStatus(stringOrSpace(null));
        exec.setDateFin(stringOrSpace(null));
        exec.setCodeRne(stringOrSpace(null));
        exec.setDepartement(stringOrSpace(null));
        exec.setMotifFin(stringOrSpace(null));
        exec.setSecteur(stringOrSpace(null));
        exec.setTelephone(stringOrSpace(null));
        exec.setFax(stringOrSpace(null));
        exec.setAdresse(stringOrSpace(null));
        exec.setCodePostal(stringOrSpace(null));
        exec.setVille(stringOrSpace(null));
        exec.setMail(stringOrSpace(null));
        exec.setPhonetique(stringOrSpace(null));

        result.add(exec);

        EnsembleDto deleg = new EnsembleDto();
        deleg.setMandature(MANDATURE);
        deleg.setUidEnsemble(DELEGUES_SPECIAUX);
        deleg.setLibCourt("Délégués spéciaux");
        deleg.setDateCreation("22/12/2015");
        deleg.setType("commission");
        deleg.setTypeCommission("Délégués spéciaux");
        // TODO TODO
        deleg.setNbMembre(10L);
        deleg.setNbTitulaire(10L);
        deleg.setNbSuppleant(0L);
        deleg.setLibLong("Délégués spéciaux");
        deleg.setSt(3f);
        deleg.setValid('1');

        deleg.setDescription(NON_RENSEIGNE);
        deleg.setStatus(stringOrSpace(null));
        deleg.setDateFin(stringOrSpace(null));
        deleg.setCodeRne(stringOrSpace(null));
        deleg.setDepartement(stringOrSpace(null));
        deleg.setMotifFin(stringOrSpace(null));
        deleg.setSecteur(stringOrSpace(null));
        deleg.setTelephone(stringOrSpace(null));
        deleg.setFax(stringOrSpace(null));
        deleg.setAdresse(stringOrSpace(null));
        deleg.setCodePostal(stringOrSpace(null));
        deleg.setVille(stringOrSpace(null));
        deleg.setMail(stringOrSpace(null));
        deleg.setPhonetique(stringOrSpace(null));

        result.add(deleg);
        return result;
    }

    private List<EnsembleDto> getEnsemblesGroupesPolitiques(List<GroupePolitique> groupePolitiques) {
        return groupePolitiques.stream().map(gp -> {
            EnsembleDto e = new EnsembleDto();
            e.setUidEnsemble(gp.exportUid());
            e.setMandature(MANDATURE);
            e.setLibCourt(gp.getNomCourt());
            e.setDateCreation(formatDate(gp.getDateDebut()));
            e.setType("groupe politique");
            e.setTypeCommission(stringOrSpace(null));
            e.setNbMembre(gp.getAppartenancesGroupePolitique().stream()
                .filter(g -> g.getDateFin() == null)
                .count());
            e.setNbTitulaire(0L);
            e.setNbSuppleant(0L);
            e.setValid('1');
            e.setLibLong(gp.getNom());
            e.setSt(0F);
            e.setDateFin(formatDate(gp.getDateFin()));
            e.setMotifFin(stringOrSpace(gp.getMotifFin()));
            e.setTelephone(gp.getPhone());

            e.setDescription(NON_RENSEIGNE);
            if (e.getLibLong().equals("Non Inscrits")) {
                e.setDescription(NON_RENSEIGNE);
            }
            e.setCodeRne(stringOrSpace(null));
            e.setDepartement(stringOrSpace(null));
            e.setSecteur(stringOrSpace(null));
            e.setTelephone(stringOrSpace(null));
            e.setFax(stringOrSpace(null));
            e.setAdresse(stringOrSpace(null));
            e.setCodePostal(stringOrSpace(null));
            e.setVille(stringOrSpace(null));
            e.setPhonetique(stringOrSpace(null));

            if (gp.getAdressePostale() != null) {
                e.setAdresse(stringOrSpace(gp.getAdressePostale().getVoie()));
                e.setCodePostal(stringOrSpace(gp.getAdressePostale().getCodePostal()));
                e.setVille(stringOrSpace(gp.getAdressePostale().getVille()));
            }
            e.setStatus(stringOrSpace(null));
            e.setMail(stringOrSpace(gp.getMail()));
            return e;
        }).collect(Collectors.toList());
    }

    private List<EnsembleDto> getEnsemblesCommissionsThematiques(List<CommissionThematique> cts) {
        return cts.stream().map(ct -> {
            EnsembleDto e = new EnsembleDto();
            e.setMandature(MANDATURE);
            e.setUidEnsemble(ct.exportUid());
            e.setLibCourt("");
            e.setDateCreation(formatDate(ct.getDateDebut()));
            e.setType("commission");
            e.setTypeCommission("Thématique");
            e.setNbMembre(appartenanceCommissionThematiqueRepository.findAllByCommissionThematique(ct).stream()
                .filter(a -> a.getDateFin() == null)
                .count());
            e.setNbMembre(e.getNbMembre() + fonctionCommissionThematiqueRepository.findAllByCommissionThematique(ct).stream()
                .filter(a -> a.getDateFin() == null)
                .count());
            e.setNbTitulaire(e.getNbMembre());
            // TODO
            e.setNbMembre(0L);
            e.setNbTitulaire(0L);
            e.setNbSuppleant(0L);
            e.setLibLong(ct.getNom());
            e.setValid('1');
            e.setDescription(NON_RENSEIGNE);
            e.setSt(0f);
            e.setDateFin(formatDate(ct.getDateFin()));
            e.setDescription(NON_RENSEIGNE);
            e.setCodeRne(stringOrSpace(null));
            e.setDepartement(stringOrSpace(null));
            e.setMotifFin(stringOrSpace(ct.getMotifFin()));
            e.setSecteur(stringOrSpace(null));
            e.setTelephone(stringOrSpace(null));
            e.setFax(stringOrSpace(null));
            e.setAdresse(stringOrSpace(null));
            e.setCodePostal(stringOrSpace(null));
            e.setVille(stringOrSpace(null));
            e.setMail(stringOrSpace(null));
            e.setPhonetique(stringOrSpace(null));
            if (ct.getDateFin() != null) {
                e.setStatus("F");
            } else {
                e.setStatus(SPACE);
            }
            return e;
        }).collect(Collectors.toList());
    }

    private List<MembreDto> getMembres(List<Elu> elus) {
        List<MembreDto> m1 = elus.stream().flatMap(e -> e.getAppartenancesCommissionPermanente().stream()
            .filter(a -> a.getDateFin() == null)
            .map(acp -> {
                MembreDto m = new MembreDto();
                m.setUidMembre(acp.exportUid());
                m.setUidEnsemble(COMMISSION_PERMANENTE);
                m.setUidConseiller(e.exportUid());

                m.setMandature(MANDATURE);
                m.setType("Commissions");
                m.setDateDebut(formatDate(acp.getDateDebut()));
                m.setDateFin(formatDate(acp.getDateFin()));
                m.setNumeroNomination(stringOrSpace(null));
                m.setStatus(stringOrSpace(null));
                m.setNomination(stringOrSpace(null));
                m.setSt(0f);
                m.setFonction(stringOrSpace(null));
                m.setBureau('0');
                m.setMotifFin(stringOrSpace(acp.getMotifFin()));
                m.setDateNomination(stringOrSpace(null));
                m.setDescription(stringOrSpace(null));
                return m;
            })).collect(Collectors.toList());
        List<MembreDto> m2 = elus.stream().flatMap(e -> e.getFonctionsExecutives().stream()
            .filter(a -> a.getDateFin() == null)
            .map(fe -> {
                MembreDto m = new MembreDto();
                m.setUidMembre(fe.exportUid());
                m.setUidEnsemble(EXECUTIF);
                m.setUidConseiller(e.exportUid());

                m.setMandature(MANDATURE);
                m.setType("Exécutif");
                m.setDateDebut(formatDate(fe.getDateDebut()));
                m.setDateFin(formatDate(fe.getDateFin()));
                m.setNumeroNomination(stringOrSpace(null));
                m.setStatus("Titulaire");
                m.setNomination(stringOrSpace(null));
                m.setSt(0f);
                m.setFonction(stringOrSpace(fe.getFonction()));
                m.setBureau(bureau(fe.getFonction()));
                m.setMotifFin(stringOrSpace(fe.getMotifFin()));
                m.setDateNomination(stringOrSpace(null));
                m.setDescription(stringOrSpace(null));
                return m;
            })).collect(Collectors.toList());
        List<MembreDto> m3 = elus.stream().flatMap(e -> e.getFonctionsCommissionPermanente().stream()
            .filter(a -> a.getDateFin() == null)
            .filter(fcp -> fcp.getFonction() != null)
            .map(fcp -> {
                MembreDto m = new MembreDto();
                m.setUidMembre(fcp.exportUid());
                // .contains("résident") pour président, vice-président, présidente, avec ou sans maj...
                if (fcp.getFonction().contains("résident")) {
                    m.setUidEnsemble(COMMISSION_PERMANENTE);
                    m.setBureau('0');
                    if (fcp.getFonction().equals("Président") || fcp.getFonction().equals("Présidente"))
                        m.setBureau('1');
                } else {
                    m.setUidEnsemble(DELEGUES_SPECIAUX);
                    m.setBureau(bureau(fcp.getFonction()));
                }
                m.setUidConseiller(e.exportUid());

                m.setMandature(MANDATURE);
                m.setType("Commissions");
                m.setDateDebut(formatDate(fcp.getDateDebut()));
                m.setDateFin(formatDate(fcp.getDateFin()));
                m.setNumeroNomination(stringOrSpace(null));
                m.setStatus(stringOrSpace(null));
                m.setNomination(stringOrSpace(null));
                m.setSt(0f);
                m.setFonction(stringOrSpace(fcp.getFonction()));
                m.setMotifFin(stringOrSpace(fcp.getMotifFin()));
                m.setDateNomination(stringOrSpace(null));
                m.setDescription(stringOrSpace(null));
                return m;
            })).collect(Collectors.toList());
        List<String> fonctionsCT = new ArrayList<>();
        List<MembreDto> m5 = elus.stream().flatMap(e -> e.getFonctionsCommissionsThematiques().stream()
            .filter(a -> a.getDateFin() == null)
            .filter(fct -> fct.getCommissionThematique() != null)
            .map(fct -> {
                fonctionsCT.add(fct.exportUid());
                MembreDto m = new MembreDto();
                m.setUidMembre(fct.exportUid());
                m.setUidEnsemble(fct.getCommissionThematique().exportUid());
                m.setUidConseiller(e.exportUid());

                m.setMandature(MANDATURE);
                m.setType("Commissions");
                m.setDateDebut(formatDate(fct.getDateDebut()));
                m.setDateFin(formatDate(fct.getDateFin()));
                m.setNumeroNomination(stringOrSpace(null));
                m.setStatus(stringOrSpace(null));
                m.setNomination(stringOrSpace(null));
                m.setSt(0f);
                m.setFonction(stringOrSpace(fct.getFonction()));
                m.setBureau(bureau(fct.getFonction()));
                m.setMotifFin(stringOrSpace(fct.getMotifFin()));
                m.setDateNomination(stringOrSpace(null));
                m.setDescription(stringOrSpace(null));
                return m;
            })).collect(Collectors.toList());
        List<MembreDto> m4 = elus.stream().flatMap(e -> e.getAppartenancesCommissionsThematiques().stream()
            .filter(a -> a.getDateFin() == null)
            .filter(act -> !fonctionsCT.contains(act.exportUid()) && act.getCommissionThematique() != null)
            .map(act -> {
                MembreDto m = new MembreDto();
                m.setUidMembre(act.exportUid());
                m.setUidEnsemble(act.getCommissionThematique().exportUid());
                m.setUidConseiller(e.exportUid());

                m.setMandature(MANDATURE);
                m.setType("Commissions");
                m.setDateDebut(formatDate(act.getDateDebut()));
                m.setDateFin(formatDate(act.getDateFin()));
                m.setNumeroNomination(stringOrSpace(null));
                m.setStatus(stringOrSpace(null));
                m.setNomination(stringOrSpace(null));
                m.setSt(0f);
                m.setFonction(stringOrSpace(null));
                m.setBureau('0');
                m.setMotifFin(stringOrSpace(act.getMotifFin()));
                m.setDateNomination(stringOrSpace(null));
                m.setDescription(stringOrSpace(null));
                return m;
            })).collect(Collectors.toList());
        List<String> fonctionsGP = new ArrayList<>();
        List<MembreDto> m7 = elus.stream().flatMap(e -> e.getFonctionsGroupePolitique().stream()
            .filter(a -> a.getDateFin() == null)
            .filter(fgp -> fgp.getGroupePolitique() != null)
            .map(fgp -> {
                fonctionsGP.add(fgp.exportUid());
                MembreDto m = new MembreDto();
                m.setUidMembre(fgp.exportUid());
                m.setUidEnsemble(fgp.getGroupePolitique().exportUid());
                m.setUidConseiller(e.exportUid());

                m.setMandature(MANDATURE);
                m.setType("Groupe politique");
                m.setDateDebut(formatDate(fgp.getDateDebut()));
                m.setDateFin(formatDate(fgp.getDateFin()));
                m.setNumeroNomination(stringOrSpace(null));
                m.setStatus(stringOrSpace(null));
                m.setNomination(stringOrSpace(null));
                m.setSt(0f);
                m.setFonction(stringOrSpace(fgp.getFonction()));
                m.setBureau(bureau(fgp.getFonction()));
                m.setMotifFin(stringOrSpace(fgp.getMotifFin()));
                m.setDateNomination(stringOrSpace(null));
                m.setDescription(stringOrSpace(null));
                return m;
            })).collect(Collectors.toList());
        List<MembreDto> m6 = elus.stream().flatMap(e -> e.getAppartenancesGroupePolitique().stream()
            .filter(a -> a.getDateFin() == null)
            .filter(agp -> !fonctionsGP.contains(agp.exportUid()) && agp.getGroupePolitique() != null)
            .map(agp -> {
                MembreDto m = new MembreDto();
                m.setUidMembre(agp.exportUid());
                m.setUidEnsemble(agp.getGroupePolitique().exportUid());
                m.setUidConseiller(e.exportUid());

                m.setMandature(MANDATURE);
                m.setType("Groupe politique");
                m.setDateDebut(formatDate(agp.getDateDebut()));
                m.setDateFin(formatDate(agp.getDateFin()));
                m.setNumeroNomination(stringOrSpace(null));
                m.setStatus(stringOrSpace(null));
                m.setNomination(stringOrSpace(null));
                m.setSt(0f);
                m.setFonction(stringOrSpace(null));
                m.setBureau('0');
                m.setMotifFin(stringOrSpace(agp.getMotifFin()));
                m.setDateNomination(stringOrSpace(null));
                m.setDescription(stringOrSpace(null));
                return m;
            })).collect(Collectors.toList());
        // TODO code rne en double : "0750708M", "0751451V  "...
//        List<String> doublons = Arrays.asList("0750708M", "0751451V", "0754471C", "0754476H", "0754679D", "0754718W",
//            "0754811X", "0754815B", "0754816C", "0754850P", "0771654E", "0772241T", "0772447S", "0772468P", "0782058N",
//            "0782059P", "0783430E", "0922451P", "0932217E", "0932305A", "0951848T", "0951963T");

        m1.addAll(m2);
        m1.addAll(m3);
        m1.addAll(m4);
        m1.addAll(m5);
        m1.addAll(m6);
        m1.addAll(m7);
        return m1;
    }

    // TODO penser à nettoyer le Publishable apres usage
    private <T extends Publishable> Optional<T> getPublishable(List<T> publishables) {
        return publishables.stream()
            .filter(n -> n.getPublicationAnnuaire() != null && n.getPublicationAnnuaire())
            .filter(n -> n.getNiveauConfidentialite() == PUBLIABLE)
            .findFirst();
    }

    private String getUrl(Elu e, TypeIdentiteInternet typeIdentiteInternet) {
        Optional<IdentiteInternet> ii = e.getIdentitesInternet().stream()
            .filter(i -> i.getTypeIdentiteInternet() == typeIdentiteInternet)
            .findFirst();
        // TODO PDE wtf cet espace
        return ii.map(IdentiteInternet::getUrl).orElse(SPACE);
    }

    private String formatDate(LocalDate date) {
        if (date != null) {
            return date.format(DATE_FORMATTER);
        } else {
            return SPACE;
        }
    }

    // TODO ici checker qu'il n'y a pas d'espace au départ
    private String stringOrSpace(String label) {
        return label != null ? label : SPACE;
    }

    private String stringOrEmpty(String label) {
        return label != null ? label : EMPTY_STRING;
    }

    private char bureau(String fonction) {
        if (fonction == null)
            return '0';
        // TODO une exception ?
        if (fonction.contains("Présidente du Conseil Régional"))
            return '0';
        // TODO une exception ?
        if (fonction.contains("13ème Vice-Président chargé du logement et de la politique de la ville"))
            return '0';
        // TODO une exception ?
        if (fonction.contains("Présidente déléguée"))
            return '0';
        // TODO une exception ?
        if (fonction.contains("9ème Vice-Président chargé de l'action internationale et du tourisme"))
            return '0';
        // .contains("résident") pour président, vice-président, présidente, avec ou sans maj...
        if (fonction.contains("résident"))
            return '1';
        if (fonction.equals("Secrétaire"))
            return '1';
        return '0';
    }

}
