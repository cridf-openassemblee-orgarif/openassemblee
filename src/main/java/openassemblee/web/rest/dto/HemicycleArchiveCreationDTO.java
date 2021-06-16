package openassemblee.web.rest.dto;

import java.io.Serializable;

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
