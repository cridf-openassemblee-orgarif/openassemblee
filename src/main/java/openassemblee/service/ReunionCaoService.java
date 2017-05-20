package openassemblee.service;

import openassemblee.domain.PresenceElu;
import openassemblee.domain.ReunionCao;
import openassemblee.repository.PresenceEluRepository;
import openassemblee.repository.ReunionCaoRepository;
import openassemblee.repository.search.ReunionCaoSearchRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReunionCaoService {

    @Inject
    private ReunionCaoRepository reunionCaoRepository;
    @Inject
    private ReunionCaoSearchRepository reunionCaoSearchRepository;

    @Inject
    private PresenceEluRepository presenceEluRepository;

    @Transactional(readOnly = true)
    public ReunionCao get(Long id) {
        ReunionCao rc = reunionCaoRepository.findOne(id);
        if (rc == null) {
            return null;
        }
        rc.getPresenceElus().forEach(pe -> {
            Hibernate.initialize(pe.getElu().getAppartenancesGroupePolitique());
            Hibernate.initialize(pe.getSignatures());
        });
        return rc;
    }

    @Transactional
    public ReunionCao create(ReunionCao reunionCao) {
        // les id de presenceElu sont settées car sinon le Set Java ne garde qu'une instance
        // donc on les remplace complètement
        Set<PresenceElu> pes = reunionCao.getPresenceElus().stream()
            .map(pe -> {
                pe.setId(null);
                pe = presenceEluRepository.save(pe);
                return pe;
            })
            .collect(Collectors.toSet());
        reunionCao.setPresenceElus(pes);
        ReunionCao result = reunionCaoRepository.save(reunionCao);
        reunionCaoSearchRepository.save(result);
        return result;
    }
}
