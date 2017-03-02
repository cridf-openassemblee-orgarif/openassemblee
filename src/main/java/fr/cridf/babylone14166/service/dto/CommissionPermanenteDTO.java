package fr.cridf.babylone14166.service.dto;

import fr.cridf.babylone14166.domain.AppartenanceCommissionPermanente;
import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.domain.FonctionCommissionPermanente;
import fr.cridf.babylone14166.domain.FonctionExecutive;

import java.util.List;
import java.util.Map;

public class CommissionPermanenteDTO {

    private List<AppartenanceCommissionPermanente> appartenances;
    private List<FonctionCommissionPermanente> fonctions;
    private List<FonctionExecutive> fonctionsExecutives;
    private Map<Long, Elu> elus;

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
