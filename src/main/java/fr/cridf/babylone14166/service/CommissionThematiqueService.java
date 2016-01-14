package fr.cridf.babylone14166.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.AppartenanceCommissionThematique;
import fr.cridf.babylone14166.domain.CommissionThematique;
import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.repository.search.AdressePostaleSearchRepository;
import fr.cridf.babylone14166.repository.search.CommissionThematiqueSearchRepository;
import fr.cridf.babylone14166.service.dto.*;

@Service
@Transactional
public class CommissionThematiqueService {

    @Inject
    private CommissionThematiqueRepository commissionThematiqueRepository;
    @Inject
    private CommissionThematiqueSearchRepository commissionThematiqueSearchRepository;

    @Inject
    private AdressePostaleRepository adressePostaleRepository;
    @Inject
    private AdressePostaleSearchRepository adressePostaleSearchRepository;

    @Inject
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;

    public List<CommissionThematiqueListDTO> getAll() {
        List<CommissionThematique> list = commissionThematiqueRepository.findAll();
        return list.stream().map(gp -> {
            List<AppartenanceCommissionThematique> acts =
                appartenanceCommissionThematiqueRepository.findAllByCommissionThematique(gp);
            int count = (int) acts.stream()
                .filter(CommissionThematiqueService::isAppartenanceCourante)
                .count();
            return new CommissionThematiqueListDTO(gp, count);
        }).collect(Collectors.toList());
    }

    public CommissionThematiqueDTO get(Long id) {
        CommissionThematique ct = commissionThematiqueRepository.findOne(id);
        List<AppartenanceCommissionThematique> acts =
            appartenanceCommissionThematiqueRepository.findAllByCommissionThematique(ct);
        List<AppartenanceCommissionThematiqueDTO> agpDtos = acts.stream()
            .filter(CommissionThematiqueService::isAppartenanceCourante)
            .map(a -> new AppartenanceCommissionThematiqueDTO(a, a.getElu()))
            .collect(Collectors.toList());
        return new CommissionThematiqueDTO(ct, acts);
    }

    // TODO va mériter un super test et une verif pour les dates
    // s'optimise ou... ?
    public static boolean isAppartenanceCourante(AppartenanceCommissionThematique a) {
        return a.getDateFin() == null || a.getDateFin().isAfter(LocalDate.now());
    }

}
