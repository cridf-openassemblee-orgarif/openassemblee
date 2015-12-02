package fr.cridf.babylone14166.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import fr.cridf.babylone14166.domain.enumeration.Civilite;

/**
 * A Elu.
 */
@Entity
@Table(name = "elu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "elu")
public class Elu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "civilite")
    private Civilite civilite;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "nom_jeune_fille")
    private String nomJeuneFille;

    @Column(name = "profession")
    private String profession;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    // TODO mlo pourquoi tests et la pratique ne donne pas le meme resultat
    @OneToMany//(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "elu_adresses_Postales",
        joinColumns = @JoinColumn(name = "elu_id"),
        inverseJoinColumns = @JoinColumn(name = "adresse_postale_id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<AdressePostale> adressesPostales = new ArrayList<>();

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

    public String getNomJeuneFille() {
        return nomJeuneFille;
    }

    public void setNomJeuneFille(String nomJeuneFille) {
        this.nomJeuneFille = nomJeuneFille;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public List<AdressePostale> getAdressesPostales() {
        return adressesPostales;
    }

    public void setAdressesPostales(List<AdressePostale> adressesPostales) {
        this.adressesPostales = adressesPostales;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Elu elu = (Elu) o;
        return Objects.equals(id, elu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Elu{" +
            "id=" + id +
            ", civilite='" + civilite + "'" +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", nomJeuneFille='" + nomJeuneFille + "'" +
            ", profession='" + profession + "'" +
            ", dateNaissance='" + dateNaissance + "'" +
            ", lieuNaissance='" + lieuNaissance + "'" +
            '}';
    }
}
