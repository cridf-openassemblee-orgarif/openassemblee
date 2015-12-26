package fr.cridf.babylone14166.service.dto;

import java.util.List;

import fr.cridf.babylone14166.domain.GroupePolitique;

public class GroupePolitiqueDTO {

    private GroupePolitique groupePolitique;
    private List<AppartenanceGroupePolitiqueDTO> appartenanceGroupePolitiqueDTOs;

    public GroupePolitiqueDTO(GroupePolitique groupePolitique,
        List<AppartenanceGroupePolitiqueDTO> appartenanceGroupePolitiqueDTOs) {
        this.groupePolitique = groupePolitique;
        this.appartenanceGroupePolitiqueDTOs = appartenanceGroupePolitiqueDTOs;
    }

    public GroupePolitique getGroupePolitique() {
        return groupePolitique;
    }

    public void setGroupePolitique(GroupePolitique groupePolitique) {
        this.groupePolitique = groupePolitique;
    }

    public List<AppartenanceGroupePolitiqueDTO> getAppartenanceGroupePolitiqueDTOs() {
        return appartenanceGroupePolitiqueDTOs;
    }

    public void setAppartenanceGroupePolitiqueDTOs(List<AppartenanceGroupePolitiqueDTO>
        appartenanceGroupePolitiqueDTOs) {
        this.appartenanceGroupePolitiqueDTOs = appartenanceGroupePolitiqueDTOs;
    }
}

