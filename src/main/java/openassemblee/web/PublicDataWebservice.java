package openassemblee.web;

import openassemblee.domain.*;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.domain.enumeration.TypeIdentiteInternet;
import openassemblee.publicdata.ConseillerDto;
import openassemblee.publicdata.EnsembleDto;
import openassemblee.publicdata.MembreDto;
import openassemblee.repository.EluRepository;
import openassemblee.repository.GroupePolitiqueRepository;
import openassemblee.repository.ImageRepository;
import org.elasticsearch.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static openassemblee.domain.enumeration.NiveauConfidentialite.PUBLIABLE;
import static openassemblee.domain.enumeration.TypeIdentiteInternet.*;

@RestController
@RequestMapping("/api/publicdata/v1")
public class PublicDataWebservice {

    @Autowired
    private EluRepository eluRepository;

    @Autowired
    private GroupePolitiqueRepository groupePolitiqueRepository;

    @Autowired
    private ImageRepository imageRepository;

    @RequestMapping(value = "/websitedata", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public Map<String, Object> ensembles() {
        List<Elu> elus = eluRepository.findAll()
            .stream()
            .filter(e -> Strings.isNullOrEmpty(e.getMotifDemission()) && e.getDateDemission() == null)
            .collect(Collectors.toList());
        List<GroupePolitique> groupesPolitiques = groupePolitiqueRepository.findAll()
            .stream()
            .filter(gp -> Strings.isNullOrEmpty(gp.getMotifFin()) && gp.getDateFin() == null)
            .collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("conseillers", getConseillers(elus));
        result.put("ensembles", getEnsembles(groupesPolitiques));
        result.put("membres", getMembres(elus));
        return result;
    }

    private List<ConseillerDto> getConseillers(List<Elu> elus) {
        return elus.stream()
            .map(e -> {
                ConseillerDto d = new ConseillerDto();
                d.setUidConseiller(e.getImportUid());
                // TODO
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
                // TODO
                d.setNbEnfants("");
                // TODO
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
                // TODO différence avec setActiviteProf ?
                d.setProfession(e.getProfession());
                // TODO
                d.setDistinctions("");
                // TODO gné
                d.setDescription(".");
                d.setJpegphotoId(String.valueOf(e.getImage()));
                // TODO gné
                d.setSt(0F);
                StringBuilder commissionsStringBuilder = new StringBuilder();
                e.getAppartenancesCommissionsThematiques().stream()
                    .filter(a -> a.getDateFin() == null)
                    .forEach(a -> commissionsStringBuilder.append(", ").append(a.getCommissionThematique().getNom()));
                d.setCommissions(commissionsStringBuilder.toString());
                // TODO
                d.setDesignations("");
                Optional<FonctionExecutive> fe = e.getFonctionsExecutives().stream()
                    .filter(f -> f.getDateFin() == null)
                    .findFirst();
                d.setFonctionExecutif(fe.map(fonctionExecutive -> fonctionExecutive.getFonction()).orElse(""));
                // TODO
                d.setPhonetique(null);
                d.setTwitter(getUrl(e, Twitter));
                d.setFacebook(getUrl(e, Facebook));
                d.setSiteInternet(getUrl(e, SiteInternet));
                d.setBlog(getUrl(e, Blog));
                d.setAutre(getUrl(e, Autre));

                return d;
            }).collect(Collectors.toList());
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

    private List<EnsembleDto> getEnsembles(List<GroupePolitique> groupePolitiques) {
        return groupePolitiques.stream().map(gp -> {
            EnsembleDto e = new EnsembleDto();
            e.setUidEnsemble(gp.getImportUid());
            // TODO
            e.setMandature("");
            e.setLibCourt(gp.getNomCourt());
            e.setDateCreation(formatDate(gp.getDateDebut()));
            e.setType("Groupe politique");
            // TODO
            e.setTypeCommission("");
            e.setNbMembre(gp.getAppartenancesGroupePolitique().stream()
                .filter(g -> g.getDateFin() == null)
                .count());
            // TODO
            e.setNbTitulaire(0L);
            // TODO
            e.setNbSuppleant(0L);
            e.setValid('O');
            e.setLibLong(gp.getNom());
            // TODO
            e.setSt(0F);
            e.setDateFin(formatDate(gp.getDateFin()));
            // TODO
            e.setDescription("");
            // TODO
            e.setCodeRne("");
            // TODO
            e.setDepartement("");
            e.setMotifFin(gp.getMotifFin());
            // TODO
            e.setSecteur("");
            e.setTelephone(gp.getPhone());
            e.setFax(gp.getFax());
            if (gp.getAdressePostale() != null) {
                e.setAdresse(gp.getAdressePostale().getVoie());
                e.setCodePostal(gp.getAdressePostale().getCodePostal());
                e.setVille(gp.getAdressePostale().getVille());
            }
            // TODO
            e.setStatus("");
            e.setMail(gp.getMail());
            // TODO
            e.setPhonetique("");
            return e;
        }).collect(Collectors.toList());
    }

    private List<MembreDto> getMembres(List<Elu> elus) {
        return elus.stream().flatMap(e -> e.getAppartenancesGroupePolitique().stream().map(agp -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(agp.getImportUid());
            // TODO
            m.setMandature("");
            m.setUidConseiller(e.getImportUid());
            if (agp.getGroupePolitique() != null) {
                m.setUidEnsemble(agp.getGroupePolitique().getImportUid());
            }
            m.setType("Groupe politique");
            m.setDateDebut(formatDate(agp.getDateDebut()));
            m.setDateFin(formatDate(agp.getDateFin()));
            // TODO
            m.setNumeroNomination("");
            // TODO
            m.setStatus("");
            // TODO
            m.setNomination("");
            // TODO
            m.setSt(0f);
            // TODO
            m.setFonction("");
            // TODO
            m.setBureau('A');
            m.setMotifFin(agp.getMotifFin());
            // TODO
            m.setDateNomination("");
            // TODO
            m.setDescription("");
            // TODO pas de site web ???
            return m;
        })).collect(Collectors.toList());
    }

    private String formatDate(LocalDate date) {
        if (date != null) {
            return date.format(InjectDataWebservice.DATE_FORMATTER);
        } else {
            return " ";
        }
    }

}
