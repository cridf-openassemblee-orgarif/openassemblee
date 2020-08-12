package openassemblee.web.rest.dto;

import java.io.Serializable;
import java.util.List;

public class HemicycleArchiveDataWithConfigurationDTO implements Serializable {

    private HemicycleArchiveDataDTO data;
    private HemicycleConfigurationRendu rendu;

    public HemicycleArchiveDataWithConfigurationDTO() {
    }

    public HemicycleArchiveDataWithConfigurationDTO(HemicycleArchiveDataDTO data, HemicycleConfigurationRendu rendu) {
        this.data = data;
        this.rendu = rendu;
    }

    public HemicycleArchiveDataDTO getData() {
        return data;
    }

    public void setData(HemicycleArchiveDataDTO data) {
        this.data = data;
    }

    public HemicycleConfigurationRendu getRendu() {
        return rendu;
    }

    public void setRendu(HemicycleConfigurationRendu rendu) {
        this.rendu = rendu;
    }
}
