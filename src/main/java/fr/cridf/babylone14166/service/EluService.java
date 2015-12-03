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
    private AdressePostaleRepository adressePostaleRepository;

    @Inject
    private NumeroTelephoneRepository numeroTelephoneRepository;

    @Inject
    private EluSearchRepository eluSearchRepository;

    @Inject
    private AdressePostaleSearchRepository adressePostaleSearchRepository;

    @Inject
    private NumeroTelephoneSearchRepository numeroTelephoneSearchRepository;

    public Elu get(Long id) {
        Elu elu = eluRepository.findOne(id);
        if (elu != null) {
            Hibernate.initialize(elu.getAdressesPostales());
            Hibernate.initialize(elu.getNumerosTelephones());
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
                adressePostaleSearchRepository.save(a);
                adressePostaleRepository.save(a);
            }
        }
        eluSearchRepository.save(elu);
        return eluRepository.save(elu);
    }
}
