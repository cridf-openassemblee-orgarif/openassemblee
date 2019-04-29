package openassemblee.domain;

import openassemblee.domain.enumeration.NatureFixeMobile;
import openassemblee.domain.enumeration.NatureProPerso;
import openassemblee.domain.enumeration.NiveauConfidentialite;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A NumeroTelephone.
 */
@Entity
@Table(name = "numero_telephone")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "numerotelephone")
public class NumeroTelephone implements Publishable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nature_pro_perso")
    private NatureProPerso natureProPerso;

    @Enumerated(EnumType.STRING)
    @Column(name = "nature_fixe_mobile")
    private NatureFixeMobile natureFixeMobile;

    @Column(name = "numero")
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_confidentialite")
    private NiveauConfidentialite niveauConfidentialite;

    @Column(name = "publication_annuaire")
    private Boolean publicationAnnuaire;

    public NumeroTelephone() {
    }

    public NumeroTelephone(NatureProPerso natureProPerso, NatureFixeMobile natureFixeMobile, String numero, NiveauConfidentialite niveauConfidentialite, Boolean publicationAnnuaire) {
        this.natureProPerso = natureProPerso;
        this.natureFixeMobile = natureFixeMobile;
        this.numero = numero;
        this.niveauConfidentialite = niveauConfidentialite;
        this.publicationAnnuaire = publicationAnnuaire;
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

    public NatureFixeMobile getNatureFixeMobile() {
        return natureFixeMobile;
    }

    public void setNatureFixeMobile(NatureFixeMobile natureFixeMobile) {
        this.natureFixeMobile = natureFixeMobile;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public NiveauConfidentialite getNiveauConfidentialite() {
        return niveauConfidentialite;
    }

    public void setNiveauConfidentialite(NiveauConfidentialite niveauConfidentialite) {
        this.niveauConfidentialite = niveauConfidentialite;
    }

    @Override
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
        NumeroTelephone numeroTelephone = (NumeroTelephone) o;
        return Objects.equals(id, numeroTelephone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NumeroTelephone{" +
            "id=" + id +
            ", natureProPerso='" + natureProPerso + "'" +
            ", natureFixeMobile='" + natureFixeMobile + "'" +
            ", numero='" + numero + "'" +
            ", niveauConfidentialite='" + niveauConfidentialite + "'" +
            ", publicationAnnuaire='" + publicationAnnuaire + "'" +
            '}';
    }
}
