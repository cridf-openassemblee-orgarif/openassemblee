package openassemblee.service.dto;

import openassemblee.domain.Seance;

import java.io.Serializable;

public class SeanceCreationDTO implements Serializable {

    private Seance seance;
    private Long projetPlanId;
    private Long seancePlanId;

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public Long getProjetPlanId() {
        return projetPlanId;
    }

    public void setProjetPlanId(Long projetPlanId) {
        this.projetPlanId = projetPlanId;
    }

    public Long getSeancePlanId() {
        return seancePlanId;
    }

    public void setSeancePlanId(Long seancePlanId) {
        this.seancePlanId = seancePlanId;
    }
}
