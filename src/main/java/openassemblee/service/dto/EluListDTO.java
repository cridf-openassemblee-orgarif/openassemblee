package openassemblee.service.dto;

import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;
import openassemblee.service.util.ConfidentialiteUtil;

public class EluListDTO {

    private Elu elu;
    private GroupePolitique groupePolitique;
    private String shortFonction;
    private Boolean currentMandat;

    public EluListDTO(Elu elu, String shortFonction, Boolean currentMandat, Boolean loadAdresses, Boolean filterAdresses) {
        this.elu = elu;
        ConfidentialiteUtil.filterElu(elu, loadAdresses, filterAdresses);
        this.shortFonction = shortFonction;
        this.currentMandat = currentMandat;
    }

    public EluListDTO(Elu elu,
                      GroupePolitique groupePolitique,
                      String shortFonction,
                      Boolean currentMandat,
                      Boolean loadAdresses,
                      Boolean filterAdresses) {
        this.elu = elu;
        ConfidentialiteUtil.filterElu(elu, loadAdresses, filterAdresses);
        this.groupePolitique = groupePolitique;
        this.shortFonction = shortFonction;
        this.currentMandat = currentMandat;
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

    public Boolean getCurrentMandat() {
        return currentMandat;
    }
}
