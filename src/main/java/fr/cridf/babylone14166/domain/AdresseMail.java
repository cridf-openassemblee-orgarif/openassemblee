package fr.cridf.babylone14166.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import fr.cridf.babylone14166.domain.enumeration.NatureProPerso;

import fr.cridf.babylone14166.domain.enumeration.NiveauConfidentialite;

/**
 * A AdresseMail.
 */
@Entity
@Table(name = "adresse_mail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "adressemail")
public class AdresseMail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nature_pro_perso")
    private NatureProPerso natureProPerso;

    @Column(name = "mail")
    private String mail;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_confidentialite")
    private NiveauConfidentialite niveauConfidentialite;

    @Column(name = "adresse_de_correspondance")
    private Boolean adresseDeCorrespondance;

    @Column(name = "publication_annuaire")
    private Boolean publicationAnnuaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NatureProPerso getNatureProPerso() {
        return natureProPerso;
    }

    public void setNatureProPerso(NatureProPerso natureProPerso) {
        this.natureProPerso = natureProPerso;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public NiveauConfidentialite getNiveauConfidentialite() {
        return niveauConfidentialite;
    }

    public void setNiveauConfidentialite(NiveauConfidentialite niveauConfidentialite) {
        this.niveauConfidentialite = niveauConfidentialite;
    }

    public Boolean getAdresseDeCorrespondance() {
        return adresseDeCorrespondance;
    }

    public void setAdresseDeCorrespondance(Boolean adresseDeCorrespondance) {
        this.adresseDeCorrespondance = adresseDeCorrespondance;
    }

    public Boolean getPublicationAnnuaire() {
        return publicationAnnuaire;
    }

    public void setPublicationAnnuaire(Boolean publicationAnnuaire) {
        this.publicationAnnuaire = publicationAnnuaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdresseMail adresseMail = (AdresseMail) o;
        return Objects.equals(id, adresseMail.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AdresseMail{" +
            "id=" + id +
            ", natureProPerso='" + natureProPerso + "'" +
            ", mail='" + mail + "'" +
            ", niveauConfidentialite='" + niveauConfidentialite + "'" +
            ", adresseDeCorrespondance='" + adresseDeCorrespondance + "'" +
            ", publicationAnnuaire='" + publicationAnnuaire + "'" +
            '}';
    }
}
