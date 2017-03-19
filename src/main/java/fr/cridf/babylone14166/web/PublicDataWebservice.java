package fr.cridf.babylone14166.web;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.domain.enumeration.TypeIdentiteInternet;
import fr.cridf.babylone14166.publicdata.ConseillerDto;
import fr.cridf.babylone14166.publicdata.EnsembleDto;
import fr.cridf.babylone14166.publicdata.MembreDto;
import fr.cridf.babylone14166.repository.EluRepository;
import fr.cridf.babylone14166.repository.GroupePolitiqueRepository;
import fr.cridf.babylone14166.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static fr.cridf.babylone14166.domain.enumeration.NiveauConfidentialite.PUBLIABLE;
import static fr.cridf.babylone14166.domain.enumeration.TypeIdentiteInternet.*;

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
        List<Elu> elus = eluRepository.findAll();
        elus.forEach(e -> e.setUuid(UUID.randomUUID()));
        List<GroupePolitique> groupesPolitiques = groupePolitiqueRepository.findAll();
        groupesPolitiques.forEach(gp -> gp.setUuid(UUID.randomUUID()));
        Map<String, Object> result = new HashMap<>();
        result.put("conseillers", getConseillers(elus));
        result.put("ensembles", getEnsembles(groupesPolitiques));
        result.put("membres", getMembres(elus));
        return result;
    }

    private List<ConseillerDto> getConseillers(List<Elu> elus) {
        return elus.stream().map(e -> {
            ConseillerDto d = new ConseillerDto();
//            d.setMandature("");
            d.setNom(e.getNom());
            d.setActiviteProf(e.getProfession());
            getPublishable(e.getAdressesPostales()).ifPresent(ap -> {
                d.setAdresse(ap.getVoie());
                d.setVille(ap.getVille());
                d.setCodePostal(ap.getCodePostal());
            });
            if (e.getDateNaissance() != null) {
                d.setDateNaissance(e.getDateNaissance().toString());
            }
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
//            d.setListeCourt("");
//            d.setListeElectorale("");
//            d.setNbEnfants("");
//            d.setNomJeuneFille("");
            d.setAutresMandats("");
            d.setSituationFamiliale("");
            getPublishable(e.getNumerosTelephones()).ifPresent(nt -> d.setTelephone(nt.getNumero()));
            getPublishable(e.getNumerosFax()).ifPresent(nf -> d.setFax(nf.getNumero()));
            getPublishable(e.getAdressesMail()).ifPresent(am -> d.setMail(am.getMail()));
            d.setValid('O');
            // private String code_dep_election;
            d.setPrenom(e.getPrenom());
            if (e.getCivilite() != null) {
                d.setCivilite(e.getCivilite().label());
            }
            d.setVilleNaissance(e.getLieuNaissance());
            // TODO différence avec setActiviteProf ?
            d.setProfession(e.getProfession());
            d.setUidConseiller(e.getUuid().toString());
//            private String distinctions;
//            private String description;
//                d.setJpegphotoId(new SerialBlob(imageRepository.getImage(e.getImage()).getData()));
            d.setJpegphotoId(String.valueOf(e.getImage()));
//            private Float st;
            StringBuilder commissionsStringBuilder = new StringBuilder();
            e.getAppartenancesCommissionsThematiques().stream()
                .filter(a -> a.getDateFin() == null)
                .forEach(a -> commissionsStringBuilder.append(", ").append(a.getCommissionThematique().getNom()));
            d.setCommissions(commissionsStringBuilder.toString());
//            private String designations;
            Optional<FonctionExecutive> fe = e.getFonctionsExecutives().stream()
                .filter(f -> f.getDateFin() == null)
                .findFirst();
            fe.ifPresent(fonctionExecutive -> d.setFonctionExecutif(fonctionExecutive.getFonction()));
//            private String phonetique;
            d.setTwitter(getUrl(e, Twitter));
            d.setFacebook(getUrl(e, Facebook));
            d.setSiteInternet(getUrl(e, SiteInternet));
            d.setBlog(getUrl(e, Blog));
//            private String autre;

            return d;
        }).collect(Collectors.toList());
    }

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
        return ii.map(IdentiteInternet::getUrl).orElse(null);
    }

    private List<EnsembleDto> getEnsembles(List<GroupePolitique> groupePolitiques) {
        return groupePolitiques.stream().map(gp -> {
            EnsembleDto e = new EnsembleDto();
//            private String mandature;
            e.setUidEnsemble(gp.getUuid().toString());
            e.setLibCourt(gp.getNomCourt());
            if (gp.getDateDebut() != null) {
                e.setDateCreation(gp.getDateDebut().toString());
            }
            e.setType("Groupe politique");
//            private String typeCommission;
            e.setNbMembre(gp.getAppartenancesGroupePolitique().stream()
                .filter(g -> g.getDateFin() == null)
                .count());
//            private Float nbTitulaire;
//            private Float nbSuppleant;
            e.setValid('O');
            e.setLibLong(gp.getNom());
//            private Float st;
            if (gp.getDateFin() != null) {
                e.setDateFin(gp.getDateFin().toString());
            }
//            private String description;
//            private String codeRne;
//            private String departement;
            e.setMotifFin(gp.getMotifFin());
//            private String secteur;
//            private String telephone;
//            private String fax;
            if (gp.getAdressePostale() != null) {
                e.setAdresse(gp.getAdressePostale().getVoie());
                e.setCodePostal(gp.getAdressePostale().getCodePostal());
                e.setVille(gp.getAdressePostale().getVille());
            }
//            private String status;
//            private String mail;
//            private String phonetique;
            return e;
        }).collect(Collectors.toList());
    }

    private List<MembreDto> getMembres(List<Elu> elus) {
        return elus.stream().flatMap(e -> e.getAppartenancesGroupePolitique().stream().map(agp -> {
            MembreDto m = new MembreDto();
            m.setUidMembre(UUID.randomUUID().toString());
//            private String mandature;
            m.setUidConseiller(e.getUuid().toString());
            if (agp.getGroupePolitique() != null) {
                m.setUidEnsemble(agp.getGroupePolitique().getUuid().toString());
            }
            m.setType("Groupe politique");
            if (agp.getDateDebut() != null) {
                m.setDateDebut(agp.getDateDebut().toString());
            }
            if (agp.getDateFin() != null) {
                m.setDateFin(agp.getDateFin().toString());
            }
//            private String numeroNomination;
//            private String status;
//            private String nomination;
//            private Float st;
//            private String fonction;
//            private Character bureau;
            m.setMotifFin(agp.getMotifFin());
//            private String dateNomination;
//            private String description;
            return m;
        })).collect(Collectors.toList());
    }

}
