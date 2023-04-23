package openassemblee.service.inconsistency;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import openassemblee.domain.Pouvoir;
import openassemblee.domain.Seance;
import openassemblee.repository.PouvoirRepository;
import openassemblee.web.rest.dto.InconsistenciesDTO;
import openassemblee.web.rest.dto.InconsistencyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InconsistencyService {

    @Autowired
    private PouvoirRepository pouvoirRepository;

    @Transactional(readOnly = true)
    public List<InconsistenciesDTO> getInconsistencies() {
        List<InconsistenciesDTO> inconsistencies = new ArrayList<>();

        //        Pouvoir heures à la con
        List<Pouvoir> pouvoirs = pouvoirRepository.findAll();
        //        List<Pouvoir> missingBeneficiaire =
        inconsistencies.add(
            new InconsistenciesDTO(
                InconsistenciesDTO.InconsistencyCategory.POUVOIR_BENEFICIAIRE_MANQUANT,
                pouvoirs
                    .stream()
                    .filter(p -> p.getEluBeneficiaire() == null)
                    .map(this::pouvoirInconsistency)
                    .collect(Collectors.toList())
            )
        );
        inconsistencies.add(
            new InconsistenciesDTO(
                InconsistenciesDTO.InconsistencyCategory.POUVOIR_CEDEUR_MANQUANT,
                pouvoirs
                    .stream()
                    .filter(p -> p.getEluCedeur() == null)
                    .map(this::pouvoirInconsistency)
                    .collect(Collectors.toList())
            )
        );

        return inconsistencies;
    }

    private InconsistencyDTO pouvoirInconsistency(Pouvoir p) {
        return new InconsistencyDTO(
            p.getClass(),
            p.getId(),
            Seance.class,
            p.getSeance().getId()
        );
    }
}
