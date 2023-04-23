package openassemblee.web.rest.dto;

import java.io.Serializable;
import java.util.List;

public class HemicyclePlanUpdateDTO implements Serializable {

    private Long id;
    private List<HemicycleAssociationDTO> associations;

    public HemicyclePlanUpdateDTO() {}

    public HemicyclePlanUpdateDTO(
        Long id,
        List<HemicycleAssociationDTO> associations
    ) {
        this.id = id;
        this.associations = associations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<HemicycleAssociationDTO> getAssociations() {
        return associations;
    }

    public void setAssociations(List<HemicycleAssociationDTO> associations) {
        this.associations = associations;
    }
}
