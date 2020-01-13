package openassemblee.web.rest.dto;

import java.util.List;

public class AssembleeDTO {

    private List<AssembleeChairDTO> chairs;

    public List<AssembleeChairDTO> getChairs() {
        return chairs;
    }

    public AssembleeDTO(List<AssembleeChairDTO> chairs) {
        this.chairs = chairs;
    }
}
