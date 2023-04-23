package openassemblee.service.dto;

import openassemblee.domain.Pouvoir;

public class PouvoirListDTO {

    private Pouvoir pouvoir;
    private EluListDTO eluCedeur;
    private EluListDTO eluBeneficiaire;

    public PouvoirListDTO(
        Pouvoir pouvoir,
        EluListDTO eluCedeur,
        EluListDTO eluBeneficiaire
    ) {
        this.pouvoir = pouvoir;
        this.eluCedeur = eluCedeur;
        this.eluBeneficiaire = eluBeneficiaire;
    }

    public Pouvoir getPouvoir() {
        return pouvoir;
    }

    public EluListDTO getEluCedeur() {
        return eluCedeur;
    }

    public EluListDTO getEluBeneficiaire() {
        return eluBeneficiaire;
    }
}
