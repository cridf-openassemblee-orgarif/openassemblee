package openassemblee.service.dto;

import openassemblee.domain.CommissionThematique;

import java.util.List;

public class CommissionThematiqueDTO {

    private CommissionThematique commissionThematique;
    private List<AppartenanceCommissionThematiqueDTO> appartenanceCommissionThematiqueDTOs;
    private List<FonctionCommissionThematiqueDTO> fonctionCommissionThematiqueDTOs;

    public CommissionThematiqueDTO(CommissionThematique commissionThematique,
                                   List<AppartenanceCommissionThematiqueDTO> appartenanceCommissionThematiqueDTOs,
                                   List<FonctionCommissionThematiqueDTO> fonctionCommissionThematiqueDTOs) {
        this.commissionThematique = commissionThematique;
        this.appartenanceCommissionThematiqueDTOs = appartenanceCommissionThematiqueDTOs;
        this.fonctionCommissionThematiqueDTOs = fonctionCommissionThematiqueDTOs;
    }

    public CommissionThematique getCommissionThematique() {
        return commissionThematique;
    }

    public List<AppartenanceCommissionThematiqueDTO> getAppartenanceCommissionThematiqueDTOs() {
        return appartenanceCommissionThematiqueDTOs;
    }

    public List<FonctionCommissionThematiqueDTO> getFonctionCommissionThematiqueDTOs() {
        return fonctionCommissionThematiqueDTOs;
    }
}
