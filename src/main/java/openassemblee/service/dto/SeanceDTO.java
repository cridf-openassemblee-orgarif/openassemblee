package openassemblee.service.dto;

import openassemblee.domain.GroupePolitique;
import openassemblee.domain.Seance;

import java.util.List;

public class SeanceDTO {

    private final Seance seance;
    private final List<PouvoirListDTO> pouvoirs;
    private final List<GroupePolitique> groupePolitiques;
    private final Long hemicyclePlanId;

    public SeanceDTO(Seance seance, List<PouvoirListDTO> pouvoirs, List<GroupePolitique> groupePolitiques, Long hemicyclePlanId) {
        this.seance = seance;
        this.pouvoirs = pouvoirs;
        this.groupePolitiques = groupePolitiques;
        this.hemicyclePlanId = hemicyclePlanId;
    }

    public Seance getSeance() {
        return seance;
    }

    public List<PouvoirListDTO> getPouvoirs() {
        return pouvoirs;
    }

    public List<GroupePolitique> getGroupePolitiques() {
        return groupePolitiques;
    }

    public Long getHemicyclePlanId() {
        return hemicyclePlanId;
    }
}
