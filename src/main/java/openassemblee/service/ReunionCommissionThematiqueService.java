package openassemblee.service;

import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import openassemblee.domain.AppartenanceCommissionThematique;
import openassemblee.domain.Elu;
import openassemblee.domain.PresenceElu;
import openassemblee.domain.ReunionCommissionThematique;
import openassemblee.repository.AppartenanceCommissionThematiqueRepository;
import openassemblee.repository.PresenceEluRepository;
import openassemblee.repository.ReunionCommissionThematiqueRepository;
import openassemblee.repository.search.ReunionCommissionThematiqueSearchRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReunionCommissionThematiqueService {

    @Inject
    private ReunionCommissionThematiqueRepository reunionCommissionThematiqueRepository;

    @Inject
    private ReunionCommissionThematiqueSearchRepository reunionCommissionThematiqueSearchRepository;

    @Inject
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;

    @Inject
    private PresenceEluRepository presenceEluRepository;

    @Transactional(readOnly = true)
    public ReunionCommissionThematique get(Long id) {
        ReunionCommissionThematique rct =
            reunionCommissionThematiqueRepository.findOne(id);
        if (rct == null) {
            return null;
        }
        Hibernate.initialize(rct.getCommissionsThematiques());
        rct
            .getPresenceElus()
            .forEach(pe -> {
                Hibernate.initialize(
                    pe.getElu().getAppartenancesGroupePolitique()
                );
                Hibernate.initialize(pe.getSignatures());
            });
        return rct;
    }

    @Transactional
    public ReunionCommissionThematique create(
        ReunionCommissionThematique reunionCommissionThematique
    ) {
        Set<Elu> elus = reunionCommissionThematique
            .getCommissionsThematiques()
            .stream()
            .flatMap(ct ->
                appartenanceCommissionThematiqueRepository
                    .findAllByCommissionThematique(ct)
                    .stream()
            )
            .filter(a -> a.getDateFin() == null)
            .map(AppartenanceCommissionThematique::getElu)
            .collect(Collectors.toSet());
        Set<PresenceElu> pes = elus
            .stream()
            .map(e -> {
                PresenceElu pe = new PresenceElu();
                pe.setElu(e);
                return presenceEluRepository.save(pe);
            })
            .collect(Collectors.toSet());
        reunionCommissionThematique.setPresenceElus(pes);
        ReunionCommissionThematique result =
            reunionCommissionThematiqueRepository.save(
                reunionCommissionThematique
            );
        reunionCommissionThematiqueSearchRepository.save(result);
        return result;
    }
}
