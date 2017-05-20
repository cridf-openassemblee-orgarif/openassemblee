package openassemblee.service.dto;

import openassemblee.domain.Seance;

import java.util.List;

public class SeanceDTO {

    private final Seance seance;
    private final List<PouvoirListDTO> pouvoirs;

    public SeanceDTO(Seance seance, List<PouvoirListDTO> pouvoirs) {
        this.seance = seance;
        this.pouvoirs = pouvoirs;
    }

    public Seance getSeance() {
        return seance;
    }

    public List<PouvoirListDTO> getPouvoirs() {
        return pouvoirs;
    }
}
