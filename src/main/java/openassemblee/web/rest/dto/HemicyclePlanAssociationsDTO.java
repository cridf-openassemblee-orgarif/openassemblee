package openassemblee.web.rest.dto;

import openassemblee.domain.HemicyclePlan;

import java.util.List;

public class HemicyclePlanAssociationsDTO {

    private List<HemicyclePlan.Association> associations ;
    private HemicycleConfigurationRendu configurationRendu;

    public HemicyclePlanAssociationsDTO(List<HemicyclePlan.Association> associations, HemicycleConfigurationRendu configurationRendu) {
        this.associations = associations;
        this.configurationRendu = configurationRendu;
    }

    public List<HemicyclePlan.Association> getAssociations() {
        return associations;
    }

    public void setAssociations(List<HemicyclePlan.Association> associations) {
        this.associations = associations;
    }

    public HemicycleConfigurationRendu getConfigurationRendu() {
        return configurationRendu;
    }

    public void setConfigurationRendu(HemicycleConfigurationRendu configurationRendu) {
        this.configurationRendu = configurationRendu;
    }
}
