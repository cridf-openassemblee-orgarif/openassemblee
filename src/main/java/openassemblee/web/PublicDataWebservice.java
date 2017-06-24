package openassemblee.web;

import openassemblee.domain.*;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.domain.enumeration.TypeIdentiteInternet;
import openassemblee.publicdata.ConseillerDto;
import openassemblee.publicdata.EnsembleDto;
import openassemblee.publicdata.MembreDto;
import openassemblee.repository.*;
import org.elasticsearch.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static openassemblee.domain.enumeration.NiveauConfidentialite.PUBLIABLE;
import static openassemblee.domain.enumeration.TypeIdentiteInternet.*;

@RestController
@RequestMapping("/api/publicdata/v1")
public class PublicDataWebservice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String NON_RENSEIGNE = "- non renseigné -";
    private static final String MANDATURE = "18";
    public static final String COMMISSION_PERMANENTE = "COM002";
    public static final String EXECUTIF = "COM003";
    public static final String DELEGUES_SPECIAUX = "COM004";

    @Autowired
    private EluRepository eluRepository;

    @Autowired
    private GroupePolitiqueRepository groupePolitiqueRepository;

    @Autowired
    private OrganismeRepository organismeRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CommissionThematiqueRepository commissionThematiqueRepository;

    @Autowired
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;

    @Autowired
    private AppartenanceOrganismeRepository appartenanceOrganismeRepository;

    @RequestMapping(value = "/websitedata", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public Map<String, Object> ensembles() {
        // TODO attention pour tous les trucs où on compte, à bien utiliser les trucs filtrés !
        // une solution pour implem simple des demissionnaire est d'utiliser un boolean
        // ou un systeme ou tu dois préciser la data, WS envoie tjs avec la data d'ajd
        // TODO Solveig confirmation qu'on ne filtre PAS par motif/date fin !? pour autres qu'élu
        // check que par contre les "nombre" sont cohérent avec les demissions
        List<Elu> elus = eluRepository.findAll()
            .stream()
            .filter(e -> Strings.isNullOrEmpty(e.getMotifDemission()) && e.getDateDemission() == null)
            .collect(Collectors.toList());
        List<GroupePolitique> groupesPolitiques = groupePolitiqueRepository.findAll();
        List<Organisme> organismes = organismeRepository.findAll();
        List<CommissionThematique> commissionsThematiques = commissionThematiqueRepository.findAll();

        Map<String, Object> result = new HashMap<>();
        result.put("conseillers", getConseillers(elus));

        List<EnsembleDto> ensembles = new ArrayList<>();
        ensembles.addAll(getCommissionPermanente());
        ensembles.addAll(getEnsemblesCommissionsThematiques(commissionsThematiques));
        ensembles.addAll(getEnsemblesGroupesPolitiques(groupesPolitiques));
        ensembles.addAll(getEnsemblesOrganismes(organismes));
        result.put("ensembles", ensembles);

        result.put("membres", getMembres(elus, organismes));
        return result;
    }

    private List<ConseillerDto> getConseillers(List<Elu> elus) {
        return elus.stream()
            .map(e -> {
                ConseillerDto d = new ConseillerDto();
                d.setUidConseiller(e.getImportUid());
                // TODO Solveig
                d.setMandature(MANDATURE);
                d.setNom(e.getNom());
                d.setActiviteProf(stringOrSpace(null));
                d.setProfession(e.getProfession());
                getPublishable(e.getAdressesPostales()).ifPresent(ap -> {
                    d.setAdresse(ap.getVoie());
                    d.setVille(ap.getVille());
                    d.setCodePostal(ap.getCodePostal());
                });
                d.setDateNaissance(formatDate(e.getDateNaissance()));
                Optional<AppartenanceGroupePolitique> agp = e.getAppartenancesGroupePolitique().stream()
                    .filter(a -> a.getDateFin() == null)
                    .findFirst();
                // first set to empty
                d.setGroupeCourt("");
                d.setGroupePolitique("");
                agp.ifPresent(g -> {
                    GroupePolitique gp = agp.get().getGroupePolitique();
                    if (gp != null) {
                        d.setGroupeCourt(gp.getNomCourt());
                        d.setGroupePolitique(gp.getNom());
                    }
                });
                d.setListeCourt(e.getListeCourt());
                d.setListeElectorale(e.getListeElectorale());
                d.setNbEnfants("");
                d.setNomJeuneFille("");
                d.setAutresMandats("");
                d.setSituationFamiliale("");
                d.setTelephone(getPublishable(e.getNumerosTelephones()).map(NumeroTelephone::getNumero).orElse(""));
                d.setFax(getPublishable(e.getNumerosFax()).map(NumeroFax::getNumero).orElse(""));
                d.setMail(getPublishable(e.getAdressesMail()).map(AdresseMail::getMail).orElse(""));
                d.setValid('1');
                // TODO le naming de notre cote a ptet une lacune
                d.setDepElection(e.getDepartement());
                d.setCodeDepElection(e.getCodeDepartement());
                d.setPrenom(e.getPrenom());
                if (e.getCivilite() != null) {
                    d.setCivilite(e.getCivilite() == Civilite.MONSIEUR ? "M." : "Mme");
                }
                // TODO PDE espace ?
                d.setVilleNaissance(stringOrSpace(e.getLieuNaissance()));
                // TODO PDE gné
                d.setDescription(".");
                d.setJpegphotoId(String.valueOf(e.getImage()));
                // TODO PDE gné
                d.setSt(0F);
                StringBuilder commissionsStringBuilder = new StringBuilder();
                e.getAppartenancesCommissionsThematiques().stream()
                    .filter(a -> a.getDateFin() == null)
                    .forEach(a -> commissionsStringBuilder.append(", ").append(a.getCommissionThematique().getNom()));
                d.setCommissions(commissionsStringBuilder.toString());
                d.setDistinctions("");
                d.setDesignations("");
                Optional<FonctionExecutive> fe = e.getFonctionsExecutives().stream()
                    .filter(f -> f.getDateFin() == null)
                    .findFirst();
                d.setFonctionExecutif(fe.map(fonctionExecutive -> fonctionExecutive.getFonction()).orElse(""));
                d.setPhonetique(null);
                d.setTwitter(getUrl(e, Twitter));
                d.setFacebook(getUrl(e, Facebook));
                d.setSiteInternet(getUrl(e, SiteInternet));
                d.setBlog(getUrl(e, Blog));
                d.setAutre(getUrl(e, Autre));

                return d;
            }).collect(Collectors.toList());
    }

    private List<EnsembleDto> getCommissionPermanente() {
        List<EnsembleDto> result = new ArrayList<>();
        EnsembleDto cp = new EnsembleDto();
        // TODO Solveig
        cp.setMandature(MANDATURE);
        cp.setUidEnsemble(COMMISSION_PERMANENTE);
        cp.setLibCourt("CP");
        cp.setDateCreation("18/12/2015");
        cp.setType("commission");
        cp.setTypeCommission("Permanente");
        // TODO Solveig diff entre les deux ? + suppléants
        cp.setNbMembre(60L);
        cp.setNbTitulaire(60L);
        cp.setNbSuppleant(0L);
        cp.setLibLong("Commission permanente");
        cp.setSt(1f);
        cp.setValid('1');
        cp.setDescription(NON_RENSEIGNE);

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
        // TODO Solveig
        exec.setMandature(MANDATURE);
        exec.setUidEnsemble(EXECUTIF);
        exec.setLibCourt("VP");
        exec.setDateCreation("18/12/2015");
        exec.setType("commission");
        exec.setTypeCommission("Exécutif");
        // TODO Solveig diff entre les deux ? + suppléants
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
        // TODO Solveig
        deleg.setMandature(MANDATURE);
        deleg.setUidEnsemble(DELEGUES_SPECIAUX);
        deleg.setLibCourt("Délégués spéciaux");
        deleg.setDateCreation("22/12/2015");
        deleg.setType("commission");
        deleg.setTypeCommission("Délégués spéciaux");
        // TODO Solveig diff entre les deux ? + suppléants
        deleg.setNbMembre(10L);
        deleg.setNbTitulaire(10L);
        deleg.setNbSuppleant(0L);
        deleg.setLibLong("Délégués spéciaux");
        deleg.setSt(3f);
        deleg.setValid('1');
        deleg.setDescription(NON_RENSEIGNE);

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
            e.setUidEnsemble(gp.getImportUid());
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
                e.setDescription(stringOrSpace(null));
            }
            e.setCodeRne(stringOrSpace(null));
            e.setDepartement(stringOrSpace(null));
            e.setSecteur(stringOrSpace(null));
            e.setTelephone(stringOrSpace(null));
            e.setFax(stringOrSpace(null));
            e.setAdresse(stringOrSpace(null));
            e.setCodePostal(stringOrSpace(null));
            e.setVille(stringOrSpace(null));
            e.setMail(stringOrSpace(null));
            e.setPhonetique(stringOrSpace(null));

            if (gp.getAdressePostale() != null) {
                e.setAdresse(gp.getAdressePostale().getVoie());
                e.setCodePostal(gp.getAdressePostale().getCodePostal());
                e.setVille(gp.getAdressePostale().getVille());
            }
            e.setStatus(stringOrSpace(null));
            e.setMail(gp.getMail());
            return e;
        }).collect(Collectors.toList());
    }

    private List<EnsembleDto> getEnsemblesOrganismes(List<Organisme> organismes) {
        return organismes.stream().map(o -> {
            EnsembleDto e = new EnsembleDto();
            e.setUidEnsemble(o.getImportUid());
            e.setMandature(MANDATURE);
            // TODO Solveig
            e.setLibCourt(o.getSigle());
            e.setDateCreation(formatDate(o.getDateDebut()));
            e.setType("organisme");
            e.setTypeCommission(stringOrSpace(null));
            if (!Strings.isNullOrEmpty(o.getCodeRNE())) {
                List<AppartenanceOrganisme> aos = appartenanceOrganismeRepository.findAllByCodeRNE(o.getCodeRNE())
                    .stream()
                    .filter(ao -> Strings.isNullOrEmpty(ao.getMotifFin()) && ao.getDateFin() == null)
                    .collect(Collectors.toList());
                e.setNbMembre((long) aos.size());
            } else {
                e.setNbMembre(0L);
            }
            e.setNbTitulaire(0L);
            e.setNbSuppleant(0L);
            e.setValid('1');
            e.setLibLong(o.getNom());
            e.setSt(0F);
            e.setDateFin(formatDate(o.getDateFin()));
            e.setDescription(stringOrSpace(o.getDescription()));
            // TODO fou => pas besoin de trimer ??
            // checker qu'effectivement c'est bon putain
            e.setCodeRne(stringOrSpace(o.getCodeRNE()));
            // TODO Solveig
            e.setSecteur(stringOrSpace(o.getSecteur()));
            e.setMotifFin(stringOrSpace(o.getMotifFin()));
            e.setSecteur(o.getSecteur());
            e.setTelephone(o.getTelephone());
            e.setFax(o.getFax());
            e.setPhonetique(o.getPhonetique());
            e.setDepartement(stringOrSpace(o.getDepartement()));
            e.setMail(stringOrSpace(null));
            e.setPhonetique(stringOrSpace(o.getPhonetique()));
            if (o.getAdressePostale() != null) {
                e.setAdresse(o.getAdressePostale().getVoie());
                e.setCodePostal(o.getAdressePostale().getCodePostal());
                e.setVille(o.getAdressePostale().getVille());
            }
            // TODO PDE Solveig ? suppression
            // non cohérence
            e.setStatus(o.getStatus());
            // TODO remove ?
//            if(e.getUidEnsemble().equals("ORG2156")) {
//                e.setStatus("O");
//            }
            return e;
        }).collect(Collectors.toList());
    }

    private List<EnsembleDto> getEnsemblesCommissionsThematiques(List<CommissionThematique> cts) {
        return cts.stream().map(ct -> {
            EnsembleDto e = new EnsembleDto();
            e.setMandature(MANDATURE);
            e.setUidEnsemble(ct.getImportUid());
            e.setLibCourt(ct.getNomCourt());
            e.setDateCreation(formatDate(ct.getDateDebut()));
            e.setType("commission");
            e.setTypeCommission("Thématique");
            long nbMembres = appartenanceCommissionThematiqueRepository.findAllByCommissionThematique(ct).stream()
                .filter(a -> a.getDateFin() == null)
                .count();
            e.setNbMembre(nbMembres);
            e.setNbTitulaire(nbMembres);
            // TODO Solveig on a pas prévu de suppléants
            e.setNbSuppleant(0L);
            e.setLibLong(ct.getNom());
            // TODO Solveig on a pas d'ordre dans Siger
            // on peut mettre en dur dans la base maintenant, mais c'est la merde à la première modification
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
                e.setStatus(" ");
            }
            return e;
        }).collect(Collectors.toList());
    }

    // TODO Solveig pour info, dans l'export actuel, tous les liens entre un elu et un organismes sont par code rne
    // select data_membre.uid_membre from data_membre, data_ensemble where data_membre.uid_ensemble = data_ensemble.uid_ensemble and data_ensemble.code_rne is null;
    private List<MembreDto> getMembres(List<Elu> elus, List<Organisme> organismes) {
        List<MembreDto> m1 = elus.stream().flatMap(e -> e.getAppartenancesCommissionPermanente().stream().map(acp -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(acp.getImportUid());
            m.setUidEnsemble(COMMISSION_PERMANENTE);
            m.setUidConseiller(e.getImportUid());

            m.setMandature("");
            m.setType("Commissions");
            m.setDateDebut(formatDate(acp.getDateDebut()));
            m.setDateFin(formatDate(acp.getDateFin()));
            m.setNumeroNomination("");
            m.setStatus("");
            m.setNomination("");
            m.setSt(0f);
            m.setFonction("");
            m.setBureau('A');
            m.setMotifFin(stringOrSpace(acp.getMotifFin()));
            m.setDateNomination("");
            m.setDescription("");
            return m;
        })).collect(Collectors.toList());
        List<MembreDto> m2 = elus.stream().flatMap(e -> e.getFonctionsExecutives().stream().map(fe -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(fe.getImportUid());
            m.setUidEnsemble(EXECUTIF);
            m.setUidConseiller(e.getImportUid());

            m.setMandature("");
            m.setType("Commissions");
            m.setDateDebut(formatDate(fe.getDateDebut()));
            m.setDateFin(formatDate(fe.getDateFin()));
            m.setNumeroNomination("");
            m.setStatus("");
            m.setNomination("");
            m.setSt(0f);
            m.setFonction("");
            m.setBureau('A');
            m.setMotifFin(stringOrSpace(fe.getMotifFin()));
            m.setDateNomination("");
            m.setDescription("");
            return m;
        })).collect(Collectors.toList());
        List<MembreDto> m3 = elus.stream().flatMap(e -> e.getFonctionsCommissionPermanente().stream().map(fcp -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(fcp.getImportUid());
            m.setUidEnsemble(DELEGUES_SPECIAUX);
            m.setUidConseiller(e.getImportUid());

            m.setMandature("");
            m.setType("Commissions");
            m.setDateDebut(formatDate(fcp.getDateDebut()));
            m.setDateFin(formatDate(fcp.getDateFin()));
            m.setNumeroNomination("");
            m.setStatus("");
            m.setNomination("");
            m.setSt(0f);
            m.setFonction("");
            m.setBureau('A');
            m.setMotifFin(stringOrSpace(fcp.getMotifFin()));
            m.setDateNomination("");
            m.setDescription("");
            return m;
        })).collect(Collectors.toList());
        List<MembreDto> m4 = elus.stream().flatMap(e -> e.getAppartenancesCommissionsThematiques().stream().map(act -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(act.getImportUid());
            m.setUidEnsemble(act.getCommissionThematique().getImportUid());
            m.setUidConseiller(e.getImportUid());

            m.setMandature("");
            m.setType("Commissions");
            m.setDateDebut(formatDate(act.getDateDebut()));
            m.setDateFin(formatDate(act.getDateFin()));
            m.setNumeroNomination("");
            m.setStatus("");
            m.setNomination("");
            m.setSt(0f);
            m.setFonction("");
            m.setBureau('A');
            m.setMotifFin(stringOrSpace(act.getMotifFin()));
            m.setDateNomination("");
            m.setDescription("");
            return m;
        })).collect(Collectors.toList());
        List<MembreDto> m5 = elus.stream().flatMap(e -> e.getFonctionsCommissionsThematiques().stream().map(fct -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(fct.getImportUid());
            m.setUidEnsemble(fct.getCommissionThematique().getImportUid());
            m.setUidConseiller(e.getImportUid());

            m.setMandature("");
            m.setType("Commissions");
            m.setDateDebut(formatDate(fct.getDateDebut()));
            m.setDateFin(formatDate(fct.getDateFin()));
            m.setNumeroNomination("");
            m.setStatus("");
            m.setNomination("");
            m.setSt(0f);
            m.setFonction("");
            m.setBureau('A');
            m.setMotifFin(stringOrSpace(fct.getMotifFin()));
            m.setDateNomination("");
            m.setDescription("");
            return m;
        })).collect(Collectors.toList());
        List<MembreDto> m6 = elus.stream().flatMap(e -> e.getAppartenancesGroupePolitique().stream().map(agp -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(agp.getImportUid());
            m.setUidEnsemble(agp.getGroupePolitique().getImportUid());
            m.setUidConseiller(e.getImportUid());

            m.setMandature("");
            if (agp.getGroupePolitique() != null) {
                m.setUidEnsemble(agp.getGroupePolitique().getImportUid());
            }
            m.setType("Groupe politique");
            m.setDateDebut(formatDate(agp.getDateDebut()));
            m.setDateFin(formatDate(agp.getDateFin()));
            m.setNumeroNomination("");
            m.setStatus("");
            m.setNomination("");
            m.setSt(0f);
            m.setFonction("");
            m.setBureau('A');
            m.setMotifFin(stringOrSpace(agp.getMotifFin()));
            m.setDateNomination("");
            m.setDescription("");
            return m;
        })).collect(Collectors.toList());
        List<MembreDto> m7 = elus.stream().flatMap(e -> e.getFonctionsGroupePolitique().stream().map(fgp -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(fgp.getImportUid());
            m.setUidEnsemble(fgp.getGroupePolitique().getImportUid());
            m.setUidConseiller(e.getImportUid());

            m.setMandature("");
            m.setType("Commissions");
            m.setDateDebut(formatDate(fgp.getDateDebut()));
            m.setDateFin(formatDate(fgp.getDateFin()));
            m.setNumeroNomination("");
            m.setStatus("");
            m.setNomination("");
            m.setSt(0f);
            m.setFonction("");
            m.setBureau('A');
            m.setMotifFin(stringOrSpace(fgp.getMotifFin()));
            m.setDateNomination("");
            m.setDescription("");
            return m;
        })).collect(Collectors.toList());
        // TODO code rne en double : "0750708M", "0751451V  "...
