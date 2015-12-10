package fr.cridf.babylone14166.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.web.rest.dto.CommissionPermanenteDTO;

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
        List<AppartenanceCommissionPermanente> acp = appartenanceCommissionPermanenteRepository.findAll();
        List<FonctionCommissionPermanente> fcp = fonctionCommissionPermanenteRepository.findAll();
        List<FonctionExecutive> fe = fonctionExecutiveRepository.findAll();
        return new CommissionPermanenteDTO(acp, fcp, fe);
    }

}
