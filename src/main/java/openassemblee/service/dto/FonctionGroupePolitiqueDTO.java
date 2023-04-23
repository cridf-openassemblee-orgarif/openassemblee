package openassemblee.service.dto;

import openassemblee.domain.Elu;
import openassemblee.domain.FonctionGroupePolitique;

public class FonctionGroupePolitiqueDTO {

    private FonctionGroupePolitique fonctionGroupePolitique;
    private Elu elu;

    public FonctionGroupePolitiqueDTO(
        FonctionGroupePolitique fonctionGroupePolitique,
        Elu elu
    ) {
        this.fonctionGroupePolitique = fonctionGroupePolitique;
        this.elu = elu;
    }

    public FonctionGroupePolitique getFonctionGroupePolitique() {
        return fonctionGroupePolitique;
    }

    public Elu getElu() {
        return elu;
    }
}
