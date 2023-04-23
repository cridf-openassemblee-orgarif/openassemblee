package openassemblee.service.dto;

import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;

public class EluEnFonctionDTO {

    private Elu elu;
    private GroupePolitique groupePolitique;
    private String fonctionLabel;

    public EluEnFonctionDTO(
        Elu elu,
        GroupePolitique groupePolitique,
        String fonctionLabel
    ) {
        this.elu = elu;
        this.groupePolitique = groupePolitique;
        this.fonctionLabel = fonctionLabel;
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

    public String getFonctionLabel() {
        return fonctionLabel;
    }

    public void setFonctionLabel(String fonctionLabel) {
        this.fonctionLabel = fonctionLabel;
    }
}
