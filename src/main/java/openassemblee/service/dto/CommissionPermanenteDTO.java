package openassemblee.service.dto;

import openassemblee.domain.AppartenanceCommissionPermanente;
import openassemblee.domain.Elu;

import java.util.List;
import java.util.Map;

public class CommissionPermanenteDTO {

    private List<AppartenanceCommissionPermanente> appartenances;
    private Map<Long, Elu> elus;

    public CommissionPermanenteDTO(List<AppartenanceCommissionPermanente> appartenances, Map<Long, Elu> elus) {
        this.appartenances = appartenances;
        this.elus = elus;
    }

    public List<AppartenanceCommissionPermanente> getAppartenances() {
        return appartenances;
    }

    public void setAppartenances(List<AppartenanceCommissionPermanente> appartenances) {
        this.appartenances = appartenances;
    }

    public Map<Long, Elu> getElus() {
        return elus;
    }

    public void setElus(Map<Long, Elu> elus) {
        this.elus = elus;
    }
}
