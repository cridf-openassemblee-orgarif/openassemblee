package openassemblee.service.dto;

import java.util.List;
import openassemblee.domain.GroupePolitique;
import openassemblee.domain.HemicyclePlan;
import openassemblee.domain.Seance;

public class SeanceDTO {

    private final Seance seance;
    private final List<PouvoirListDTO> pouvoirs;
    private final List<GroupePolitique> groupePolitiques;
    private final HemicyclePlan hemicyclePlan;

    public SeanceDTO(
        Seance seance,
        List<PouvoirListDTO> pouvoirs,
        List<GroupePolitique> groupePolitiques,
        HemicyclePlan hemicyclePlan
    ) {
        this.seance = seance;
        this.pouvoirs = pouvoirs;
        this.groupePolitiques = groupePolitiques;
        this.hemicyclePlan = hemicyclePlan;
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

    public HemicyclePlan getHemicyclePlan() {
        return hemicyclePlan;
    }
}
