package fr.cridf.babylone14166.service;

import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.AdressePostale;
import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.repository.AdressePostaleRepository;
import fr.cridf.babylone14166.repository.EluRepository;

@Service
@Transactional
public class EluService {

    @Inject
    private EluRepository eluRepository;

    @Inject
    private AdressePostaleRepository adressePostaleRepository;

    public Elu get(Long id) {
        Elu elu = eluRepository.findOne(id);
        if (elu != null) {
            Hibernate.initialize(elu.getAdressesPostales());
        }
        return elu;
    }

    public Elu save(Elu elu) {
        if (elu.getAdressesPostales() != null) {
            for (AdressePostale a : elu.getAdressesPostales()) {
                adressePostaleRepository.save(a);
            }
        }
        return eluRepository.save(elu);
    }
}
