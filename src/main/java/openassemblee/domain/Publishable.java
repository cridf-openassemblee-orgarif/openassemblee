package openassemblee.domain;

import openassemblee.domain.enumeration.NiveauConfidentialite;

public interface Publishable {
    Boolean getPublicationAnnuaire();

    NiveauConfidentialite getNiveauConfidentialite();
}
