package openassemblee.service;

import openassemblee.domain.AppartenanceGroupePolitique;
import openassemblee.domain.FonctionGroupePolitique;
import openassemblee.domain.GroupePolitique;
import openassemblee.repository.AdressePostaleRepository;
import openassemblee.repository.AppartenanceGroupePolitiqueRepository;
import openassemblee.repository.FonctionGroupePolitiqueRepository;
import openassemblee.repository.GroupePolitiqueRepository;
import openassemblee.repository.search.AdressePostaleSearchRepository;
import openassemblee.repository.search.AppartenanceGroupePolitiqueSearchRepository;
import openassemblee.repository.search.GroupePolitiqueSearchRepository;
import openassemblee.service.dto.AppartenanceGroupePolitiqueDTO;
import openassemblee.service.dto.FonctionGroupePolitiqueDTO;
import openassemblee.service.dto.GroupePolitiqueDTO;
import openassemblee.service.dto.GroupePolitiqueListDTO;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
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

    @Inject
    private SessionMandatureService sessionMandatureService;

    @Transactional(readOnly = true)
    public List<GroupePolitiqueListDTO> getAll() {
        List<GroupePolitique> list = groupePolitiqueRepository.findByMandature(sessionMandatureService.getMandature());
        return list.stream().map(gp -> {
            List<AppartenanceGroupePolitique> agps =
                appartenanceGroupePolitiqueRepository.findAllByGroupePolitique(gp);
            int count = (int) agps.stream()
                .filter(GroupePolitiqueService::isAppartenanceCourante)
                .count();
            return new GroupePolitiqueListDTO(gp, count);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroupePolitiqueDTO get(Long id) {
        GroupePolitique gp = groupePolitiqueRepository.findOne(id);
        if (gp == null) {
            return null;
        } else {
            Hibernate.initialize(gp.getAdressePostale());
        }
        List<AppartenanceGroupePolitiqueDTO> agpDtos = appartenanceGroupePolitiqueRepository
            .findAllByGroupePolitique(gp).stream()
            .map(a -> new AppartenanceGroupePolitiqueDTO(a, a.getElu()))
            .collect(Collectors.toList());
        List<FonctionGroupePolitiqueDTO> ftpDtps = fonctionGroupePolitiqueRepository
            .findAllByGroupePolitique(gp).stream()
            .map(a -> new FonctionGroupePolitiqueDTO(a, a.getElu()))
            .collect(Collectors.toList());
        return new GroupePolitiqueDTO(gp, agpDtos, ftpDtps);
    }

    // TODO va mériter un super test et une verif pour les dates
    // s'optimise ou... ?
    @Transactional(readOnly = true)
    public static boolean isAppartenanceCourante(AppartenanceGroupePolitique a) {
        // plus tard : || a.getDateFin().isAfter(LocalDate.now())
        return a.getDateFin() == null;
    }

    @Transactional(readOnly = true)
    public static boolean isFonctionCourante(FonctionGroupePolitique f) {
        // plus tard : || f.getDateFin().isAfter(LocalDate.now())
        return f.getDateFin() == null;
    }

    @Transactional
    public GroupePolitique save(GroupePolitique groupePolitique) {
        if (groupePolitique.getAdressePostale() != null) {
            adressePostaleRepository.save(groupePolitique.getAdressePostale());
            adressePostaleSearchRepository.save(groupePolitique.getAdressePostale());
        }
        groupePolitiqueRepository.save(groupePolitique);
        groupePolitiqueSearchRepository.save(groupePolitique);
        return groupePolitique;
    }

    @Transactional
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
