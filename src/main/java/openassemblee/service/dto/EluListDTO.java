package openassemblee.service.dto;

import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;
import openassemblee.service.util.ConfidentialiteUtil;

public class EluListDTO {

    private Elu elu;
    private GroupePolitique groupePolitique;
    private String shortFonction;

    public EluListDTO(Elu elu, String shortFonction, Boolean loadAdresses, Boolean filterAdresses) {
        this.elu = elu;
        ConfidentialiteUtil.filterElu(elu, loadAdresses, filterAdresses);
        this.shortFonction = shortFonction;
    }

    public EluListDTO(Elu elu,
                      GroupePolitique groupePolitique,
                      String shortFonction,
                      Boolean loadAdresses,
                      Boolean filterAdresses) {
        this.elu = elu;
        ConfidentialiteUtil.filterElu(elu, loadAdresses, filterAdresses);
        this.groupePolitique = groupePolitique;
        this.shortFonction = shortFonction;
    }

    public Elu getElu() {
        return elu;
    }

    public GroupePolitique getGroupePolitique() {
        return groupePolitique;
    }

    public String getShortFonction() {
        return shortFonction;
    }
}
