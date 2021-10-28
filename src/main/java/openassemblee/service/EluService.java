package openassemblee.service;

import openassemblee.domain.*;
import openassemblee.domain.enumeration.NiveauConfidentialite;
import openassemblee.repository.*;
import openassemblee.repository.search.*;
import openassemblee.service.dto.EluDTO;
import openassemblee.service.dto.EluListDTO;
import openassemblee.service.util.EluNomComparator;
import org.elasticsearch.common.base.Strings;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EluService {

    private static final Logger logger = LoggerFactory.getLogger(EluService.class);

    @Inject
    private EluRepository eluRepository;
    @Inject
    private EluSearchRepository eluSearchRepository;

    @Inject
    private AdressePostaleRepository adressePostaleRepository;
    @Inject
    private AdressePostaleSearchRepository adressePostaleSearchRepository;

    @Inject
    private NumeroTelephoneRepository numeroTelephoneRepository;
    @Inject
    private NumeroTelephoneSearchRepository numeroTelephoneSearchRepository;

    @Inject
    private NumeroFaxRepository numeroFaxRepository;
    @Inject
    private NumeroFaxSearchRepository numeroFaxSearchRepository;

    @Inject
    private AdresseMailRepository adresseMailRepository;
    @Inject
    private AdresseMailSearchRepository adresseMailSearchRepository;

    @Inject
    private IdentiteInternetRepository identiteInternetRepository;
    @Inject
    private IdentiteInternetSearchRepository identiteInternetSearchRepository;

    @Inject
    private AppartenanceOrganismeRepository appartenanceOrganismeRepository;

    @Inject
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;

    @Inject
    private AppartenanceCommissionPermanenteRepository appartenanceCommissionPermanenteRepository;

    @Inject
    private OrganismeRepository organismeRepository;

    @Inject
    private AppartenanceGroupePolitiqueRepository appartenanceGroupePolitiqueRepository;

    @Inject
    private FonctionCommissionPermanenteRepository fonctionCommissionPermanenteRepository;

    @Inject
    private FonctionExecutiveRepository fonctionExecutiveRepository;

    @Inject
    private FonctionCommissionThematiqueRepository fonctionCommissionThematiqueRepository;

    @Inject
    private FonctionGroupePolitiqueRepository fonctionGroupePolitiqueRepository;

    @Inject
    private CommissionPermanenteService commissionPermanenteService;

    @Inject
    private SessionMandatureService sessionMandatureService;

    @Transactional(readOnly = true)
    public List<Elu> getActifsAssemblee() {
        return eluRepository.findAll().stream()
            .filter(e -> isCurrentMandat(e.getMandats(), sessionMandatureService.getMandature())
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Elu> getCommissionPermanente() {
        return eluRepository.findAll().stream()
            .filter(e -> {
                // FIXME demo condition suffisante ? Les executifs ?
                for (AppartenanceCommissionPermanente a : e.getAppartenancesCommissionPermanente()) {
                    if (a.getDateFin() == null
                        && a.getMandature().getId().equals(sessionMandatureService.getMandature().getId())) {
                        return true;
                    }
                }
                return false;
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EluListDTO> getAll(Boolean loadAdresses, Boolean filterAdresses, Boolean removeDemissionaires) {
        List<Elu> elus = eluRepository.findAll();
        Mandature currentMandature = sessionMandatureService.getMandature();
        return elus.stream().map(e -> eluToEluListDTO(e, loadAdresses, filterAdresses))
            // les deux filter sont nécessaires :
            // 1) enlève les élus d'autres mandatures
            .filter(e -> {
                List<Mandat> mandats = e.getElu().getMandats().stream()
                    .filter(m -> m.getMandature().getId().equals(currentMandature.getId()))
                    .collect(Collectors.toList());
                return !mandats.isEmpty();
            })
            // 2) enlève les démissionnaires si besoin
            .filter(e -> !removeDemissionaires || isCurrentMandat(e.getElu().getMandats(), currentMandature))
            .sorted(EluNomComparator.comparing(EluListDTO::getElu))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EluListDTO getEluListDTO(Long id, Boolean loadAdresses, Boolean filterAdresses) {
        return eluToEluListDTO(eluRepository.findOne(id), loadAdresses, filterAdresses);
    }

    public EluListDTO eluToEluListDTO(Elu elu, Boolean loadAdresses, Boolean filterAdresses) {
        Optional<GroupePolitique> groupePolitique = elu.getAppartenancesGroupePolitique().stream()
            .filter(GroupePolitiqueService::isAppartenanceCourante)
            .filter(a -> a.getGroupePolitique() != null)
            .filter(a -> a.getGroupePolitique().getMandature().getId().equals(sessionMandatureService.getMandature().getId()))
            .map(AppartenanceGroupePolitique::getGroupePolitique)
            .filter(Objects::nonNull)
            .findFirst();
        String fonctionExec = elu.getFonctionsExecutives().stream()
            .filter(FonctionExecutive::isFonctionCourante)
            .filter(f -> f.getMandature().getId().equals(sessionMandatureService.getMandature().getId()))
            .findFirst()
            .map(f -> FonctionCommissionPermanente.getShortFonction(f.getFonction()))
            .orElse(null);
        String fonctionCP = elu.getFonctionsCommissionPermanente().stream()
            .filter(FonctionCommissionPermanente::isFonctionCourante)
            .filter(f -> f.getMandature().getId().equals(sessionMandatureService.getMandature().getId()))
            .findFirst()
            .map(f -> FonctionCommissionPermanente.getShortFonction(f.getFonction()))
            .orElse(null);
        String shortFonction = fonctionExec != null ? fonctionExec : fonctionCP != null ? fonctionCP : null;
        Boolean isCurrentMandat = isCurrentMandat(elu.getMandats(), sessionMandatureService.getMandature());
        if (groupePolitique.isPresent()) {
            return new EluListDTO(elu, groupePolitique.get(), shortFonction, isCurrentMandat, loadAdresses, filterAdresses);
        } else {
            return new EluListDTO(elu, shortFonction, isCurrentMandat, loadAdresses, filterAdresses);
        }
    }

    @Transactional(readOnly = true)
    public EluDTO get(Long id, Boolean filterAdresses) {
        Elu elu = eluRepository.findOne(id);
        if (elu == null) {
            return null;
        }
        Mandature mandature = sessionMandatureService.getMandature();
        Hibernate.initialize(elu.getAdressesPostales());
        Hibernate.initialize(elu.getNumerosTelephones());
        Hibernate.initialize(elu.getNumerosFax());
        Hibernate.initialize(elu.getAdressesMail());
        Hibernate.initialize(elu.getIdentitesInternet());
        Hibernate.initialize(elu.getAppartenancesOrganismes());
        Hibernate.initialize(elu.getDistinctionHonorifiques());
        filterEluMandat(elu, mandature);
        List<GroupePolitique> groupesPolitiques = elu.getAppartenancesGroupePolitique().stream()
            .map(i -> i.getGroupePolitique()).collect(Collectors.toList());
        groupesPolitiques.addAll(elu.getFonctionsGroupePolitique().stream()
            .map(i -> i.getGroupePolitique()).collect(Collectors.toList()));
        Map<Long, GroupePolitique> groupesPolitiquesMap = groupesPolitiques.stream()
            .distinct()
            .collect(Collectors.toMap(GroupePolitique::getId, Function.identity()));
        List<CommissionThematique> commissionThematiques = elu.getAppartenancesCommissionsThematiques().stream()
            .map(i -> i.getCommissionThematique()).collect(Collectors.toList());
        commissionThematiques.addAll(elu.getFonctionsCommissionsThematiques().stream()
            .map(i -> i.getCommissionThematique()).collect(Collectors.toList()));
        Map<Long, CommissionThematique> commissionThematiquesMap = commissionThematiques.stream()
            .distinct()
            .collect(Collectors.toMap(CommissionThematique::getId, Function.identity()));
        return new EluDTO(elu, groupesPolitiquesMap, commissionThematiquesMap, true, filterAdresses);
    }

    void filterEluMandat(Elu elu, Mandature mandature) {
        Long mandatureId = mandature.getId();
        elu.setMandats(elu.getMandats().stream()
            .filter(m -> m.getMandature().getId().equals(mandatureId)).collect(Collectors.toSet()));

        elu.setAppartenancesCommissionPermanente(elu.getAppartenancesCommissionPermanente().stream()
            .filter(a -> a.getMandature().getId().equals(mandatureId)).collect(Collectors.toList()));
        elu.setAppartenancesCommissionsThematiques(elu.getAppartenancesCommissionsThematiques().stream()
            .filter(a -> a.getCommissionThematique() != null)
            .filter(a -> a.getCommissionThematique().getMandature().getId().equals(mandatureId)).collect(Collectors.toSet()));
        elu.setAppartenancesGroupePolitique(elu.getAppartenancesGroupePolitique().stream()
            .filter(a -> a.getGroupePolitique() != null)
            .filter(a -> a.getGroupePolitique().getMandature().getId().equals(mandatureId))
            .collect(Collectors.toList()));

        elu.setFonctionsCommissionPermanente(elu.getFonctionsCommissionPermanente().stream()
            .filter(f -> f.getMandature().getId().equals(mandatureId)).collect(Collectors.toList()));
        elu.setFonctionsExecutives(elu.getFonctionsExecutives().stream()
            .filter(f -> f.getMandature().getId().equals(mandatureId)).collect(Collectors.toList()));
        elu.setFonctionsGroupePolitique(elu.getFonctionsGroupePolitique().stream()
            .filter(f -> f.getGroupePolitique() != null)
            .filter(f -> f.getGroupePolitique().getMandature().getId().equals(mandatureId))
            .collect(Collectors.toList()));
        elu.setFonctionsCommissionsThematiques(elu.getFonctionsCommissionsThematiques().stream()
            .filter(f -> f.getCommissionThematique() != null)
            .filter(f -> f.getCommissionThematique().getMandature().getId().equals(mandatureId)).collect(Collectors.toSet()));

        elu.setAutreMandats(elu.getAutreMandats().stream()
            .filter(m -> m.getMandature().getId().equals(mandatureId))
            .collect(Collectors.toSet()));
    }

    @Transactional
    public Elu saveElu(Elu elu) {
        Elu result = eluRepository.save(elu);
        eluSearchRepository.save(elu);
        return result;
    }

    @Transactional
    public void saveAdresseMail(Long id, AdresseMail adresseMail) {
        adresseMailRepository.save(adresseMail);
        adresseMailSearchRepository.save(adresseMail);
        Elu elu = eluRepository.getOne(id);
        elu.getAdressesMail().add(adresseMail);
        eluRepository.save(elu);
    }

    @Transactional
    public void updateAdresseMail(AdresseMail adresseMail) {
        adresseMailRepository.save(adresseMail);
        adresseMailSearchRepository.save(adresseMail);
    }

    @Transactional
    public void deleteAdresseMail(Long eluId, Long adresseMailId) {
        Elu elu = eluRepository.getOne(eluId);
        // TODO ça mérite un test car on dépend de l'impl equals là
        AdresseMail am = new AdresseMail();
        am.setId(adresseMailId);
        elu.getAdressesMail().remove(am);
        eluRepository.save(elu);
        adresseMailRepository.delete(adresseMailId);
        adresseMailSearchRepository.delete(adresseMailId);
    }

    @Transactional
    public void saveAdressePostale(long id, AdressePostale adressePostale) {
        adressePostaleRepository.save(adressePostale);
        adressePostaleSearchRepository.save(adressePostale);
        Elu elu = eluRepository.getOne(id);
        elu.getAdressesPostales().add(adressePostale);
        eluRepository.save(elu);
    }

    @Transactional
    public void updateAdressePostale(AdressePostale adressePostale) {
        adressePostaleRepository.save(adressePostale);
        adressePostaleSearchRepository.save(adressePostale);
    }

    @Transactional
    public void deleteAdressePostale(Long eluId, Long adressePostaleId) {
        Elu elu = eluRepository.getOne(eluId);
        // TODO ça mérite un test car on dépend de l'impl equals là
        AdressePostale ap = new AdressePostale();
        ap.setId(adressePostaleId);
        elu.getAdressesPostales().remove(ap);
        eluRepository.save(elu);
        adressePostaleRepository.delete(adressePostaleId);
        adressePostaleSearchRepository.delete(adressePostaleId);
    }

    @Transactional
    public void saveIdentiteInternet(Long id, IdentiteInternet identiteInternet) {
        identiteInternetRepository.save(identiteInternet);
        identiteInternetSearchRepository.save(identiteInternet);
        Elu elu = eluRepository.getOne(id);
        elu.getIdentitesInternet().add(identiteInternet);
        eluRepository.save(elu);
    }

    @Transactional
    public void updateIdentiteInternet(IdentiteInternet identiteInternet) {
        identiteInternetRepository.save(identiteInternet);
        identiteInternetSearchRepository.save(identiteInternet);
    }

    @Transactional
    public void deleteIdentiteInternet(Long eluId, Long identiteInternetId) {
        Elu elu = eluRepository.getOne(eluId);
        // TODO ça mérite un test car on dépend de l'impl equals là
        IdentiteInternet ii = new IdentiteInternet();
        ii.setId(identiteInternetId);
        elu.getIdentitesInternet().remove(ii);
        eluRepository.save(elu);
        identiteInternetRepository.delete(identiteInternetId);
        identiteInternetSearchRepository.delete(identiteInternetId);
    }

    @Transactional
    public void saveNumeroFax(Long id, NumeroFax numeroFax) {
        numeroFaxRepository.save(numeroFax);
        numeroFaxSearchRepository.save(numeroFax);
        Elu elu = eluRepository.getOne(id);
        elu.getNumerosFax().add(numeroFax);
        eluRepository.save(elu);
    }

    @Transactional
    public void updateNumeroFax(NumeroFax numeroFax) {
        numeroFaxRepository.save(numeroFax);
        numeroFaxSearchRepository.save(numeroFax);
    }

    @Transactional
    public void deleteNumeroFax(Long eluId, Long numeroFaxId) {
        Elu elu = eluRepository.getOne(eluId);
        // TODO ça mérite un test car on dépend de l'impl equals là
        NumeroFax nf = new NumeroFax();
        nf.setId(numeroFaxId);
        elu.getNumerosFax().remove(nf);
        eluRepository.save(elu);
        numeroFaxRepository.delete(numeroFaxId);
        numeroFaxSearchRepository.delete(numeroFaxId);
    }

    @Transactional
    public void saveNumeroTelephone(Long id, NumeroTelephone numeroTelephone) {
        numeroTelephoneRepository.save(numeroTelephone);
        numeroTelephoneSearchRepository.save(numeroTelephone);
        Elu elu = eluRepository.getOne(id);
        elu.getNumerosTelephones().add(numeroTelephone);
        eluRepository.save(elu);
    }

    @Transactional
    public void updateNumeroTelephone(NumeroTelephone numeroTelephone) {
        numeroTelephoneRepository.save(numeroTelephone);
        numeroTelephoneSearchRepository.save(numeroTelephone);
    }

    @Transactional
    public void deleteNumeroTelephone(Long eluId, Long numeroTelephoneId) {
        Elu elu = eluRepository.getOne(eluId);
        // TODO ça mérite un test car on dépend de l'impl equals là
        NumeroTelephone nt = new NumeroTelephone();
        nt.setId(numeroTelephoneId);
        elu.getNumerosTelephones().remove(nt);
        eluRepository.save(elu);
        numeroTelephoneRepository.delete(numeroTelephoneId);
        numeroTelephoneSearchRepository.delete(numeroTelephoneId);
    }

    @Transactional(readOnly = true)
    public ExcelExportService.Entry[] getExportEntries(Boolean filterAdresses) {
        List<EluListDTO> dtos = getAll(true, filterAdresses, true);
        List<List<String>> elusActifsLines = new ArrayList<>();
        elusActifsLines.add(Arrays.asList("Civilité", "Prénom", "Nom", "Groupe politique", "Profession",
            "Lieu de naissance", "Date de naissance", "Département", "Adresses postales", "Emails", "Numéros publics",
            "Numéros internes ou confidentiels"));
//        List<List<String>> elusInactifsLines = new ArrayList<>();
//        elusInactifsLines.add(Arrays.asList("Civilité", "Prénom", "Nom", "Groupe politique", "Date de démission",
//            "Motif de démission", "Profession", "Lieu de naissance", "Date de naissance", "Département",
//            "Adresses postales", "Emails", "Numéros publics", "Numéros internes ou confidentiels"));
        for (EluListDTO dto : dtos) {
            Elu e = dto.getElu();
            GroupePolitique gp = dto.getGroupePolitique();
//            if (e.getDateDemission() == null) {
            elusActifsLines.add(xlsEluLine(e, gp, false));
//            } else {
//                elusInactifsLines.add(xlsEluLine(e, gp, true));
//            }
        }
        return new ExcelExportService.Entry[]{
            new ExcelExportService.Entry("Élus", elusActifsLines),
//            new ExcelExportService.Entry("Élus démissionnaires", elusInactifsLines),
            new ExcelExportService.Entry("Fonctions", commissionPermanenteService.getFonctionsEntry(dtos))
        };
    }

    public List<String> xlsEluLine(Elu e, GroupePolitique gp, Boolean demissionaire) {
        String civilite = e.getCiviliteLabel();
        String groupePolitique = gp != null ? gp.getNom() :
            "Aucun groupe politique";
        String dateNaissance = e.getDateNaissance() != null ?
            e.getDateNaissance().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de naissance inconnue";
        String adressePostales = e.getAdressesPostales().stream()
            .map(ap -> ap.getOneline())
            .reduce("", (a1, a2) -> a1 + (a1.equals("") ? "" : ", ") + a2);
        String emails = e.getAdressesMail().stream()
            .map(ap -> ap.getMail())
            .reduce("", (a1, a2) -> a1 + (a1.equals("") ? "" : ", ") + a2);
        String numerosPublics = e.getNumerosTelephones().stream()
            .filter(n -> n.getNiveauConfidentialite() == NiveauConfidentialite.PUBLIABLE)
            .map(n -> n.getNumero() + (n.getNatureProPerso() != null ? "(" + n.getNatureProPerso().name() + ")" : ""))
            .reduce("", (n1, n2) -> n1 + (n1.equals("") ? "" : ", ") + n2);
        String numerosInternesOuConfidentiels = e.getNumerosTelephones().stream()
            .filter(n -> n.getNiveauConfidentialite() == NiveauConfidentialite.INTERNE || n.getNiveauConfidentialite() == NiveauConfidentialite.CONFIDENTIEL)
            .map(n -> n.getNumero() + "(" + n.getNiveauConfidentialite().name() + (n.getNatureProPerso() != null ? "," + n.getNatureProPerso().name() : "") + ")")
            .reduce("", (n1, n2) -> n1 + (n1.equals("") ? "" : ", ") + n2);
        Mandat mandat = getOnlyCurrentMandat(e.getMandats(), sessionMandatureService.getMandature());
        String departement = mandat != null ? mandat.getDepartement() : "";
        if (!demissionaire) {
            return Arrays.asList(civilite, e.getPrenom(), e.getNom(), groupePolitique,
                e.getProfession(), e.getLieuNaissance(), dateNaissance, departement, adressePostales, emails,
                numerosPublics, numerosInternesOuConfidentiels);
        } else {
            String dateDemission = mandat != null && mandat.getDateDemission() != null ?
                mandat.getDateDemission().format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
            String motifDemission = mandat != null ? mandat.getMotifDemission() : "";
            return Arrays.asList(civilite, e.getPrenom(), e.getNom(), groupePolitique, dateDemission,
                motifDemission, e.getProfession(), e.getLieuNaissance(), dateNaissance, departement,
                adressePostales, emails, numerosPublics, numerosInternesOuConfidentiels);
        }
    }

    @Transactional(readOnly = true)
    public static boolean isCurrentMandat(Set<Mandat> mandats, Mandature currentMandature) {
        return !getCurrentMandats(mandats, currentMandature).isEmpty();
    }

    @Transactional(readOnly = true)
    private static List<Mandat> getCurrentMandats(Set<Mandat> mandats, Mandature currentMandature) {
        // plus tard : || a.getDateFin().isAfter(LocalDate.now()) ?
        return mandats.stream()
            .filter(m -> m.getMandature().getId().equals(currentMandature.getId()))
            .filter(m -> Strings.isNullOrEmpty(m.getMotifDemission()) && m.getDateDemission() == null)
            .collect(Collectors.toList());
    }

    // pas idéal comme api, mais uniquement utilisé par les exports/api au moment de l'écriture
    @Transactional(readOnly = true)
    public static Mandat getOnlyCurrentMandat(Set<Mandat> mandats, Mandature currentMandature) {
        List<Mandat> result = getCurrentMandats(mandats, currentMandature);
        if (result.size() > 1) {
            Long eluId = result.stream().findFirst().get().getElu().getId();
            logger.warn("Plusieurs mandats sur l'élu " + eluId);
            return null;
        }
        return result.stream().findFirst().orElse(null);
    }
}
