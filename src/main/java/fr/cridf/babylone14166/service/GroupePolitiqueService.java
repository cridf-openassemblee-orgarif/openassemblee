package fr.cridf.babylone14166.service;

import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.GroupePolitique;
import fr.cridf.babylone14166.repository.AdressePostaleRepository;
import fr.cridf.babylone14166.repository.GroupePolitiqueRepository;
import fr.cridf.babylone14166.repository.search.AdressePostaleSearchRepository;
import fr.cridf.babylone14166.repository.search.GroupePolitiqueSearchRepository;

@Service
@Transactional
public class GroupePolitiqueService {

    @Inject
    private GroupePolitiqueRepository groupePolitiqueRepository;
    @Inject
    private GroupePolitiqueSearchRepository groupePolitiqueSearchRepository;

    @Inject
    private AdressePostaleRepository adressePostaleRepository;
    @Inject
    private AdressePostaleSearchRepository adressePostaleSearchRepository;

    public GroupePolitique get(Long id) {
        GroupePolitique groupePolitique = groupePolitiqueRepository.findOne(id);
        if (groupePolitique != null) {
            Hibernate.initialize(groupePolitique.getAdressePostale());
        }
        return groupePolitique;
    }

    public GroupePolitique save(GroupePolitique groupePolitique) {
        if (groupePolitique.getAdressePostale() != null) {
            adressePostaleRepository.save(groupePolitique.getAdressePostale());
            adressePostaleSearchRepository.save(groupePolitique.getAdressePostale());
        }
        groupePolitiqueRepository.save(groupePolitique);
        groupePolitiqueSearchRepository.save(groupePolitique);
        return groupePolitique;
    }

}
