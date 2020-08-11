package openassemblee.web.rest.dto;

import openassemblee.domain.HemicyclePlan;

import java.io.Serializable;
import java.util.List;

public class HemicyclePlanUpdateDTO implements Serializable {

    private Long id;
    private List<HemicyclePlan.Association> associations ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<HemicyclePlan.Association> getAssociations() {
        return associations;
    }

    public void setAssociations(List<HemicyclePlan.Association> associations) {
        this.associations = associations;
    }
}
