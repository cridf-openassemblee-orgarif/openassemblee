package openassemblee.service.dto;

import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;
import openassemblee.service.util.ConfidentialiteUtil;

public class EluListDTO {

    private Elu elu;
    private GroupePolitique groupePolitique;

    public EluListDTO(Elu elu, Boolean loadAdresses, Boolean filterAdresses) {
        this.elu = elu;
        ConfidentialiteUtil.filterElu(elu, loadAdresses, filterAdresses);
    }

    public EluListDTO(Elu elu, GroupePolitique groupePolitique, Boolean loadAdresses, Boolean filterAdresses) {
        this.elu = elu;
        ConfidentialiteUtil.filterElu(elu, loadAdresses, filterAdresses);
        this.groupePolitique = groupePolitique;
    }

    public Elu getElu() {
        return elu;
    }

    public GroupePolitique getGroupePolitique() {
        return groupePolitique;
    }
}
