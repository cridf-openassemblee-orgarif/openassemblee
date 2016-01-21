package fr.cridf.babylone14166.service.dto;

import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.domain.FonctionGroupePolitique;

public class FonctionGroupePolitiqueDTO {

    private FonctionGroupePolitique fonctionGroupePolitique;
    private Elu elu;

    public FonctionGroupePolitiqueDTO(FonctionGroupePolitique fonctionGroupePolitique, Elu elu) {
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
