package openassemblee.web.rest.dto;

import java.io.Serializable;
import java.util.List;

public class HemicycleArchiveDataDTO implements Serializable {

    private List<HemicycleAssociationDTO> associations;
    private List<HemicycleEluDTO> elus;
    private List<HemicycleGroupePolitiqueDTO> groupePolitiques;

    public HemicycleArchiveDataDTO() {
    }

    public HemicycleArchiveDataDTO(List<HemicycleAssociationDTO> associations, List<HemicycleEluDTO> elus, List<HemicycleGroupePolitiqueDTO> groupePolitiques) {
        this.associations = associations;
        this.elus = elus;
        this.groupePolitiques = groupePolitiques;
    }

    public List<HemicycleAssociationDTO> getAssociations() {
        return associations;
    }

    public void setAssociations(List<HemicycleAssociationDTO> associations) {
        this.associations = associations;
    }

    public List<HemicycleEluDTO> getElus() {
        return elus;
    }

    public void setElus(List<HemicycleEluDTO> elus) {
        this.elus = elus;
    }

    public List<HemicycleGroupePolitiqueDTO> getGroupePolitiques() {
        return groupePolitiques;
    }

    public void setGroupePolitiques(List<HemicycleGroupePolitiqueDTO> groupePolitiques) {
        this.groupePolitiques = groupePolitiques;
    }
}
