package openassemblee.web.rest.dto;

import openassemblee.domain.enumeration.Civilite;

public class HemicycleEluDTO {
    private Long id;
    private Civilite civilite;
    private String nom;
    private String prenom;
    private Long groupePolitiqueId;
    private String shortFonction;
    private Boolean actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Civilite getCivilite() {
        return civilite;
    }

    public void setCivilite(Civilite civilite) {
        this.civilite = civilite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Long getGroupePolitiqueId() {
        return groupePolitiqueId;
    }

    public void setGroupePolitiqueId(Long groupePolitiqueId) {
        this.groupePolitiqueId = groupePolitiqueId;
    }

    public String getShortFonction() {
        return shortFonction;
    }

    public void setShortFonction(String shortFonction) {
        this.shortFonction = shortFonction;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}
