package fr.cridf.babylone14166.service;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.repository.search.*;
import fr.cridf.babylone14166.service.dto.EluDTO;
import fr.cridf.babylone14166.service.dto.EluListDTO;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class EluService {

    private List<EluDTO> all;
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
    private OrganismeRepository organismeRepository;

    @Inject
    private AppartenanceGroupePolitiqueRepository appartenanceGroupePolitiqueRepository;

    public List<EluListDTO> getAll() {
        List<Elu> elus = eluRepository.findAll();
        return elus.stream().map(e -> {
            List<AppartenanceGroupePolitique> agps = appartenanceGroupePolitiqueRepository.findAllByElu(e);
            Optional<GroupePolitique> groupePolitique = agps.stream()
                .filter(GroupePolitiqueService::isAppartenanceCourante)
                .map(AppartenanceGroupePolitique::getGroupePolitique)
                .findFirst();
            if (groupePolitique.isPresent()) {
                return new EluListDTO(e, groupePolitique.get());
            } else {
                return new EluListDTO(e);
            }
        }).collect(Collectors.toList());
    }

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
            .map(rne -> new Object[]{rne, organismeRepository.findOneByCodeRNE(rne)})
            .filter(o -> o[1] != null)
            .collect(Collectors.toMap(o -> (String) o[0], o -> (Organisme) o[1]));
        return new EluDTO(elu, groupesPolitiques, commissionsThematiques, organismes);
    }

    public void saveAdresseMail(Long id, AdresseMail adresseMail) {
        adresseMailRepository.save(adresseMail);
        adresseMailSearchRepository.save(adresseMail);
        Elu elu = eluRepository.getOne(id);
        elu.getAdressesMail().add(adresseMail);
        eluRepository.save(elu);
    }

    public void saveAdressePostale(long id, AdressePostale adressePostale) {
        adressePostaleRepository.save(adressePostale);
        adressePostaleSearchRepository.save(adressePostale);
        Elu elu = eluRepository.getOne(id);
        elu.getAdressesPostales().add(adressePostale);
        eluRepository.save(elu);
    }

    public void updateAdressePostale(AdressePostale adressePostale) {
        adressePostaleRepository.save(adressePostale);
        adressePostaleSearchRepository.save(adressePostale);
    }

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

    public void saveIdentiteInternet(Long id, IdentiteInternet identiteInternet) {
        identiteInternetRepository.save(identiteInternet);
        identiteInternetSearchRepository.save(identiteInternet);
        Elu elu = eluRepository.getOne(id);
        elu.getIdentitesInternet().add(identiteInternet);
        eluRepository.save(elu);
    }

    public void saveNumeroFax(Long id, NumeroFax numeroFax) {
        numeroFaxRepository.save(numeroFax);
        numeroFaxSearchRepository.save(numeroFax);
        Elu elu = eluRepository.getOne(id);
        elu.getNumerosFax().add(numeroFax);
        eluRepository.save(elu);
    }

    public void saveNumeroTelephone(Long id, NumeroTelephone numeroTelephone) {
        numeroTelephoneRepository.save(numeroTelephone);
        numeroTelephoneSearchRepository.save(numeroTelephone);
        Elu elu = eluRepository.getOne(id);
        elu.getNumerosTelephones().add(numeroTelephone);
        eluRepository.save(elu);
    }

}
