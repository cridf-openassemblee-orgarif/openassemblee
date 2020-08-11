package openassemblee.service;

import openassemblee.domain.*;
import openassemblee.domain.enumeration.TypeSeance;
import openassemblee.repository.*;
import openassemblee.repository.search.SeanceSearchRepository;
import openassemblee.service.dto.EluListDTO;
import openassemblee.service.dto.PouvoirListDTO;
import openassemblee.web.rest.dto.SeanceCreationDTO;
import openassemblee.service.dto.SeanceDTO;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
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
    @Inject
    private HemicyclePlanRepository hemicyclePlanRepository;

    @Transactional(readOnly = true)
    public Seance get(Long id) {
        Seance seance = seanceRepository.findOne(id);
        seance.getPresenceElus().forEach(pe -> {
            Hibernate.initialize(pe.getElu().getAppartenancesGroupePolitique());
            Hibernate.initialize(pe.getSignatures());
        });
        return seance;
    }

    @Transactional(readOnly = true)
    public SeanceDTO getDto(Long id) {
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
                EluListDTO eluCedeur = p.getEluCedeur() != null ?
                    eluService.getEluListDTO(p.getEluCedeur().getId(), false, false)
                    : null;
                EluListDTO eluBeneficiaire = p.getEluBeneficiaire() != null ?
                    eluService.getEluListDTO(p.getEluBeneficiaire().getId(), false, false)
                    : null;
                return new PouvoirListDTO(p, eluCedeur, eluBeneficiaire);
            }).collect(Collectors.toList());

        List<GroupePolitique> groupePolitiques = groupePolitiqueRepository.findAll();
        HemicyclePlan hp = hemicyclePlanRepository.findOneBySeance(seance);
        return new SeanceDTO(seance, pouvoirs, groupePolitiques, hp != null ? hp.getId() : null);
    }

    @Transactional(readOnly = true)
    public List<Pouvoir> getPouvoirsFromSeanceId(Long id) {
        Seance seance = seanceRepository.findOne(id);
        if (seance == null) {
            return new ArrayList<>();
        }
        List<Pouvoir> pouvoirs = pouvoirRepository.findAllBySeance(seance);
        pouvoirs.forEach(p -> {
            Hibernate.initialize(p.getEluCedeur().getAppartenancesGroupePolitique());
            Hibernate.initialize(p.getEluBeneficiaire().getAppartenancesGroupePolitique());
        });
        return pouvoirs;
    }

    @Transactional
    public Seance create(SeanceCreationDTO seance) {
        // FIXME demo ici, pour le coup, c'est un vrai problème si on a pas fixé le type de seance
        // de la possibilité de supprimer des présence ?
        List<Elu> elus = seance.getSeance().getType() == TypeSeance.COMMISSION_PERMANENTE ? eluService.getCommissionPermanente() :
            eluService.getActifsAssemblee();
        Set<PresenceElu> pes = elus.stream().map(e -> {
            PresenceElu pe = new PresenceElu();
            pe.setElu(e);
            return presenceEluRepository.save(pe);
        }).collect(Collectors.toSet());
        seance.getSeance().setPresenceElus(pes);
        Seance result = seanceRepository.save(seance.getSeance());
        seanceSearchRepository.save(result);
        HemicyclePlan hemicyclePlan = new HemicyclePlan();
        hemicyclePlan.setSeance(seance.getSeance());
        if(seance.getSeancePlanId() != null) {
            Seance s = seanceRepository.findOne(seance.getProjetPlanId());
            HemicyclePlan hp = hemicyclePlanRepository.findOneBySeance(s);
            hemicyclePlan.setConfiguration(hp.getConfiguration());
            hemicyclePlan.setJsonPlan(hp.getJsonPlan());
        }
        hemicyclePlanRepository.save(hemicyclePlan);
        return result;
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