//        List<String> doublons = Arrays.asList("0750708M", "0751451V", "0754471C", "0754476H", "0754679D", "0754718W",
//            "0754811X", "0754815B", "0754816C", "0754850P", "0771654E", "0772241T", "0772447S", "0772468P", "0782058N",
//            "0782059P", "0783430E", "0922451P", "0932217E", "0932305A", "0951848T", "0951963T");
        Map<String, Organisme> organismeMapRNE = organismes.stream()
            .filter(o -> o.getCodeRNE() != null)
            .map(Organisme.UniqueRneOrganisme::new)
//            .filter(o -> !o.getNom().equals("Greta des métiers de l'hôtellerie - lycée jean drouant"))
//            .filter(o -> !o.getNom().equals("Cfa quincaillerie-vente de produits pour l'habitat (vth)"))
            .distinct()
            .map(o -> o.organisme)
            .collect(Collectors.toMap(Organisme::getCodeRNE, Function.identity()));
        Map<String, Organisme> organismeMapNom = organismes.stream()
            .filter(o -> o.getNom() != null)
            .map(Organisme.UniqueNomOrganisme::new)
            .distinct()
            .map(o -> o.organisme)
            .collect(Collectors.toMap(Organisme::getNom, Function.identity()));
        List<MembreDto> m8 = elus.stream().flatMap(e -> e.getAppartenancesOrganismes().stream().map(ao -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(ao.getImportUid());
            if (ao.getCodeRNE() != null) {
                m.setUidEnsemble(organismeMapRNE.get(ao.getCodeRNE()).getImportUid());
            } else {
                Organisme o = organismeMapNom.get(ao.getOrganisme());
                if (o != null) {
                    m.setUidEnsemble(o.getImportUid());
                } else {
                    logger.info("Unknown organisme " + ao.getOrganisme());
                }
            }
            m.setUidConseiller(e.getImportUid());

            m.setMandature("");
            m.setType("Organisme");
            m.setDateDebut(formatDate(ao.getDateDebut()));
            m.setDateFin(formatDate(ao.getDateFin()));
            m.setNumeroNomination("");
            m.setStatus("");
            m.setNomination("");
            m.setSt(0f);
            m.setFonction("");
            m.setBureau('A');
            m.setMotifFin(stringOrSpace(ao.getMotifFin()));
            m.setDateNomination("");
            m.setDescription("");
            return m;
        })).collect(Collectors.toList());

        m1.addAll(m2);
        m1.addAll(m3);
        m1.addAll(m4);
        m1.addAll(m5);
        m1.addAll(m6);
        m1.addAll(m7);
        m1.addAll(m8);
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
        return ii.map(IdentiteInternet::getUrl).orElse(" ");
    }

    private String formatDate(LocalDate date) {
        if (date != null) {
            return date.format(InjectDataWebservice.DATE_FORMATTER);
        } else {
            return " ";
        }
    }

    private String stringOrSpace(String label) {
        return label != null ? label : " ";
    }

}
