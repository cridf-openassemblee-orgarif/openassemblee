package fr.cridf.babylone14166.service.dto;

import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.domain.FonctionCommissionThematique;

public class FonctionCommissionThematiqueDTO {

    private FonctionCommissionThematique fonctionCommissionThematique;
    private Elu elu;

    public FonctionCommissionThematiqueDTO(FonctionCommissionThematique fonctionCommissionThematique, Elu elu) {
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
