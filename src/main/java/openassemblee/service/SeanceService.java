package openassemblee.service;

import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;
import openassemblee.domain.PresenceElu;
import openassemblee.domain.Seance;
import openassemblee.repository.GroupePolitiqueRepository;
import openassemblee.repository.PouvoirRepository;
import openassemblee.repository.PresenceEluRepository;
import openassemblee.repository.SeanceRepository;
import openassemblee.repository.search.SeanceSearchRepository;
import openassemblee.service.dto.EluListDTO;
import openassemblee.service.dto.PouvoirListDTO;
import openassemblee.service.dto.SeanceDTO;
import openassemblee.domain.enumeration.TypeSeance;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
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
    private PresenceEluRepository presenceEluRepository;
    @Inject
    private GroupePolitiqueRepository groupePolitiqueRepository;

    @Transactional(readOnly = true)
    public SeanceDTO get(Long id) {
        Seance seance = seanceRepository.findOne(id);
        if (seance == null) {
            return null;
        }
        seance.getPresenceElus().forEach(pe -> {
            Hibernate.initialize(pe.getElu().getAppartenancesGroupePolitique());
            Hibernate.initialize(pe.getSignatures());
        });
        List<PouvoirListDTO> pouvoirs = pouvoirRepository.findAllBySeance(seance)
            .stream().map(p -> {
                EluListDTO eluCedeur = eluService.getEluListDTO(p.getEluCedeur().getId());
                EluListDTO eluBeneficiaire = eluService.getEluListDTO(p.getEluBeneficiaire().getId());
                return new PouvoirListDTO(p, eluCedeur, eluBeneficiaire);
            }).collect(Collectors.toList());
        List<GroupePolitique> groupePolitiques = groupePolitiqueRepository.findAll();
        return new SeanceDTO(seance, pouvoirs, groupePolitiques);
    }

    @Transactional
    public Seance create(Seance seance) {
        // FIXME demo ici, pour le coup, c'est un vrai problème si on a pas fixé le type de seance
        // de la possibilité de supprimer des présence ?
        List<Elu> elus = seance.getType() == TypeSeance.COMMISSION_PERMANENTE ? eluService.getCommissionPermanente() :
            eluService.getActifsAssemblee();
        Set<PresenceElu> pes = elus.stream().map(e -> {
            PresenceElu pe = new PresenceElu();
            pe.setElu(e);
            return presenceEluRepository.save(pe);
        }).collect(Collectors.toSet());
        seance.setPresenceElus(pes);
        Seance result = seanceRepository.save(seance);
        seanceSearchRepository.save(result);
        return seance;
    }

    @Transactional
    public Seance update(Seance seance) {
        // FIXME si le nombre de signature est différent...
        Seance result = seanceRepository.save(seance);
        seanceSearchRepository.save(seance);
        return seance;
    }

    // TODONOW il faut modifier tous les pouvoirs
    // et virer les presence
    // et virer les signatures
    @Transactional
    public void delete(Long id) {
        seanceRepository.delete(id);
        seanceSearchRepository.delete(id);
    }
}
