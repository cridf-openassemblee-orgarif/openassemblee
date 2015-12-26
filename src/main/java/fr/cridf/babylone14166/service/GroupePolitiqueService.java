package fr.cridf.babylone14166.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.AppartenanceGroupePolitique;
import fr.cridf.babylone14166.domain.GroupePolitique;
import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.repository.search.AdressePostaleSearchRepository;
import fr.cridf.babylone14166.repository.search.GroupePolitiqueSearchRepository;
import fr.cridf.babylone14166.service.dto.*;

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

    @Inject
    private AppartenanceGroupePolitiqueRepository appartenanceGroupePolitiqueRepository;

    public List<GroupePolitiqueListDTO> getAll() {
        List<GroupePolitique> list = groupePolitiqueRepository.findAll();
        return list.stream().map(gp -> {
            List<AppartenanceGroupePolitique> agps =
                appartenanceGroupePolitiqueRepository.findAllByGroupePolitique(gp);
            int count = (int) agps.stream()
                .filter(GroupePolitiqueService::isAppartenanceCourante)
                .count();
            return new GroupePolitiqueListDTO(gp, count);
        }).collect(Collectors.toList());
    }

    public GroupePolitiqueDTO get(Long id) {
        GroupePolitique gp = groupePolitiqueRepository.findOne(id);
        if (gp != null) {
            Hibernate.initialize(gp.getAdressePostale());
        }
        List<AppartenanceGroupePolitique> agps =
            appartenanceGroupePolitiqueRepository.findAllByGroupePolitique(gp);
        List<AppartenanceGroupePolitiqueDTO> agpDtos = agps.stream()
            .filter(GroupePolitiqueService::isAppartenanceCourante)
            .map(a -> new AppartenanceGroupePolitiqueDTO(a, a.getElu()))
            .collect(Collectors.toList());
        return new GroupePolitiqueDTO(gp, agpDtos);
    }

    // TODO va mériter un super test et une verif pour les dates
    // s'optimise ou... ?
    public static boolean isAppartenanceCourante(AppartenanceGroupePolitique a) {
        return a.getDateFin() == null || a.getDateFin().isAfter(LocalDate.now());
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
