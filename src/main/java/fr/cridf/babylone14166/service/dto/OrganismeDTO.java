package fr.cridf.babylone14166.service.dto;

import fr.cridf.babylone14166.domain.Organisme;

import java.util.List;

public class OrganismeDTO {

    private Organisme organisme;
    private List<AppartenanceOrganismeDTO> appartenances;

    public OrganismeDTO(Organisme organisme, List<AppartenanceOrganismeDTO> appartenances) {
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
