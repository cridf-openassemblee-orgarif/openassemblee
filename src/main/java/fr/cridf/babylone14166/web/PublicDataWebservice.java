package fr.cridf.babylone14166.web;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.domain.enumeration.TypeIdentiteInternet;
import fr.cridf.babylone14166.publicdata.ConseillerDto;
import fr.cridf.babylone14166.publicdata.EnsembleDto;
import fr.cridf.babylone14166.publicdata.MembreDto;
import fr.cridf.babylone14166.repository.EluRepository;
import fr.cridf.babylone14166.repository.GroupePolitiqueRepository;
import fr.cridf.babylone14166.repository.ImageRepository;
import org.elasticsearch.common.base.Strings;
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

    private static long lastConseillerId = 0;
    private static long lastEnsembleId = 0;
    private static long lastMembreId = 0;

    @RequestMapping(value = "/websitedata", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public Map<String, Object> ensembles() {
        List<Elu> elus = eluRepository.findAll();
        elus.forEach(e -> e.setUuid(String.valueOf(lastConseillerId++)));
        List<GroupePolitique> groupesPolitiques = groupePolitiqueRepository.findAll();
        groupesPolitiques.forEach(gp -> gp.setUuid(String.valueOf(lastEnsembleId++)));
        Map<String, Object> result = new HashMap<>();
        result.put("conseillers", getConseillers(elus));
        result.put("ensembles", getEnsembles(groupesPolitiques));
        result.put("membres", getMembres(elus));
        return result;
    }

    private List<ConseillerDto> getConseillers(List<Elu> elus) {
        return elus.stream()
            // TODO filter ?
            .filter(e -> !Strings.isNullOrEmpty(e.getMotifDemission()) && e.getDateDemission() != null)
            .map(e -> {
            ConseillerDto d = new ConseillerDto();
            // TODO
            d.setMandature("");
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
            // TODO
            d.setListeCourt("");
            // TODO
            d.setListeElectorale("");
            // TODO
            d.setNbEnfants("");
            // TODO
            d.setNomJeuneFille("");
            d.setAutresMandats("");
            d.setSituationFamiliale("");
            getPublishable(e.getNumerosTelephones()).ifPresent(nt -> d.setTelephone(nt.getNumero()));
            getPublishable(e.getNumerosFax()).ifPresent(nf -> d.setFax(nf.getNumero()));
            getPublishable(e.getAdressesMail()).ifPresent(am -> d.setMail(am.getMail()));
            d.setValid('O');
            // TODO
            d.setCodeDepElection("");
            d.setPrenom(e.getPrenom());
            if (e.getCivilite() != null) {
                d.setCivilite(e.getCivilite().label());
            }
            d.setVilleNaissance(e.getLieuNaissance());
            // TODO différence avec setActiviteProf ?
            d.setProfession(e.getProfession());
            d.setUidConseiller(e.getUuid().toString());
            // TODO
            d.setDistinctions("");
            // TODO
            d.setDescription("");
            d.setJpegphotoId(String.valueOf(e.getImage()));
            // TODO
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
            fe.ifPresent(fonctionExecutive -> d.setFonctionExecutif(fonctionExecutive.getFonction()));
            // TODO
            d.setPhonetique("");
            d.setTwitter(getUrl(e, Twitter));
            d.setFacebook(getUrl(e, Facebook));
            d.setSiteInternet(getUrl(e, SiteInternet));
            d.setBlog(getUrl(e, Blog));
            // TODO
            d.setAutre("");

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
            // TODO
            e.setMandature("");
            e.setUidEnsemble(gp.getUuid().toString());
            e.setLibCourt(gp.getNomCourt());
            if (gp.getDateDebut() != null) {
                e.setDateCreation(gp.getDateDebut().toString());
            }
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
            if (gp.getDateFin() != null) {
                e.setDateFin(gp.getDateFin().toString());
            }
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
            m.setUidMembre(String.valueOf(lastMembreId++));
            // TODO
            m.setMandature("");
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

}
