package openassemblee.webservice;

import static openassemblee.config.Constants.parisZoneId;
import static openassemblee.service.EluService.isCurrentMandat;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import openassemblee.domain.*;
import openassemblee.domain.api.*;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.domain.enumeration.NatureProPerso;
import openassemblee.domain.enumeration.NiveauConfidentialite;
import openassemblee.repository.EluRepository;
import openassemblee.repository.GroupePolitiqueRepository;
import openassemblee.service.EluService;
import openassemblee.service.SessionMandatureService;
import openassemblee.service.util.AppartenancesMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/publicdata/v2")
public class EluWebserviceResource {

    @Autowired
    protected EluRepository eluRepository;

    @Autowired
    protected GroupePolitiqueRepository groupePolitiqueRepository;

    @Autowired
    private SessionMandatureService sessionMandatureService;

    class ElusData {

        public List<ApiElu> elus;

        public ElusData(List<ApiElu> elus) {
            this.elus = elus;
        }
    }

    class GroupePolitiquesData {

        public List<ApiGroupePolitique> elus;
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/elus", method = RequestMethod.GET)
    public ElusData elus(@RequestParam Optional<Boolean> actifsUniquement) {
        LocalDate today = LocalDate.now(parisZoneId);
        Boolean actifOnly = actifsUniquement.orElse(false);
        Long mandatureId = sessionMandatureService.getMandature().getId();
        List<ApiElu> elus = eluRepository
            .findAll()
            .stream()
            .map(elu -> {
                ApiElu apiElu = new ApiElu();
                apiElu.id = elu.getId();
                apiElu.uid = elu.getUid();
                //apiElu.shortUid = elu.getShortUid();
                apiElu.civilite = elu.getCivilite();
                apiElu.nom = elu.getNom();
                apiElu.prenom = elu.getPrenom();
                apiElu.nomJeuneFille = elu.getNomJeuneFille();
                apiElu.profession = nullIfBlank(elu.getProfession());
                apiElu.dateNaissance = elu.getDateNaissance();
                apiElu.lieuNaissance = elu.getLieuNaissance();
                apiElu.codeDepartement = elu.getCodeDepartement();
                apiElu.departement = elu.getDepartement();
                apiElu.image = elu.getImage();
                apiElu.mandats = mapMandats(elu, actifOnly, today, mandatureId);
                apiElu.actif = apiElu.mandats.stream().anyMatch(it -> it.actif);
                apiElu.appartenancesGroupePolitique =
                    mapGroupePolitiques(elu, actifOnly, today, mandatureId);
                apiElu.appartenancesCommissionsThematiques =
                    mapCommissionThematiques(
                        elu,
                        actifOnly,
                        today,
                        mandatureId
                    );
                mapCoordonnées(elu, apiElu);
                return apiElu;
            })
            .filter(it -> !actifOnly || it.actif)
            .sorted(
                Comparator.comparing(e -> {
                    if (e.nom != null) return e.nom; else return "";
                })
            )
            .collect(Collectors.toList());
        return new ElusData(elus);
    }

