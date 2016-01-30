package fr.cridf.babylone14166.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.*;
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
    @Inject
    private FonctionCommissionThematiqueRepository fonctionCommissionThematiqueRepository;

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
        if(ct == null) {
            return null;
        }
        List<AppartenanceCommissionThematique> acts =
            appartenanceCommissionThematiqueRepository.findAllByCommissionThematique(ct);
        List<AppartenanceCommissionThematiqueDTO> actDtos = acts.stream()
            .filter(CommissionThematiqueService::isAppartenanceCourante)
            .map(a -> new AppartenanceCommissionThematiqueDTO(a, a.getElu()))
            .collect(Collectors.toList());
        List<FonctionCommissionThematique> fcts = fonctionCommissionThematiqueRepository
            .findAllByCommissionThematique(ct);
        List<FonctionCommissionThematiqueDTO> fctDtos = fcts.stream()
            .filter(CommissionThematiqueService::isAppartenanceCourante)
            .map(a -> new FonctionCommissionThematiqueDTO(a, a.getElu()))
            .collect(Collectors.toList());
        return new CommissionThematiqueDTO(ct, actDtos, fctDtos);
    }

    // TODO va mériter un super test et une verif pour les dates
    // s'optimise ou... ?
    public static boolean isAppartenanceCourante(AppartenanceCommissionThematique a) {
        // later remettre || a.getDateFin().isAfter(LocalDate.now());
        return a.getDateFin() == null;
    }

    public static boolean isAppartenanceCourante(FonctionCommissionThematique f) {
        // later remettre || a.getDateFin().isAfter(LocalDate.now());
        return f.getDateFin() == null;
    }

}
