package fr.cridf.babylone14166.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.repository.search.*;
import fr.cridf.babylone14166.service.dto.EluDTO;
import fr.cridf.babylone14166.service.dto.EluListDTO;

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
            return new EluListDTO(e, groupePolitique.get());
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
            .distinct()
            .collect(Collectors.toMap(GroupePolitique::getId, Function.identity())));
        groupesPolitiques.putAll(elu.getFonctionsGroupePolitique().stream()
            .map(a -> a.getGroupePolitique())
            .distinct()
            .collect(Collectors.toMap(GroupePolitique::getId, Function.identity())));
        Map<Long, CommissionThematique> commissionsThematiques = new HashMap<>();
        commissionsThematiques.putAll(elu.getAppartenancesCommissionsThematiques().stream()
            .map(a -> a.getCommissionThematique())
            .distinct()
            .collect(Collectors.toMap(CommissionThematique::getId, Function.identity())));
        commissionsThematiques.putAll(elu.getFonctionsCommissionsThematiques().stream()
            .map(a -> a.getCommissionThematique())
            .distinct()
            .collect(Collectors.toMap(CommissionThematique::getId, Function.identity())));
        Map<String, Organisme> organismes = elu.getAppartenancesOrganismes().stream()
            .map(a -> a.getCodeRNE())
            .distinct()
            .collect(Collectors.toMap(Function.identity(), organismeRepository::findOneByCodeRNE));
        return new EluDTO(elu, groupesPolitiques, commissionsThematiques, organismes);
    }

    public Elu save(Elu elu) {
        save(elu.getNumerosTelephones(), numeroTelephoneRepository, numeroTelephoneSearchRepository);
        save(elu.getAdressesPostales(), adressePostaleRepository, adressePostaleSearchRepository);
        save(elu.getNumerosFax(), numeroFaxRepository, numeroFaxSearchRepository);
        save(elu.getAdressesMail(), adresseMailRepository, adresseMailSearchRepository);
        save(elu.getIdentitesInternet(), identiteInternetRepository, identiteInternetSearchRepository);
        eluRepository.save(elu);
        eluSearchRepository.save(elu);
        return elu;
    }

    private <T> void save(List<T> entities, JpaRepository<T, Long> repository,
        ElasticsearchRepository<T, Long> searchRepository) {
        if (entities != null) {
            repository.save(entities);
            for (T t : entities) {
                searchRepository.save(t);
            }
        }
    }
}
