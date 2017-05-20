package openassemblee.service;

import openassemblee.domain.Organisme;
import openassemblee.repository.AppartenanceOrganismeRepository;
import openassemblee.repository.OrganismeRepository;
import openassemblee.service.dto.AppartenanceOrganismeDTO;
import openassemblee.service.dto.OrganismeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrganismeService {

    @Inject
    private OrganismeRepository organismeRepository;

    @Inject
    private AppartenanceOrganismeRepository appartenanceOrganismeRepository;

    @Inject
    private EluService eluService;

    @Transactional(readOnly = true)
    public OrganismeDTO get(long id) {
        Organisme organisme = organismeRepository.getOne(id);
        if (organisme == null) {
            return null;
        }
        List<AppartenanceOrganismeDTO> appartenances = appartenanceOrganismeRepository.findAllByCodeRNE(organisme.getCodeRNE()).stream()
            .map(a -> new AppartenanceOrganismeDTO(a, eluService.getEluListDTO(a.getElu().getId())))
            .collect(Collectors.toList());
        return new OrganismeDTO(organisme, appartenances);
    }

}
