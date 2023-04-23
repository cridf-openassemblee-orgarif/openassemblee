package openassemblee.web.rest.dto;

import java.io.Serializable;
import openassemblee.domain.Mandature;

public class HemicyclePlanCreationDTO implements Serializable {

    private String label;
    private Long configurationId;
    private boolean fromAlphabeticOrder;
    private Long fromSeanceId;
    private Long fromProjetId;
    private Mandature mandature;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Long configurationId) {
        this.configurationId = configurationId;
    }

    public boolean isFromAlphabeticOrder() {
        return fromAlphabeticOrder;
    }

    public void setFromAlphabeticOrder(boolean fromAlphabeticOrder) {
        this.fromAlphabeticOrder = fromAlphabeticOrder;
    }

    public Long getFromSeanceId() {
        return fromSeanceId;
    }

    public void setFromSeanceId(Long fromSeanceId) {
        this.fromSeanceId = fromSeanceId;
    }

    public Long getFromProjetId() {
        return fromProjetId;
    }

    public void setFromProjetId(Long fromProjetId) {
        this.fromProjetId = fromProjetId;
    }

    public Mandature getMandature() {
        return mandature;
    }

    public void setMandature(Mandature mandature) {
        this.mandature = mandature;
    }
}
