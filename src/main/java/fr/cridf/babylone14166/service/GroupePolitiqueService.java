package fr.cridf.babylone14166.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.repository.search.*;
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
    @Inject
    private AppartenanceGroupePolitiqueSearchRepository appartenanceGroupePolitiqueSearchRepository;
    @Inject
    private FonctionGroupePolitiqueRepository fonctionGroupePolitiqueRepository;

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
        List<FonctionGroupePolitique> ftps =
            fonctionGroupePolitiqueRepository.findAllByGroupePolitique(gp);
        List<FonctionGroupePolitiqueDTO> ftpDtps = ftps.stream()
            .filter(GroupePolitiqueService::isFonctionCourante)
            .map(a -> new FonctionGroupePolitiqueDTO(a, a.getElu()))
            .collect(Collectors.toList());
        return new GroupePolitiqueDTO(gp, agpDtos, ftpDtps);
    }

    // TODO va mériter un super test et une verif pour les dates
    // s'optimise ou... ?
    public static boolean isAppartenanceCourante(AppartenanceGroupePolitique a) {
        // plus tard : || a.getDateFin().isAfter(LocalDate.now())
        return a.getDateFin() == null;
    }

    public static boolean isFonctionCourante(FonctionGroupePolitique f) {
        // plus tard : || f.getDateFin().isAfter(LocalDate.now())
        return f.getDateFin() == null;
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

    public void sortirElus(GroupePolitique groupePolitique) {
        List<AppartenanceGroupePolitique> agps = appartenanceGroupePolitiqueRepository
            .findAllByGroupePolitique(groupePolitique);
        agps.stream().forEach(agp -> {
            agp.setDateFin(groupePolitique.getDateFin());
            agp.setMotifFin(groupePolitique.getMotifFin());
            appartenanceGroupePolitiqueRepository.save(agp);
            appartenanceGroupePolitiqueSearchRepository.save(agp);
        });
    }

}
