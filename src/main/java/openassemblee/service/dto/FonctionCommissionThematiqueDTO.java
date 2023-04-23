package openassemblee.service.dto;

import openassemblee.domain.Elu;
import openassemblee.domain.FonctionCommissionThematique;

public class FonctionCommissionThematiqueDTO {

    private FonctionCommissionThematique fonctionCommissionThematique;
    private Elu elu;

    public FonctionCommissionThematiqueDTO(
        FonctionCommissionThematique fonctionCommissionThematique,
        Elu elu
    ) {
        this.fonctionCommissionThematique = fonctionCommissionThematique;
        this.elu = elu;
    }

    public FonctionCommissionThematique getFonctionCommissionThematique() {
        return fonctionCommissionThematique;
    }

    public Elu getElu() {
        return elu;
    }
}
