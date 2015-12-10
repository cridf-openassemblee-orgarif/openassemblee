package fr.cridf.babylone14166.web.rest.dto;

import java.util.List;
import java.util.Map;

import fr.cridf.babylone14166.domain.*;

public class CommissionPermanenteDTO {

    private List<AppartenanceCommissionPermanente> appartenances;
    private List<FonctionCommissionPermanente> fonctions;
    private List<FonctionExecutive> fonctionsExecutives;
    private Map<Long, Elu> elus;

    public CommissionPermanenteDTO() {
    }

    public CommissionPermanenteDTO(List<AppartenanceCommissionPermanente> appartenances,
        List<FonctionCommissionPermanente> fonctions, List<FonctionExecutive> fonctionsExecutives, Map<Long, Elu>
        elus) {
        this.appartenances = appartenances;
        this.fonctions = fonctions;
        this.fonctionsExecutives = fonctionsExecutives;
        this.elus = elus;
    }

    public List<AppartenanceCommissionPermanente> getAppartenances() {
        return appartenances;
    }

    public void setAppartenances(List<AppartenanceCommissionPermanente> appartenances) {
        this.appartenances = appartenances;
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
