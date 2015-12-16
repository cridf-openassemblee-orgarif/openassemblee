package fr.cridf.babylone14166.service.dto;

import java.util.Map;

import fr.cridf.babylone14166.domain.*;

public class EluCompletDTO {

    private Elu elu;
    private Map<Long, GroupePolitique> groupesPolitiques;
    private Map<Long, CommissionThematique> commissionsThematiques;

    public EluCompletDTO(Elu elu, Map<Long, GroupePolitique> groupesPolitiques,
        Map<Long, CommissionThematique> commissionsThematiques) {
        this.elu = elu;
        this.groupesPolitiques = groupesPolitiques;
        this.commissionsThematiques = commissionsThematiques;
    }

    public Elu getElu() {
        return elu;
    }

    public void setElu(Elu elu) {
        this.elu = elu;
    }

    public Map<Long, GroupePolitique> getGroupesPolitiques() {
        return groupesPolitiques;
    }

    public void setGroupesPolitiques(Map<Long, GroupePolitique> groupesPolitiques) {
        this.groupesPolitiques = groupesPolitiques;
    }

    public Map<Long, CommissionThematique> getCommissionsThematiques() {
        return commissionsThematiques;
    }

    public void setCommissionsThematiques(Map<Long, CommissionThematique> commissionsThematiques) {
        this.commissionsThematiques = commissionsThematiques;
    }
}
