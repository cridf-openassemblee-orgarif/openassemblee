package fr.cridf.babylone14166.service.dto;

import fr.cridf.babylone14166.domain.AppartenanceGroupePolitique;
import fr.cridf.babylone14166.domain.Elu;

public class AppartenanceGroupePolitiqueDTO {

    private AppartenanceGroupePolitique appartenanceGroupePolitique;
    private Elu elu;

    public AppartenanceGroupePolitiqueDTO(AppartenanceGroupePolitique appartenanceGroupePolitique, Elu elu) {
        this.appartenanceGroupePolitique = appartenanceGroupePolitique;
        this.elu = elu;
    }

    public AppartenanceGroupePolitique getAppartenanceGroupePolitique() {
        return appartenanceGroupePolitique;
    }

    public void setAppartenanceGroupePolitique(AppartenanceGroupePolitique appartenanceGroupePolitique) {
        this.appartenanceGroupePolitique = appartenanceGroupePolitique;
    }

    public Elu getElu() {
        return elu;
    }

    public void setElu(Elu elu) {
        this.elu = elu;
    }
}
