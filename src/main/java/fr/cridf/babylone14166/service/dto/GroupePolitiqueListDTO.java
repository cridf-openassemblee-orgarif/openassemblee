package fr.cridf.babylone14166.service.dto;

import fr.cridf.babylone14166.domain.GroupePolitique;

public class GroupePolitiqueListDTO {

    private GroupePolitique groupePolitique;
    private int count;

    public GroupePolitiqueListDTO(GroupePolitique groupePolitique, int count) {
        this.groupePolitique = groupePolitique;
        this.count = count;
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

}

