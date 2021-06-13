package openassemblee.service.dto;

import openassemblee.domain.Mandat;

public class MandatEditionDTO {

    private Mandat mandat;

    private Boolean demissionDiffusion;

    public Mandat getMandat() {
        return mandat;
    }

    public void setMandat(Mandat mandat) {
        this.mandat = mandat;
    }

    public Boolean getDemissionDiffusion() {
        return demissionDiffusion;
    }

    public void setDemissionDiffusion(Boolean demissionDiffusion) {
        this.demissionDiffusion = demissionDiffusion;
    }
}
