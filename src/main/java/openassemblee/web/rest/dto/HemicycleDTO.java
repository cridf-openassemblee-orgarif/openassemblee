package openassemblee.web.rest.dto;

import java.util.List;

public class HemicycleDTO {

    private List<HemicycleChairDTO> chairs;

    public List<HemicycleChairDTO> getChairs() {
        return chairs;
    }

    public HemicycleDTO(List<HemicycleChairDTO> chairs) {
        this.chairs = chairs;
    }
}
