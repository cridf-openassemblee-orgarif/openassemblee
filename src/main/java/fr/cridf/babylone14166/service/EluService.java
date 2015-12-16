package fr.cridf.babylone14166.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.repository.search.*;
import fr.cridf.babylone14166.service.dto.EluCompletDTO;

@Service
@Transactional
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

    public Elu get(Long id) {
        Elu elu = eluRepository.findOne(id);
        if (elu != null) {
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
        }
        return elu;
    }

    public EluCompletDTO getEluComplet(Long id) {
        Elu elu = get(id);
        if (elu == null) {
            return null;
        }
        Map<Long, GroupePolitique> groupesPolitiques = new HashMap<>();
        groupesPolitiques.putAll(elu.getAppartenancesGroupePolitique().stream()
            .map(a -> a.getGroupePolitique())
            .collect(Collectors.toSet())
            .stream().collect(Collectors.toMap(GroupePolitique::getId, Function.identity())));
        groupesPolitiques.putAll(elu.getFonctionsGroupePolitique().stream()
            .map(a -> a.getGroupePolitique())
            .collect(Collectors.toSet())
            .stream().collect(Collectors.toMap(GroupePolitique::getId, Function.identity())));
        Map<Long, CommissionThematique> commissionsThematiques = new HashMap<>();
        commissionsThematiques.putAll(elu.getAppartenancesCommissionsThematiques().stream()
            .map(a -> a.getCommissionThematique())
            .collect(Collectors.toSet())
            .stream().collect(Collectors.toMap(CommissionThematique::getId, Function.identity())));
        commissionsThematiques.putAll(elu.getFonctionsCommissionsThematiques().stream()
            .map(a -> a.getCommissionThematique())
            .collect(Collectors.toSet())
            .stream().collect(Collectors.toMap(CommissionThematique::getId, Function.identity())));
        return new EluCompletDTO(elu, groupesPolitiques, commissionsThematiques);
    }

    public Elu save(Elu elu) {
        if (elu.getNumerosTelephones() != null) {
            for (NumeroTelephone t : elu.getNumerosTelephones()) {
                numeroTelephoneRepository.save(t);
                numeroTelephoneSearchRepository.save(t);
            }
        }
        if (elu.getAdressesPostales() != null) {
            for (AdressePostale a : elu.getAdressesPostales()) {
                adressePostaleRepository.save(a);
                adressePostaleSearchRepository.save(a);
            }
        }
        if (elu.getNumerosFax() != null) {
            for (NumeroFax numeroFax : elu.getNumerosFax()) {
                numeroFaxRepository.save(numeroFax);
                numeroFaxSearchRepository.save(numeroFax);
            }
        }
        if (elu.getAdressesMail() != null) {
            for (AdresseMail adresseMail : elu.getAdressesMail()) {
                adresseMailRepository.save(adresseMail);
                adresseMailSearchRepository.save(adresseMail);
            }
        }
        if (elu.getIdentitesInternet() != null) {
            for (IdentiteInternet identiteInternet : elu.getIdentitesInternet()) {
                identiteInternetRepository.save(identiteInternet);
                identiteInternetSearchRepository.save(identiteInternet);
            }
        }
        eluRepository.save(elu);
        eluSearchRepository.save(elu);
        return elu;
    }
}
