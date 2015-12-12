package fr.cridf.babylone14166.service.dto;

import java.util.Map;

import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.domain.GroupePolitique;

public class EluCompletDTO {

    private Elu elu;
    private Map<Long, GroupePolitique> groupesPolitiques;

    public EluCompletDTO(Elu elu, Map<Long, GroupePolitique> groupesPolitiques) {
        this.elu = elu;
        this.groupesPolitiques = groupesPolitiques;
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
}
