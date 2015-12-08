package fr.cridf.babylone14166.service;

import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.repository.search.*;

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
        }
        return elu;
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
