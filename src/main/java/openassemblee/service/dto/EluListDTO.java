package openassemblee.service.dto;

import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;
import openassemblee.service.util.ConfidentialiteUtil;

public class EluListDTO {

    private Elu elu;
    private GroupePolitique groupePolitique;
    private String shortFonction;
    private Boolean actifInCurrentMandat;

    public EluListDTO(Elu elu, String shortFonction, Boolean actifInCurrentMandat, Boolean loadAdresses, Boolean filterAdresses) {
        this.elu = elu;
        ConfidentialiteUtil.filterElu(elu, loadAdresses, filterAdresses);
        this.shortFonction = shortFonction;
        this.actifInCurrentMandat = actifInCurrentMandat;
    }

    public EluListDTO(Elu elu,
                      GroupePolitique groupePolitique,
                      String shortFonction,
                      Boolean actifInCurrentMandat,
                      Boolean loadAdresses,
                      Boolean filterAdresses) {
        this.elu = elu;
        ConfidentialiteUtil.filterElu(elu, loadAdresses, filterAdresses);
        this.groupePolitique = groupePolitique;
        this.shortFonction = shortFonction;
        this.actifInCurrentMandat = actifInCurrentMandat;
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

    public Boolean getActifInCurrentMandat() {
        return actifInCurrentMandat;
    }
}
