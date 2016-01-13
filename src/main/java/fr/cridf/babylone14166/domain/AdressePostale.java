package fr.cridf.babylone14166.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import fr.cridf.babylone14166.domain.enumeration.NatureProPerso;
import fr.cridf.babylone14166.domain.enumeration.NiveauConfidentialite;

/**
 * A AdressePostale.
 */
@Entity
@Table(name = "adresse_postale")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "adressepostale")
public class AdressePostale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nature_pro_perso")
    private NatureProPerso natureProPerso;

    @Column(name = "voie")
    private String voie;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "ville")
    private String ville;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_confidentialite")
    private NiveauConfidentialite niveauConfidentialite;

    @Column(name = "adresse_de_correspondance")
    private Boolean adresseDeCorrespondance;

    @Column(name = "publication_annuaire")
    private Boolean publicationAnnuaire;

    public AdressePostale() {
    }

    public AdressePostale(NatureProPerso natureProPerso, String voie, String codePostal, String ville,
        NiveauConfidentialite niveauConfidentialite, Boolean adresseDeCorrespondance, Boolean publicationAnnuaire) {
        this.natureProPerso = natureProPerso;
        this.voie = voie;
        this.codePostal = codePostal;
        this.ville = ville;
        this.niveauConfidentialite = niveauConfidentialite;
        this.adresseDeCorrespondance = adresseDeCorrespondance;
        this.publicationAnnuaire = publicationAnnuaire;
    }

    public AdressePostale(String voie, String codePostal, String ville) {
        this.voie = voie;
        this.codePostal = codePostal;
        this.ville = ville;
    }

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

    public String getVoie() {
        return voie;
    }

    public void setVoie(String voie) {
        this.voie = voie;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
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
        AdressePostale adressePostale = (AdressePostale) o;
        return Objects.equals(id, adressePostale.id);
    }

    // TODO tester
    public String getOneline() {
        String adresse = "";
        adresse += voie != null ? voie : "";
        adresse += codePostal != null ? (adresse.equals("") ? "" : " ") + codePostal : "";
        adresse += " ";
        adresse += ville != null ? ville : (adresse.equals("") ? "Adresse inconnue" : "Ville inconnue");
        return adresse;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AdressePostale{" +
            "id=" + id +
            ", natureProPerso='" + natureProPerso + "'" +
            ", voie='" + voie + "'" +
            ", codePostal='" + codePostal + "'" +
            ", ville='" + ville + "'" +
            ", niveauConfidentialite='" + niveauConfidentialite + "'" +
            ", adresseDeCorrespondance='" + adresseDeCorrespondance + "'" +
            ", publicationAnnuaire='" + publicationAnnuaire + "'" +
            '}';
    }
}
