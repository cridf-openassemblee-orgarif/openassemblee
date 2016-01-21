package fr.cridf.babylone14166.service.dto;

import java.util.List;

import fr.cridf.babylone14166.domain.GroupePolitique;

public class GroupePolitiqueDTO {

    private GroupePolitique groupePolitique;
    private List<AppartenanceGroupePolitiqueDTO> appartenanceGroupePolitiqueDTOs;
    private List<FonctionGroupePolitiqueDTO> fonctionGroupePolitiqueDTOs;

    public GroupePolitiqueDTO(GroupePolitique groupePolitique, List<AppartenanceGroupePolitiqueDTO>
        appartenanceGroupePolitiqueDTOs, List<FonctionGroupePolitiqueDTO> fonctionGroupePolitiqueDTOs) {
        this.groupePolitique = groupePolitique;
        this.appartenanceGroupePolitiqueDTOs = appartenanceGroupePolitiqueDTOs;
        this.fonctionGroupePolitiqueDTOs = fonctionGroupePolitiqueDTOs;
    }

    public GroupePolitique getGroupePolitique() {
        return groupePolitique;
    }

    public List<AppartenanceGroupePolitiqueDTO> getAppartenanceGroupePolitiqueDTOs() {
        return appartenanceGroupePolitiqueDTOs;
    }

    public List<FonctionGroupePolitiqueDTO> getFonctionGroupePolitiqueDTOs() {
        return fonctionGroupePolitiqueDTOs;
    }
}

