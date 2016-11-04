package fr.cridf.babylone14166.service;

import fr.cridf.babylone14166.domain.Organisme;
import fr.cridf.babylone14166.repository.AppartenanceOrganismeRepository;
import fr.cridf.babylone14166.repository.OrganismeRepository;
import fr.cridf.babylone14166.service.dto.AppartenanceOrganismeDTO;
import fr.cridf.babylone14166.service.dto.OrganismeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class OrganismeService {

    @Inject
    private OrganismeRepository organismeRepository;

    @Inject
    private AppartenanceOrganismeRepository appartenanceOrganismeRepository;

    @Inject
    private EluService eluService;

    public OrganismeDTO get(long id) {
        Organisme organisme = organismeRepository.getOne(id);
        if(organisme == null) {
            return null;
        }
        List<AppartenanceOrganismeDTO> appartenances = appartenanceOrganismeRepository.findAllByCodeRNE(organisme.getCodeRNE()).stream()
            .map(a -> new AppartenanceOrganismeDTO(a, eluService.getEluListDTO(a.getElu().getId())))
            .collect(Collectors.toList());
        return new OrganismeDTO(organisme, appartenances);
    }

}
