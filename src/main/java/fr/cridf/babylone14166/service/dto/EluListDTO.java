package fr.cridf.babylone14166.service.dto;

import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.domain.GroupePolitique;

public class EluListDTO {

    private Elu elu;
    private GroupePolitique groupePolitique;

    public EluListDTO(Elu elu, GroupePolitique groupePolitique) {
        this.elu = elu;
        this.groupePolitique = groupePolitique;
    }

    public Elu getElu() {
        return elu;
    }

    public void setElu(Elu elu) {
        this.elu = elu;
    }

    public GroupePolitique getGroupePolitique() {
        return groupePolitique;
    }

    public void setGroupePolitique(GroupePolitique groupePolitique) {
        this.groupePolitique = groupePolitique;
    }
}
