package openassemblee.service.dto;

import openassemblee.domain.CommissionThematique;
import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;
import openassemblee.service.util.ConfidentialiteUtil;

import java.util.Map;

public class EluDTO {

    private Elu elu;
    private Map<Long, GroupePolitique> groupesPolitiques;
    private Map<Long, CommissionThematique> commissionsThematiques;
//    private Map<String, Organisme> organismes;

    public EluDTO(Elu elu,
                  Map<Long, GroupePolitique> groupesPolitiques,
                  Map<Long, CommissionThematique> commissionsThematiques,
//                  Map<String, Organisme> organismes,
                  Boolean loadAdresses,
                  Boolean filterAdresses) {
        this.elu = elu;
        ConfidentialiteUtil.filterElu(elu, loadAdresses, filterAdresses);
        this.groupesPolitiques = groupesPolitiques;
        this.commissionsThematiques = commissionsThematiques;
//        this.organismes = organismes;
    }

    public Elu getElu() {
        return elu;
    }

    public void setElu(Elu elu) {
        this.elu = elu;
    }

    public Map<Long, GroupePolitique> getGroupesPolitiques() {
        return groupesPolitiques;
    }

    public void setGroupesPolitiques(Map<Long, GroupePolitique> groupesPolitiques) {
        this.groupesPolitiques = groupesPolitiques;
    }

    public Map<Long, CommissionThematique> getCommissionsThematiques() {
        return commissionsThematiques;
    }

    public void setCommissionsThematiques(Map<Long, CommissionThematique> commissionsThematiques) {
        this.commissionsThematiques = commissionsThematiques;
    }

    //    public Map<String, Organisme> getOrganismes() {
//        return organismes;
//    }
//
//    public void setOrganismes(Map<String, Organisme> organismes) {
//        this.organismes = organismes;
//    }
}
