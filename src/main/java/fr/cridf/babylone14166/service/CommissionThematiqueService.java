package fr.cridf.babylone14166.service;

import fr.cridf.babylone14166.domain.AppartenanceCommissionThematique;
import fr.cridf.babylone14166.domain.CommissionThematique;
import fr.cridf.babylone14166.domain.FonctionCommissionThematique;
import fr.cridf.babylone14166.repository.AdressePostaleRepository;
import fr.cridf.babylone14166.repository.AppartenanceCommissionThematiqueRepository;
import fr.cridf.babylone14166.repository.CommissionThematiqueRepository;
import fr.cridf.babylone14166.repository.FonctionCommissionThematiqueRepository;
import fr.cridf.babylone14166.repository.search.AdressePostaleSearchRepository;
import fr.cridf.babylone14166.repository.search.CommissionThematiqueSearchRepository;
import fr.cridf.babylone14166.service.dto.AppartenanceCommissionThematiqueDTO;
import fr.cridf.babylone14166.service.dto.CommissionThematiqueDTO;
import fr.cridf.babylone14166.service.dto.CommissionThematiqueListDTO;
import fr.cridf.babylone14166.service.dto.FonctionCommissionThematiqueDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public CommissionThematiqueDTO get(Long id) {
        CommissionThematique ct = commissionThematiqueRepository.findOne(id);
        if(ct == null) {
            return null;
        }
        List<AppartenanceCommissionThematiqueDTO> actDtos = appartenanceCommissionThematiqueRepository
            .findAllByCommissionThematique(ct).stream()
            .map(a -> new AppartenanceCommissionThematiqueDTO(a, a.getElu()))
            .collect(Collectors.toList());
        List<FonctionCommissionThematiqueDTO> fctDtos = fonctionCommissionThematiqueRepository
            .findAllByCommissionThematique(ct).stream()
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
