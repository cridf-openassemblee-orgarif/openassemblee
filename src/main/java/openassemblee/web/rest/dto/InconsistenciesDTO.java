package openassemblee.web.rest.dto;

import java.util.List;

public class InconsistenciesDTO {

    public enum InconsistencyCategory {
        POUVOIR_BENEFICIAIRE_MANQUANT, POUVOIR_CEDEUR_MANQUANT
    }

    private InconsistencyCategory category;
    private List<InconsistencyDTO> inconsistencies;

    public InconsistenciesDTO() {
    }

    public InconsistenciesDTO(InconsistencyCategory category, List<InconsistencyDTO> inconsistencies) {
        this.category = category;
        this.inconsistencies = inconsistencies;
    }

    public InconsistencyCategory getCategory() {
        return category;
    }

    public void setCategory(InconsistencyCategory category) {
        this.category = category;
    }

    public List<InconsistencyDTO> getInconsistencies() {
        return inconsistencies;
    }

    public void setInconsistencies(List<InconsistencyDTO> inconsistencies) {
        this.inconsistencies = inconsistencies;
    }
}