    private List<ApiMandat> mapMandats(
        Elu elu,
        Boolean actifOnly,
        LocalDate today,
        Long mandatureId
    ) {
        List<ApiMandat> result = elu
            .getMandats()
            .stream()
            .map(it -> {
                ApiMandat apiMandat = new ApiMandat();
                apiMandat.id = it.getId();
                apiMandat.mandatureId =
                    it.getMandature() != null
                        ? it.getMandature().getId()
                        : null;
                apiMandat.anneeDebutMandature =
                    it.getMandature() != null
                        ? it.getMandature().getAnneeDebut()
                        : null;
                apiMandat.anneeFinMandature =
                    it.getMandature() != null
                        ? it.getMandature().getAnneeFin()
                        : null;
                apiMandat.dateDebutMandat = it.getDateDebut();
                apiMandat.listeElectoraleId =
                    it.getListeElectorale() != null
                        ? it.getListeElectorale().getId()
                        : null;
                apiMandat.nomListeElectorale =
                    it.getListeElectorale() != null
                        ? it.getListeElectorale().getNom()
                        : null;
                apiMandat.nomCourtListeElectorale =
                    it.getListeElectorale() != null
                        ? it.getListeElectorale().getNomCourt()
                        : null;
                apiMandat.codeDepartement = it.getCodeDepartement();
                apiMandat.departement = it.getDepartement();
                apiMandat.dateDemissionMandat = it.getDateDemission();
                apiMandat.motifDemissionMandat =
                    nullIfBlank(it.getMotifDemission());
                Boolean nonDemissionnaire =
                    it.getDateDemission() == null ||
                    it.getDateDemission().isAfter(today);
                Boolean mandatureEnCours =
                    it.getMandature() != null &&
                    it.getMandature().getId().equals(mandatureId);
                apiMandat.actif = nonDemissionnaire && mandatureEnCours;
                apiMandat.appartenancesCommissionPermanente =
                    elu
                        .getAppartenancesCommissionPermanente()
                        .stream()
                        .filter(a -> AppartenancesMatcher.match(it, a))
                        .map(a -> {
                            ApiAppartenanceCommissionPermanente api =
                                new ApiAppartenanceCommissionPermanente();
                            api.id = a.getId();
                            api.dateDebut = a.getDateDebut();
                            api.dateFin = a.getDateFin();
                            api.motifFin = nullIfBlank(a.getMotifFin());
                            Boolean appartenanceNonDemissionnaire =
                                a.getDateFin() == null ||
                                a.getDateFin().isAfter(today);
                            api.actif =
                                nonDemissionnaire &&
                                mandatureEnCours &&
                                appartenanceNonDemissionnaire;
                            api.fonctions =
                                elu
                                    .getFonctionsCommissionPermanente()
                                    .stream()
                                    .filter(f ->
                                        AppartenancesMatcher.match(a, f)
                                    )
                                    .map(f -> {
                                        ApiFonctionCommissionPermanente af =
                                            new ApiFonctionCommissionPermanente();
                                        af.id = f.getId();
                                        af.fonction = f.getFonction();
                                        af.dateDebut = f.getDateDebut();
                                        af.dateFin = f.getDateFin();
                                        af.motifFin =
                                            nullIfBlank(f.getMotifFin());
                                        Boolean fonctionNonDemissionnaire =
                                            f.getDateFin() == null ||
                                            f.getDateFin().isAfter(today);
                                        af.actif =
                                            nonDemissionnaire &&
                                            mandatureEnCours &&
                                            appartenanceNonDemissionnaire &&
                                            fonctionNonDemissionnaire;
                                        return af;
                                    })
                                    .collect(Collectors.toList());
                            return api;
                        })
                        .filter(f -> !actifOnly || f.actif)
                        .collect(Collectors.toList());
                apiMandat.fonctionsExecutives =
                    elu
                        .getFonctionsExecutives()
                        .stream()
                        .filter(a -> AppartenancesMatcher.match(it, a))
                        .map(f -> {
                            ApiFonctionExecutive api =
                                new ApiFonctionExecutive();
                            api.id = f.getId();
                            api.fonction = f.getFonction();
                            api.dateDebut = f.getDateDebut();
                            api.dateFin = f.getDateFin();
                            api.motifFin = nullIfBlank(f.getMotifFin());
                            Boolean fonctionNonDemissionnaire =
                                f.getDateFin() == null ||
                                f.getDateFin().isAfter(today);
                            api.actif =
                                nonDemissionnaire &&
                                mandatureEnCours &&
                                fonctionNonDemissionnaire;
                            return api;
                        })
                        .filter(f -> !actifOnly || f.actif)
                        .collect(Collectors.toList());
                apiMandat.autreMandats =
                    elu
                        .getAutreMandats()
                        .stream()
                        .filter(a -> AppartenancesMatcher.match(it, a))
                        .map(m -> {
                            ApiAutreMandat api = new ApiAutreMandat();
                            api.id = m.getId();
                            api.collectiviteOuOrganisme =
                                m.getCollectiviteOuOrganisme();
                            api.fonction = m.getFonction();
                            api.dateDebutString = m.getDateDebutString();
                            api.dateDebut = m.getDateDebut();
                            api.dateFin = m.getDateFin();
                            api.motifFin = nullIfBlank(m.getMotifFin());
                            Boolean mandatNonDemissionnaire =
                                m.getDateFin() == null ||
                                m.getDateFin().isAfter(today);
                            api.actif =
                                nonDemissionnaire &&
                                mandatureEnCours &&
                                mandatNonDemissionnaire;
                            return api;
                        })
                        .filter(f -> !actifOnly || f.actif)
                        .collect(Collectors.toList());
                return apiMandat;
            })
            .filter(it -> !actifOnly || it.actif)
            .collect(Collectors.toList());
        return result
            .stream()
            .sorted(
                Comparator.comparing(c -> {
                    if (c.dateDebutMandat == null) {
                        return LocalDate.MIN;
                    } else {
                        return c.dateDebutMandat;
                    }
                })
            )
            .skip(actifOnly ? Math.max(result.size() - 1, 0) : 0)
            .collect(Collectors.toList());
    }

