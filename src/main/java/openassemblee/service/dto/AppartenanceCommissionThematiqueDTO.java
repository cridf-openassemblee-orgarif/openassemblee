package openassemblee.service.dto;

import openassemblee.domain.AppartenanceCommissionThematique;
import openassemblee.domain.Elu;

public class AppartenanceCommissionThematiqueDTO {

    private AppartenanceCommissionThematique appartenanceCommissionThematique;
    private Elu elu;

    public AppartenanceCommissionThematiqueDTO(
        AppartenanceCommissionThematique appartenanceCommissionThematique,
        Elu elu
    ) {
        this.appartenanceCommissionThematique =
            appartenanceCommissionThematique;
        this.elu = elu;
    }

    public AppartenanceCommissionThematique getAppartenanceCommissionThematique() {
        return appartenanceCommissionThematique;
    }

    public Elu getElu() {
        return elu;
    }
}
