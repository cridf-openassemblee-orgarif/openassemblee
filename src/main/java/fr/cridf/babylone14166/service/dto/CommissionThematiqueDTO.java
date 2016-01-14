package fr.cridf.babylone14166.service.dto;

import java.util.List;

import fr.cridf.babylone14166.domain.AppartenanceCommissionThematique;
import fr.cridf.babylone14166.domain.CommissionThematique;

public class CommissionThematiqueDTO {

    private CommissionThematique commissionThematique;
    private List<AppartenanceCommissionThematique> appartenanceCommissionThematiqueList;

    public CommissionThematiqueDTO(CommissionThematique commissionThematique, List<AppartenanceCommissionThematique>
        appartenanceCommissionThematiqueList) {
        this.commissionThematique = commissionThematique;
        this.appartenanceCommissionThematiqueList = appartenanceCommissionThematiqueList;
    }

    public CommissionThematique getCommissionThematique() {
        return commissionThematique;
    }

    public List<AppartenanceCommissionThematique> getAppartenanceCommissionThematiqueList() {
        return appartenanceCommissionThematiqueList;
    }
}