    private List<ApiAppartenanceGroupePolitique> mapGroupePolitiques(
        Elu elu,
        Boolean actifOnly,
        LocalDate today,
        Long mandatureId
    ) {
        List<ApiAppartenanceGroupePolitique> result = elu
            .getAppartenancesGroupePolitique()
            .stream()
            .map(it -> {
                ApiAppartenanceGroupePolitique a =
                    new ApiAppartenanceGroupePolitique();
                a.id = it.getId();
                a.groupePolitiqueId =
                    it.getGroupePolitique() != null
                        ? it.getGroupePolitique().getId()
                        : null;
                a.nomGroupePolitique =
                    it.getGroupePolitique() != null
                        ? it.getGroupePolitique().getNom()
                        : null;
                a.nomCourtGroupePolitique =
                    it.getGroupePolitique() != null
                        ? it.getGroupePolitique().getNomCourt()
                        : null;
                a.dateDebut = it.getDateDebut();
                a.dateFin = it.getDateFin();
                a.motifFin = nullIfBlank(it.getMotifFin());
                Boolean nonDemissionnaire =
                    it.getDateFin() == null || it.getDateFin().isAfter(today);
                Boolean mandatureEnCours =
                    it.getGroupePolitique() != null &&
                    it.getGroupePolitique().getMandature() != null &&
                    it
                        .getGroupePolitique()
                        .getMandature()
                        .getId()
                        .equals(mandatureId);
                Boolean groupeEnCours =
                    it.getGroupePolitique() != null &&
                    (
                        it.getGroupePolitique().getDateFin() == null ||
                        it.getGroupePolitique().getDateFin().isAfter(today)
                    );
                a.actif =
                    nonDemissionnaire && mandatureEnCours && groupeEnCours;
                a.fonctions =
                    elu
                        .getFonctionsGroupePolitique()
                        .stream()
                        .filter(f -> AppartenancesMatcher.match(it, f))
                        .map(f -> {
                            ApiFonctionGroupePolitique r =
                                new ApiFonctionGroupePolitique();
                            r.id = f.getId();
                            r.fonction = f.getFonction();
                            r.dateDebut = f.getDateDebut();
                            r.dateFin = f.getDateFin();
                            r.motifFin = f.getMotifFin();
                            Boolean fonctionNonDemissionnaire =
                                f.getDateFin() == null ||
                                f.getDateFin().isAfter(today);
                            r.actif =
                                nonDemissionnaire &&
                                mandatureEnCours &&
                                groupeEnCours &&
                                fonctionNonDemissionnaire;
                            return r;
                        })
                        .filter(f -> !actifOnly || f.actif)
                        .collect(Collectors.toList());
                return a;
            })
            .filter(it -> !actifOnly || it.actif)
            .collect(Collectors.toList());
        return result
            .stream()
            .sorted(
                Comparator.comparing(c -> {
                    if (c.dateDebut == null) {
                        return LocalDate.MIN;
                    } else {
                        return c.dateDebut;
                    }
                })
            )
            .skip(actifOnly ? Math.max(result.size() - 1, 0) : 0)
            .collect(Collectors.toList());
    }

