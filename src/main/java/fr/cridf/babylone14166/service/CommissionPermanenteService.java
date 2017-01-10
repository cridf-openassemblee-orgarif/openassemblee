package fr.cridf.babylone14166.service;

import fr.cridf.babylone14166.domain.AppartenanceCommissionPermanente;
import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.domain.FonctionCommissionPermanente;
import fr.cridf.babylone14166.domain.FonctionExecutive;
import fr.cridf.babylone14166.repository.AppartenanceCommissionPermanenteRepository;
import fr.cridf.babylone14166.repository.EluRepository;
import fr.cridf.babylone14166.repository.FonctionCommissionPermanenteRepository;
import fr.cridf.babylone14166.repository.FonctionExecutiveRepository;
import fr.cridf.babylone14166.service.dto.CommissionPermanenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommissionPermanenteService {

    @Autowired
    private EluRepository eluRepository;
    @Autowired
    private AppartenanceCommissionPermanenteRepository appartenanceCommissionPermanenteRepository;
    @Autowired
    private FonctionCommissionPermanenteRepository fonctionCommissionPermanenteRepository;
    @Autowired
    private FonctionExecutiveRepository fonctionExecutiveRepository;

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public static boolean isAppartenanceCourante(AppartenanceCommissionPermanente a) {
        // plus tard : || a.getDateFin().isAfter(LocalDate.now())
        return a.getDateFin() == null;
    }

    @Transactional(readOnly = true)
    public static boolean isFonctionCourante(FonctionCommissionPermanente f) {
        // plus tard : || f.getDateFin().isAfter(LocalDate.now())
        return f.getDateFin() == null;
    }

    @Transactional(readOnly = true)
    public static boolean isFonctionExecutiveCourante(FonctionExecutive f) {
        // plus tard : || f.getDateFin().isAfter(LocalDate.now())
        return f.getDateFin() == null;
    }
}
