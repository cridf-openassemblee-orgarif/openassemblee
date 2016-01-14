package fr.cridf.babylone14166.service.dto;

import fr.cridf.babylone14166.domain.AppartenanceCommissionThematique;
import fr.cridf.babylone14166.domain.Elu;

public class AppartenanceCommissionThematiqueDTO {

    private AppartenanceCommissionThematique appartenanceCommissionThematique;
    private Elu elu;

    public AppartenanceCommissionThematiqueDTO(AppartenanceCommissionThematique appartenanceCommissionThematique, Elu
        elu) {
        this.appartenanceCommissionThematique = appartenanceCommissionThematique;
        this.elu = elu;
    }

    public AppartenanceCommissionThematique getAppartenanceCommissionThematique() {
        return appartenanceCommissionThematique;
    }

    public Elu getElu() {
        return elu;
    }
}
