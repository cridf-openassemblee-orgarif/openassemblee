package openassemblee.web.rest.dto;

import openassemblee.domain.Mandature;
import openassemblee.domain.User;

public class UserWithMandatureDTO extends UserDTO {

    private Mandature currentMandature;
    private Boolean isForcedMandature;

    public UserWithMandatureDTO(User user,
                                Mandature currentMandature,
                                Boolean isForcedMandature) {
        super(user);
        this.currentMandature = currentMandature;
        this.isForcedMandature = isForcedMandature;
    }

    public Mandature getCurrentMandature() {
        return currentMandature;
    }

    public void setCurrentMandature(Mandature currentMandature) {
        this.currentMandature = currentMandature;
    }

    public Boolean getForcedMandature() {
        return isForcedMandature;
    }

    public void setForcedMandature(Boolean forcedMandature) {
        isForcedMandature = forcedMandature;
    }
}
