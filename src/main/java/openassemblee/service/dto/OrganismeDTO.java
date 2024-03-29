package openassemblee.service.dto;

import java.util.List;
import openassemblee.domain.Organisme;

public class OrganismeDTO {

    private Organisme organisme;
    private List<AppartenanceOrganismeDTO> appartenances;

    public OrganismeDTO(
        Organisme organisme,
        List<AppartenanceOrganismeDTO> appartenances
    ) {
        this.organisme = organisme;
        this.appartenances = appartenances;
    }

    public Organisme getOrganisme() {
        return organisme;
    }

    public List<AppartenanceOrganismeDTO> getAppartenances() {
        return appartenances;
    }
}
