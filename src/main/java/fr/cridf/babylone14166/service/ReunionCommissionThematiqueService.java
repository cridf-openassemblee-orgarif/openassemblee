package fr.cridf.babylone14166.service;

import fr.cridf.babylone14166.domain.ReunionCommissionThematique;
import fr.cridf.babylone14166.repository.ReunionCommissionThematiqueRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class ReunionCommissionThematiqueService {

    @Inject
    private ReunionCommissionThematiqueRepository reunionCommissionThematiqueRepository;

    @Transactional(readOnly = true)
    public ReunionCommissionThematique get(Long id) {
        ReunionCommissionThematique rct = reunionCommissionThematiqueRepository.findOne(id);
        if (rct == null) {
            return null;
        }
        Hibernate.initialize(rct.getCommissionsThematiques());
        return rct;
    }
}
