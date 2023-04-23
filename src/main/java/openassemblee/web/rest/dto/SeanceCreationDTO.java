package openassemblee.web.rest.dto;

import java.io.Serializable;
import openassemblee.domain.Seance;

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
