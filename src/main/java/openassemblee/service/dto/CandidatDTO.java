package openassemblee.service.dto;

import java.time.LocalDate;
import openassemblee.domain.enumeration.Civilite;

public class CandidatDTO {

    private Civilite civilite;
    private String prenom;
    private String nom;
    private LocalDate dateNaissance;
    private String listeElectorale;
    private String listeElectoraleCourt;
    private String departement;
    private Integer codeDepartement;

    public CandidatDTO() {}

    public CandidatDTO(
        Civilite civilite,
        String prenom,
        String nom,
        LocalDate dateNaissance,
        String listeElectorale,
        String listeElectoraleCourt,
        String departement,
        Integer codeDepartement
    ) {
        this.civilite = civilite;
        this.prenom = prenom;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.listeElectorale = listeElectorale;
        this.listeElectoraleCourt = listeElectoraleCourt;
        this.departement = departement;
        this.codeDepartement = codeDepartement;
    }

    public Civilite getCivilite() {
        return civilite;
    }

    public void setCivilite(Civilite civilite) {
        this.civilite = civilite;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getListeElectorale() {
        return listeElectorale;
    }

    public void setListeElectorale(String listeElectorale) {
        this.listeElectorale = listeElectorale;
    }

    public String getListeElectoraleCourt() {
        return listeElectoraleCourt;
    }

    public void setListeElectoraleCourt(String listeElectoraleCourt) {
        this.listeElectoraleCourt = listeElectoraleCourt;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public Integer getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(Integer codeDepartement) {
        this.codeDepartement = codeDepartement;
    }
}
