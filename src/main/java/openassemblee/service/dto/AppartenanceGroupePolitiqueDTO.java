package openassemblee.service.dto;

import openassemblee.domain.AppartenanceGroupePolitique;
import openassemblee.domain.Elu;

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
