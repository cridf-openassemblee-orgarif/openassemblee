package openassemblee.web.rest.dto;

import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;
import openassemblee.domain.HemicyclePlan;

import java.io.Serializable;
import java.util.List;

public class HemicycleArchiveCreationDTO implements Serializable {

    private Long planId;
    private HemicycleArchiveDataDTO data;
    private String svgPlan;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public HemicycleArchiveDataDTO getData() {
        return data;
    }

    public void setData(HemicycleArchiveDataDTO data) {
        this.data = data;
    }

    public String getSvgPlan() {
        return svgPlan;
    }

    public void setSvgPlan(String svgPlan) {
        this.svgPlan = svgPlan;
    }
}
