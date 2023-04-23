package openassemblee.web.rest.dto;

public class HemicycleAssociationDTO {

    private Integer chairNumber;
    private Long eluId;

    // for serialization
    public HemicycleAssociationDTO() {}

    public HemicycleAssociationDTO(Integer chairNumber, Long eluId) {
        this.chairNumber = chairNumber;
        this.eluId = eluId;
    }

    public Integer getChairNumber() {
        return chairNumber;
    }

    public void setChairNumber(Integer chairNumber) {
        this.chairNumber = chairNumber;
    }

    public Long getEluId() {
        return eluId;
    }

    public void setEluId(Long eluId) {
        this.eluId = eluId;
    }
}
