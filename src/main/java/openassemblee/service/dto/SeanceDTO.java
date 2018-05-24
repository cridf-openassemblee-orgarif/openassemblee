package openassemblee.service.dto;

import openassemblee.domain.GroupePolitique;
import openassemblee.domain.Seance;

import java.util.List;

public class SeanceDTO {

    private final Seance seance;
    private final List<PouvoirListDTO> pouvoirs;
    private final List<GroupePolitique> groupePolitiques;

    public SeanceDTO(Seance seance, List<PouvoirListDTO> pouvoirs, List<GroupePolitique> groupePolitiques) {
        this.seance = seance;
        this.pouvoirs = pouvoirs;
        this.groupePolitiques = groupePolitiques;
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
}
