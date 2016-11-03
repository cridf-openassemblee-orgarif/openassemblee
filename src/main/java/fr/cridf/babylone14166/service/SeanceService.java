package fr.cridf.babylone14166.service;

import fr.cridf.babylone14166.domain.Seance;
import fr.cridf.babylone14166.repository.PouvoirRepository;
import fr.cridf.babylone14166.repository.SeanceRepository;
import fr.cridf.babylone14166.service.dto.EluListDTO;
import fr.cridf.babylone14166.service.dto.PouvoirListDTO;
import fr.cridf.babylone14166.service.dto.SeanceDTO;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeanceService {

    @Inject
    private SeanceRepository seanceRepository;
    @Inject
    private PouvoirRepository pouvoirRepository;
    @Inject
    private EluService eluService;

    public SeanceDTO get(Long id) {
        Seance seance = seanceRepository.findOne(id);
        if (seance == null) {
            return null;
        }
        List<PouvoirListDTO> pouvoirs = pouvoirRepository.findAllByDateDebut(seance.getDate())
            .stream().map(p -> {
            EluListDTO eluCedeur = eluService.getEluListDTO(p.getEluCedeur().getId());
            EluListDTO eluBeneficiaire = eluService.getEluListDTO(p.getEluBeneficiaire().getId());
            return new PouvoirListDTO(p, eluCedeur, eluBeneficiaire);
        }).collect(Collectors.toList());
        return new SeanceDTO(seance, pouvoirs);
    }

}
