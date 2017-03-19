package fr.cridf.babylone14166.domain;

import fr.cridf.babylone14166.domain.enumeration.NiveauConfidentialite;

public interface Publishable {

    Boolean getPublicationAnnuaire();

    NiveauConfidentialite getNiveauConfidentialite();

}
