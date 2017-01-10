package fr.cridf.babylone14166.service;

import fr.cridf.babylone14166.domain.PresenceElu;
import fr.cridf.babylone14166.domain.Seance;
import fr.cridf.babylone14166.repository.PouvoirRepository;
import fr.cridf.babylone14166.repository.PresenceEluRepository;
import fr.cridf.babylone14166.repository.SeanceRepository;
import fr.cridf.babylone14166.repository.search.SeanceSearchRepository;
import fr.cridf.babylone14166.service.dto.EluListDTO;
import fr.cridf.babylone14166.service.dto.PouvoirListDTO;
import fr.cridf.babylone14166.service.dto.SeanceDTO;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

// FIXME une incohérence possible
// j'enleve une présence
// et le mec a fait des pouvoirs / signatures
@Service
public class SeanceService {

    @Inject
    private SeanceRepository seanceRepository;
    @Inject
    private PouvoirRepository pouvoirRepository;
    @Inject
    private EluService eluService;
    @Inject
    private SeanceSearchRepository seanceSearchRepository;
    @Inject
    private AuditTrailService auditTrailService;
    @Inject
    private PresenceEluRepository presenceEluRepository;

    @Transactional(readOnly = true)
    public SeanceDTO get(Long id) {
        Seance seance = seanceRepository.findOne(id);
        if (seance == null) {
            return null;
        }
        seance.getPresenceElus().forEach(pe -> Hibernate.initialize(pe.getElu().getAppartenancesGroupePolitique()));
        List<PouvoirListDTO> pouvoirs = pouvoirRepository.findAllByDateDebut(seance.getDate())
            .stream().map(p -> {
                EluListDTO eluCedeur = eluService.getEluListDTO(p.getEluCedeur().getId());
                EluListDTO eluBeneficiaire = eluService.getEluListDTO(p.getEluBeneficiaire().getId());
                return new PouvoirListDTO(p, eluCedeur, eluBeneficiaire);
            }).collect(Collectors.toList());
        return new SeanceDTO(seance, pouvoirs);
    }

    @Transactional
    public Seance create(Seance seance) {
        Seance result = seanceRepository.save(seance);
        seanceSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        eluService.getAllActifsAssemblee().forEach(e -> {
            PresenceElu pe = new PresenceElu();
            pe.setElu(e);
            pe.setSeance(result);
            presenceEluRepository.save(pe);
        });
        return seance;
    }

    @Transactional
    public Seance update(Seance seance) {
        // FIXME si le nombre de signature est différent...
        Seance result = seanceRepository.save(seance);
        seanceSearchRepository.save(seance);
        auditTrailService.logUpdate(result, result.getId());
        return seance;
    }

    // TODONOW il faut modifier tous les pouvoirs
    // et virer les presence
    // et virer les signatures
    @Transactional
    public void delete(Long id) {
        seanceRepository.delete(id);
        seanceSearchRepository.delete(id);
        auditTrailService.logDeletion(Seance.class, id);
    }
}
