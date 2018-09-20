package openassemblee.service;

import openassemblee.domain.*;
import openassemblee.repository.*;
import openassemblee.repository.search.*;
import openassemblee.service.dto.EluDTO;
import openassemblee.service.dto.EluListDTO;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EluService {

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

    @Transactional(readOnly = true)
    public List<Elu> getActifsAssemblee() {
        // FIXME pour le moment tous les élus sont actifs, wtf...
        return eluRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Elu> getCommissionPermanente() {
        return eluRepository.findAll().stream()
            .filter(e -> {
                // FIXME demo condition suffisante ? Les executifs ?
                for (AppartenanceCommissionPermanente a : e.getAppartenancesCommissionPermanente()) {
                    if (a.getDateFin() == null) {
                        return true;
                    }
                }
                return false;
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EluListDTO> getAll() {
        List<Elu> elus = eluRepository.findAll();
        return elus.stream().map(e -> {
            Optional<GroupePolitique> groupePolitique = e.getAppartenancesGroupePolitique().stream()
                .filter(GroupePolitiqueService::isAppartenanceCourante)
                .map(AppartenanceGroupePolitique::getGroupePolitique)
                .filter(Objects::nonNull)
                .findFirst();
            if (groupePolitique.isPresent()) {
                return new EluListDTO(e, groupePolitique.get());
            } else {
                return new EluListDTO(e);
            }
        })
            .sorted(Comparator.comparing(e -> e.getElu().getNom()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EluListDTO getEluListDTO(Long id) {
        return eluToEluListDTO(eluRepository.findOne(id));
    }

    public EluListDTO eluToEluListDTO(Elu elu) {
        Optional<GroupePolitique> groupePolitique = elu.getAppartenancesGroupePolitique().stream()
            .filter(GroupePolitiqueService::isAppartenanceCourante)
            .map(AppartenanceGroupePolitique::getGroupePolitique)
            .findFirst();
        return groupePolitique.map(groupePolitique1 -> new EluListDTO(elu, groupePolitique1)).orElseGet(() -> new EluListDTO(elu));
    }

    @Transactional(readOnly = true)
    public EluDTO get(Long id) {
        Elu elu = eluRepository.findOne(id);
        if (elu == null) {
            return null;
        }
        Hibernate.initialize(elu.getAdressesPostales());
        Hibernate.initialize(elu.getNumerosTelephones());
        Hibernate.initialize(elu.getNumerosFax());
        Hibernate.initialize(elu.getAdressesMail());
        Hibernate.initialize(elu.getIdentitesInternet());
        Hibernate.initialize(elu.getAppartenancesCommissionPermanente());
        Hibernate.initialize(elu.getFonctionsCommissionPermanente());
        Hibernate.initialize(elu.getFonctionsExecutives());
        Hibernate.initialize(elu.getAppartenancesGroupePolitique());
        Hibernate.initialize(elu.getFonctionsGroupePolitique());
        Hibernate.initialize(elu.getAppartenancesCommissionsThematiques());
        Hibernate.initialize(elu.getFonctionsCommissionsThematiques());
        Hibernate.initialize(elu.getAppartenancesOrganismes());
        Hibernate.initialize(elu.getAutreMandats());
        Hibernate.initialize(elu.getDistinctionHonorifiques());
        Map<Long, GroupePolitique> groupesPolitiques = new HashMap<>();
        groupesPolitiques.putAll(elu.getAppartenancesGroupePolitique().stream()
            .map(a -> a.getGroupePolitique())
            // anti-NPE mais ne devrait pas arriver
            .filter(o -> o != null)
            .distinct()
            .collect(Collectors.toMap(GroupePolitique::getId, Function.identity())));
        groupesPolitiques.putAll(elu.getFonctionsGroupePolitique().stream()
            .map(a -> a.getGroupePolitique())
            // anti-NPE mais ne devrait pas arriver
            .filter(o -> o != null)
            .distinct()
            .collect(Collectors.toMap(GroupePolitique::getId, Function.identity())));
        Map<Long, CommissionThematique> commissionsThematiques = new HashMap<>();
        commissionsThematiques.putAll(elu.getAppartenancesCommissionsThematiques().stream()
            .map(a -> a.getCommissionThematique())
            // anti-NPE mais ne devrait pas arriver
            .filter(o -> o != null)
            .distinct()
            .collect(Collectors.toMap(CommissionThematique::getId, Function.identity())));
        commissionsThematiques.putAll(elu.getFonctionsCommissionsThematiques().stream()
            .map(a -> a.getCommissionThematique())
            // anti-NPE mais ne devrait pas arriver
            .filter(o -> o != null)
            .distinct()
            .collect(Collectors.toMap(CommissionThematique::getId, Function.identity())));
        Map<String, Organisme> organismes = elu.getAppartenancesOrganismes().stream()
            .map(a -> a.getCodeRNE())
            .filter(r -> r != null && !r.equals(""))
            .distinct()
            // anti-NPE
            .map(rne -> new Object[]{rne, organismeRepository.findFirstByCodeRNE(rne)})
            .filter(o -> o[1] != null)
            .collect(Collectors.toMap(o -> (String) o[0], o -> (Organisme) o[1]));
        return new EluDTO(elu, groupesPolitiques, commissionsThematiques, organismes);
    }

    @Transactional
    public Elu saveElu(Elu elu) {
        Elu result = eluRepository.save(elu);
        eluSearchRepository.save(elu);
        if (elu.getDateDemission() != null) {
            elu.getAppartenancesCommissionPermanente().forEach(a -> {
                a.setDateFin(elu.getDateDemission());
                a.setMotifFin(elu.getMotifDemission());
                appartenanceCommissionPermanenteRepository.save(a);
            });
            elu.getAppartenancesOrganismes().forEach(a -> {
                a.setDateFin(elu.getDateDemission());
                a.setMotifFin(elu.getMotifDemission());
                appartenanceOrganismeRepository.save(a);
            });
            elu.getAppartenancesCommissionsThematiques().forEach(a -> {
                a.setDateFin(elu.getDateDemission());
                a.setMotifFin(elu.getMotifDemission());
                appartenanceCommissionThematiqueRepository.save(a);
            });
            elu.getAppartenancesGroupePolitique().forEach(a -> {
                a.setDateFin(elu.getDateDemission());
                a.setMotifFin(elu.getMotifDemission());
                appartenanceGroupePolitiqueRepository.save(a);
            });
            elu.getFonctionsCommissionPermanente().forEach(fct -> {
                fct.setDateFin(elu.getDateDemission());
                fct.setMotifFin(elu.getMotifDemission());
                fonctionCommissionPermanenteRepository.save(fct);
            });
            elu.getFonctionsExecutives().forEach(fct -> {
                fct.setDateFin(elu.getDateDemission());
                fct.setMotifFin(elu.getMotifDemission());
                fonctionExecutiveRepository.save(fct);
            });
            elu.getFonctionsGroupePolitique().forEach(fct -> {
                fct.setDateFin(elu.getDateDemission());
                fct.setMotifFin(elu.getMotifDemission());
                fonctionGroupePolitiqueRepository.save(fct);
            });
            elu.getFonctionsCommissionsThematiques().forEach(fct -> {
                fct.setDateFin(elu.getDateDemission());
                fct.setMotifFin(elu.getMotifDemission());
                fonctionCommissionThematiqueRepository.save(fct);
            });
        }
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
    public ExportService.Entry[] getExportEntries() {
        List<EluListDTO> dtos = getAll();
        List<List<String>> elusActifsLines = new ArrayList<>();
        elusActifsLines.add(Arrays.asList("Civilité", "Prénom", "Nom", "Groupe politique", "Profession", "Lieu de naissance",
            "Date de naissance"));
        List<List<String>> elusInactifsLines = new ArrayList<>();
        elusInactifsLines.add(Arrays.asList("Civilité", "Prénom", "Nom", "Groupe politique", "Profession", "Lieu de naissance",
            "Date de naissance", "Date de démission"));
        for (EluListDTO dto : dtos) {
            Elu e = dto.getElu();
            String civilite = e.getCiviliteLabel();
            String groupePolitique = dto.getGroupePolitique() != null ? dto.getGroupePolitique().getNom() :
                "Aucun groupe politique";
            String dateNaissance = e.getDateNaissance() != null ?
                e.getDateNaissance().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de naissance inconnue";
            if (dto.getElu().getDateDemission() == null) {
                elusActifsLines.add(Arrays.asList(civilite, e.getPrenom(), e.getNom(), groupePolitique, e.getProfession(),
                    e.getLieuNaissance(), dateNaissance));
            } else {
                String dateDemission = e.getDateDemission().format(DateTimeFormatter.ISO_LOCAL_DATE);
                elusInactifsLines.add(Arrays.asList(civilite, e.getPrenom(), e.getNom(), groupePolitique, e.getProfession(),
                    e.getLieuNaissance(), dateNaissance, dateDemission));
            }
        }
        return new ExportService.Entry[]{
            new ExportService.Entry("Élus", elusActifsLines),
            new ExportService.Entry("Élus démissionnaires", elusInactifsLines),
            commissionPermanenteService.getFonctionsEntry(dtos)
        };
    }
}
