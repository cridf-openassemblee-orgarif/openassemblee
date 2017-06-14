package openassemblee.web;

import openassemblee.domain.*;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.domain.enumeration.TypeIdentiteInternet;
import openassemblee.publicdata.ConseillerDto;
import openassemblee.publicdata.EnsembleDto;
import openassemblee.publicdata.MembreDto;
import openassemblee.repository.*;
import org.elasticsearch.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static openassemblee.domain.enumeration.NiveauConfidentialite.PUBLIABLE;
import static openassemblee.domain.enumeration.TypeIdentiteInternet.*;

@RestController
@RequestMapping("/api/publicdata/v1")
public class PublicDataWebservice {

    private static final String NON_RENSEIGNE = "- non renseigné -";

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
//            .stream()
//            .filter(gp -> Strings.isNullOrEmpty(gp.getMotifFin()) && gp.getDateFin() == null)
//            .collect(Collectors.toList());
        List<Organisme> organismes = organismeRepository.findAll();
//            .stream()
//            .filter(o -> Strings.isNullOrEmpty(o.getMotifFin()) && o.getDateFin() == null)
//            .collect(Collectors.toList());
        List<CommissionThematique> commissionsThematiques = commissionThematiqueRepository.findAll();
//            .stream()
//            .filter(o -> Strings.isNullOrEmpty(o.getMotifFin()) && o.getDateFin() == null)
//            .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("conseillers", getConseillers(elus));

        List<EnsembleDto> ensembles = new ArrayList<>();
        ensembles.addAll(getCommissionPermanente());
        ensembles.addAll(getEnsemblesCommissionsThematiques(commissionsThematiques));
        ensembles.addAll(getEnsemblesGroupesPolitiques(groupesPolitiques));
        ensembles.addAll(getEnsemblesOrganismes(organismes));
        result.put("ensembles", ensembles);

        result.put("membres", getMembres(elus));
        return result;
    }

    private List<ConseillerDto> getConseillers(List<Elu> elus) {
        return elus.stream()
            .map(e -> {
                ConseillerDto d = new ConseillerDto();
                d.setUidConseiller(e.getImportUid());
                // TODO Solveig
                d.setMandature("18");
                d.setNom(e.getNom());
                // TODO PDE espace ?
                d.setActiviteProf(" ");
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
                d.setVilleNaissance(!Strings.isNullOrEmpty(e.getLieuNaissance()) ? e.getLieuNaissance() : " ");
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
        cp.setMandature("18");
        cp.setUidEnsemble("COM002");
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
        result.add(cp);
        EnsembleDto exec = new EnsembleDto();
        // TODO Solveig
        exec.setMandature("18");
        exec.setUidEnsemble("COM003");
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
        result.add(exec);
        EnsembleDto deleg = new EnsembleDto();
        // TODO Solveig
        deleg.setMandature("18");
        deleg.setUidEnsemble("COM004");
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
        result.add(deleg);
        return result;
    }

    private List<EnsembleDto> getEnsemblesGroupesPolitiques(List<GroupePolitique> groupePolitiques) {
        return groupePolitiques.stream().map(gp -> {
            EnsembleDto e = new EnsembleDto();
            e.setUidEnsemble(gp.getImportUid());
            e.setMandature("");
            e.setLibCourt(gp.getNomCourt());
            e.setDateCreation(formatDate(gp.getDateDebut()));
            e.setType("groupe politique");
            e.setTypeCommission("");
            e.setNbMembre(gp.getAppartenancesGroupePolitique().stream()
                .filter(g -> g.getDateFin() == null)
                .count());
            e.setNbTitulaire(0L);
            e.setNbSuppleant(0L);
            e.setValid('O');
            e.setLibLong(gp.getNom());
            e.setSt(0F);
            e.setDateFin(formatDate(gp.getDateFin()));
            e.setDescription("");
            e.setCodeRne("");
            e.setDepartement("");
            e.setMotifFin(gp.getMotifFin().trim());
            e.setSecteur("");
            e.setTelephone(gp.getPhone());
            e.setFax(gp.getFax());
            if (gp.getAdressePostale() != null) {
                e.setAdresse(gp.getAdressePostale().getVoie());
                e.setCodePostal(gp.getAdressePostale().getCodePostal());
                e.setVille(gp.getAdressePostale().getVille());
            }
            e.setStatus("");
            e.setMail(gp.getMail());
            e.setPhonetique("");
            return e;
        }).collect(Collectors.toList());
    }

    private List<EnsembleDto> getEnsemblesOrganismes(List<Organisme> organismes) {
        return organismes.stream().map(o -> {
            EnsembleDto e = new EnsembleDto();
            e.setUidEnsemble(o.getImportUid());
            e.setMandature("");
            e.setLibCourt("");
            e.setDateCreation(formatDate(o.getDateDebut()));
            e.setType("organisme");
            e.setTypeCommission("");
            e.setNbMembre(0L);
            e.setNbTitulaire(0L);
            e.setNbSuppleant(0L);
            e.setValid('O');
            e.setLibLong(o.getNom());
            e.setSt(0F);
            e.setDateFin(formatDate(o.getDateFin()));
            e.setDescription("");
            e.setCodeRne("");
            e.setDepartement("");
            e.setMotifFin(o.getMotifFin().trim());
            e.setSecteur("");
            e.setTelephone("");
            e.setFax("");
            if (o.getAdressePostale() != null) {
                e.setAdresse(o.getAdressePostale().getVoie());
                e.setCodePostal(o.getAdressePostale().getCodePostal());
                e.setVille(o.getAdressePostale().getVille());
            }
            e.setStatus("");
            e.setMail("");
            e.setPhonetique("");
            return e;
        }).collect(Collectors.toList());
    }

    private List<EnsembleDto> getEnsemblesCommissionsThematiques(List<CommissionThematique> cts) {
        return cts.stream().map(ct -> {
            EnsembleDto e = new EnsembleDto();
            e.setMandature("18");
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
            e.setLibLong("Vice-président(e)s");
            // TODO Solveig on a pas d'ordre dans Siger
            // on peut mettre en dur dans la base maintenant, mais c'est la merde à la première modification
            e.setSt(4f);
            e.setValid('1');
            e.setDescription(NON_RENSEIGNE);
            return e;
        }).collect(Collectors.toList());
    }

    // TODO Solveig pour info, dans l'export actuel, tous les liens entre un elu et un organismes sont par code rne
    // select data_membre.uid_membre from data_membre, data_ensemble where data_membre.uid_ensemble = data_ensemble.uid_ensemble and data_ensemble.code_rne is null;
    private List<MembreDto> getMembres(List<Elu> elus) {
        return elus.stream().flatMap(e -> e.getAppartenancesGroupePolitique().stream().map(agp -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(agp.getImportUid());
            m.setMandature("");
            m.setUidConseiller(e.getImportUid());
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
            m.setMotifFin(agp.getMotifFin().trim());
            m.setDateNomination("");
            m.setDescription("");
            return m;
        })).collect(Collectors.toList());
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

}
