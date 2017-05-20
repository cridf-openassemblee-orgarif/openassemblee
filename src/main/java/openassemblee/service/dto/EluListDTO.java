package openassemblee.service.dto;

import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;

public class EluListDTO {

    private Elu elu;
    private GroupePolitique groupePolitique;

    public EluListDTO(Elu elu) {
        this.elu = elu;
    }

    public EluListDTO(Elu elu, GroupePolitique groupePolitique) {
        this.elu = elu;
        this.groupePolitique = groupePolitique;
    }

    public Elu getElu() {
        return elu;
    }

    public GroupePolitique getGroupePolitique() {
        return groupePolitique;
    }
}
