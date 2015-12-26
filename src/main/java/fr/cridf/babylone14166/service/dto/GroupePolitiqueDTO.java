package fr.cridf.babylone14166.service.dto;

import java.util.List;

import fr.cridf.babylone14166.domain.GroupePolitique;

public class GroupePolitiqueDTO {

    private GroupePolitique groupePolitique;
    private int count;
    private List<AppartenanceGroupePolitiqueDTO> appartenanceGroupePolitiqueDTOs;

    public GroupePolitiqueDTO(GroupePolitique groupePolitique, int count) {
        this.groupePolitique = groupePolitique;
        this.count = count;
    }

    public GroupePolitiqueDTO(GroupePolitique groupePolitique, int count,
        List<AppartenanceGroupePolitiqueDTO> appartenanceGroupePolitiqueDTOs) {
        this.groupePolitique = groupePolitique;
        this.count = count;
        this.appartenanceGroupePolitiqueDTOs = appartenanceGroupePolitiqueDTOs;
    }

    public GroupePolitique getGroupePolitique() {
        return groupePolitique;
    }

    public void setGroupePolitique(GroupePolitique groupePolitique) {
        this.groupePolitique = groupePolitique;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AppartenanceGroupePolitiqueDTO> getAppartenanceGroupePolitiqueDTOs() {
        return appartenanceGroupePolitiqueDTOs;
    }

    public void setAppartenanceGroupePolitiqueDTOs(List<AppartenanceGroupePolitiqueDTO>
        appartenanceGroupePolitiqueDTOs) {
        this.appartenanceGroupePolitiqueDTOs = appartenanceGroupePolitiqueDTOs;
    }
}

