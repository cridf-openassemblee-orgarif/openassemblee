package fr.cridf.babylone14166.service.dto;

import fr.cridf.babylone14166.domain.AppartenanceOrganisme;

public class AppartenanceOrganismeDTO {

    private AppartenanceOrganisme appartenance;
    private EluListDTO eluDto;

    public AppartenanceOrganismeDTO(AppartenanceOrganisme appartenance, EluListDTO eluDto) {
        this.appartenance = appartenance;
        this.eluDto = eluDto;
    }

    public AppartenanceOrganisme getAppartenance() {
        return appartenance;
    }

    public EluListDTO getEluDto() {
        return eluDto;
    }
}