    private List<ApiAppartenanceCommissionThematique> mapCommissionThematiques(
        Elu elu,
        Boolean actifOnly,
        LocalDate today,
        Long mandatureId
    ) {
        List<ApiAppartenanceCommissionThematique> result = elu
            .getAppartenancesCommissionsThematiques()
            .stream()
            .map(it -> {
                ApiAppartenanceCommissionThematique a =
                    new ApiAppartenanceCommissionThematique();
                a.id = it.getId();
                a.commissionThematiqueId =
                    it.getCommissionThematique() != null
                        ? it.getCommissionThematique().getId()
                        : null;
                a.nomCommissionThematique =
                    it.getCommissionThematique() != null
                        ? it.getCommissionThematique().getNom()
                        : null;
                a.dateDebut = it.getDateDebut();
                a.dateFin = it.getDateFin();
                a.motifFin = nullIfBlank(it.getMotifFin());
                Boolean nonDemissionnaire =
                    it.getDateFin() == null || it.getDateFin().isAfter(today);
                Boolean mandatureEnCours =
                    it.getCommissionThematique() != null &&
                    it.getCommissionThematique().getMandature() != null &&
                    it
                        .getCommissionThematique()
                        .getMandature()
                        .getId()
                        .equals(mandatureId);
                Boolean commissionEnCours =
                    it.getCommissionThematique() != null &&
                    (
                        it.getCommissionThematique().getDateFin() == null ||
                        it.getCommissionThematique().getDateFin().isAfter(today)
                    );
                a.actif =
                    nonDemissionnaire && mandatureEnCours && commissionEnCours;
                a.fonctions =
                    elu
                        .getFonctionsCommissionsThematiques()
                        .stream()
                        .filter(f -> AppartenancesMatcher.match(it, f))
                        .map(f -> {
                            ApiFonctionCommissionThematique r =
                                new ApiFonctionCommissionThematique();
                            r.id = f.getId();
                            r.fonction = f.getFonction();
                            r.dateDebut = f.getDateDebut();
                            r.dateFin = f.getDateFin();
                            r.motifFin = f.getMotifFin();
                            Boolean fonctionNonDemissionnaire =
                                f.getDateFin() == null ||
                                f.getDateFin().isAfter(today);
                            r.actif =
                                nonDemissionnaire &&
                                mandatureEnCours &&
                                commissionEnCours &&
                                fonctionNonDemissionnaire;
                            return r;
                        })
                        .filter(f -> !actifOnly || f.actif)
                        .collect(Collectors.toList());
                return a;
            })
            .filter(it -> !actifOnly || it.actif)
            .collect(Collectors.toList());
        return result
            .stream()
            .sorted(
                Comparator.comparing(c -> {
                    if (c.dateDebut == null) {
                        return LocalDate.MIN;
                    } else {
                        return c.dateDebut;
                    }
                })
            )
            .collect(Collectors.toList());
    }

    private void mapCoordonnées(Elu elu, ApiElu apiElu) {
        apiElu.adressesPostales =
            elu
                .getAdressesPostales()
                .stream()
                .filter(a -> a.getPublicationAnnuaire())
                .filter(a ->
                    a.getNiveauConfidentialite() ==
                    NiveauConfidentialite.PUBLIABLE
                )
                .filter(a -> a.getNatureProPerso() == NatureProPerso.PRO)
                .map(ap -> {
                    ApiAdressePostale a = new ApiAdressePostale();
                    a.voie = ap.getVoie();
                    a.codePostal = ap.getCodePostal();
                    a.ville = ap.getVille();
                    return a;
                })
                .collect(Collectors.toList());
        apiElu.numerosTelephones =
            elu
                .getNumerosTelephones()
                .stream()
                .filter(n -> n.getPublicationAnnuaire())
                .filter(n ->
                    n.getNiveauConfidentialite() ==
                    NiveauConfidentialite.PUBLIABLE
                )
                .filter(n -> n.getNatureProPerso() == NatureProPerso.PRO)
                .map(n -> {
                    ApiNumeroTelephone a = new ApiNumeroTelephone();
                    a.natureFixeMobile = n.getNatureFixeMobile();
                    a.numero = n.getNumero();
                    return a;
                })
                .collect(Collectors.toList());
        apiElu.numerosFax =
            elu
                .getNumerosFax()
                .stream()
                .filter(n -> n.getPublicationAnnuaire())
                .filter(n ->
                    n.getNiveauConfidentialite() ==
                    NiveauConfidentialite.PUBLIABLE
                )
                .filter(n -> n.getNatureProPerso() == NatureProPerso.PRO)
                .map(NumeroFax::getNumero)
                .collect(Collectors.toList());
        apiElu.adressesMail =
            elu
                .getAdressesMail()
                .stream()
                .filter(a -> a.getPublicationAnnuaire())
                .filter(a ->
                    a.getNiveauConfidentialite() ==
                    NiveauConfidentialite.PUBLIABLE
                )
                .filter(a -> a.getNatureProPerso() == NatureProPerso.PRO)
                .map(AdresseMail::getMail)
                .collect(Collectors.toList());
        apiElu.identitesInternet =
            elu
                .getIdentitesInternet()
                .stream()
                .map(i -> {
                    ApiIdentiteInternet a = new ApiIdentiteInternet();
                    a.typeIdentiteInternet = i.getTypeIdentiteInternet();
                    a.url = i.getUrl();
                    return a;
                })
                .collect(Collectors.toList());
        apiElu.distinctionHonorifiques =
            elu
                .getDistinctionHonorifiques()
                .stream()
                .map(it -> {
                    ApiDistinctionHonorifique a =
                        new ApiDistinctionHonorifique();
                    a.id = it.getId();
                    a.titre = it.getTitre();
                    a.date = nullIfBlank(it.getDate());
                    return a;
                })
                .collect(Collectors.toList());
    }

    private String nullIfBlank(String value) {
        if (value != null && value.trim().equals("")) {
            return null;
        } else {
            return value;
        }
    }
}
