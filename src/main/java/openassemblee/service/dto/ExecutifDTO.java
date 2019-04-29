package openassemblee.service.dto;

import openassemblee.domain.Elu;
import openassemblee.domain.FonctionCommissionPermanente;
import openassemblee.domain.FonctionExecutive;

import java.util.List;
import java.util.Map;

public class ExecutifDTO {

    private List<FonctionCommissionPermanente> fonctions;
    private List<FonctionExecutive> fonctionsExecutives;
    private Map<Long, Elu> elus;

    public ExecutifDTO(List<FonctionCommissionPermanente> fonctions, List<FonctionExecutive> fonctionsExecutives,
                       Map<Long, Elu> elus) {
        this.fonctions = fonctions;
        this.fonctionsExecutives = fonctionsExecutives;
        this.elus = elus;
    }

    public List<FonctionCommissionPermanente> getFonctions() {
        return fonctions;
    }

    public void setFonctions(List<FonctionCommissionPermanente> fonctions) {
        this.fonctions = fonctions;
    }

    public List<FonctionExecutive> getFonctionsExecutives() {
        return fonctionsExecutives;
    }

    public void setFonctionsExecutives(List<FonctionExecutive> fonctionsExecutives) {
        this.fonctionsExecutives = fonctionsExecutives;
    }

    public Map<Long, Elu> getElus() {
        return elus;
    }

    public void setElus(Map<Long, Elu> elus) {
        this.elus = elus;
    }
}
