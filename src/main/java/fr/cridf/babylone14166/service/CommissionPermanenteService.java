package fr.cridf.babylone14166.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.cridf.babylone14166.service.dto.FonctionGroupePolitiqueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.service.dto.CommissionPermanenteDTO;

@Service
@Transactional
public class CommissionPermanenteService {

    @Autowired
    private EluRepository eluRepository;
    @Autowired
    private AppartenanceCommissionPermanenteRepository appartenanceCommissionPermanenteRepository;
    @Autowired
    private FonctionCommissionPermanenteRepository fonctionCommissionPermanenteRepository;
    @Autowired
    private FonctionExecutiveRepository fonctionExecutiveRepository;

    public CommissionPermanenteDTO getCommissionPermanente() {
        List<AppartenanceCommissionPermanente> acp = appartenanceCommissionPermanenteRepository.findAll().stream()
            .filter(CommissionPermanenteService::isAppartenanceCourante)
            .collect(Collectors.toList());
        List<FonctionCommissionPermanente> fcp = fonctionCommissionPermanenteRepository.findAll().stream()
            .filter(CommissionPermanenteService::isFonctionCourante)
            .collect(Collectors.toList());
        List<FonctionExecutive> fe = fonctionExecutiveRepository.findAll().stream()
            .filter(CommissionPermanenteService::isFonctionExecutiveCourante)
            .collect(Collectors.toList());
        Set<Long> elusIds = new HashSet<>();
        elusIds.addAll(acp.stream().map(f -> f.getElu().getId()).collect(Collectors.toList()));
        elusIds.addAll(fcp.stream().map(f -> f.getElu().getId()).collect(Collectors.toList()));
        elusIds.addAll(fe.stream().map(f -> f.getElu().getId()).collect(Collectors.toList()));
        Map<Long, Elu> elus = eluRepository.findAll(elusIds).stream().collect(Collectors.toMap(Elu::getId,
            Function.identity()));
        return new CommissionPermanenteDTO(acp, fcp, fe, elus);
    }

    public static boolean isAppartenanceCourante(AppartenanceCommissionPermanente a) {
        // plus tard : || a.getDateFin().isAfter(LocalDate.now())
        return a.getDateFin() == null;
    }

    public static boolean isFonctionCourante(FonctionCommissionPermanente f) {
        // plus tard : || f.getDateFin().isAfter(LocalDate.now())
        return f.getDateFin() == null;
    }

    public static boolean isFonctionExecutiveCourante(FonctionExecutive f) {
        // plus tard : || f.getDateFin().isAfter(LocalDate.now())
        return f.getDateFin() == null;
    }
}
