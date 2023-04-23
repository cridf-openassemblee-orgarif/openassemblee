package openassemblee.web.rest.dto;

import java.util.List;

public class HemicyclePlanAssociationsDTO {

    private List<HemicycleAssociationDTO> associations;
    private HemicycleConfigurationRendu configurationRendu;

    public HemicyclePlanAssociationsDTO(
        List<HemicycleAssociationDTO> associations,
        HemicycleConfigurationRendu configurationRendu
    ) {
        this.associations = associations;
        this.configurationRendu = configurationRendu;
    }

    public List<HemicycleAssociationDTO> getAssociations() {
        return associations;
    }

    public void setAssociations(List<HemicycleAssociationDTO> associations) {
        this.associations = associations;
    }

    public HemicycleConfigurationRendu getConfigurationRendu() {
        return configurationRendu;
    }

    public void setConfigurationRendu(
        HemicycleConfigurationRendu configurationRendu
    ) {
        this.configurationRendu = configurationRendu;
    }
}
