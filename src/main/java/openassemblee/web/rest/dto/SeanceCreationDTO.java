package openassemblee.web.rest.dto;

import openassemblee.domain.Seance;

import java.io.Serializable;

public class SeanceCreationDTO implements Serializable {

    private Seance seance;
    private Long projetPlanId;

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

}
