package fr.cridf.babylone14166.domain;

import fr.cridf.babylone14166.domain.enumeration.NatureProPerso;
import fr.cridf.babylone14166.domain.enumeration.NiveauConfidentialite;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A NumeroFax.
 */
@Entity
@Table(name = "numero_fax")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "numerofax")
public class NumeroFax implements Publishable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nature_pro_perso")
    private NatureProPerso natureProPerso;

    @Column(name = "numero")
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_confidentialite")
    private NiveauConfidentialite niveauConfidentialite;

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
        NumeroFax numeroFax = (NumeroFax) o;
        return Objects.equals(id, numeroFax.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NumeroFax{" +
            "id=" + id +
            ", natureProPerso='" + natureProPerso + "'" +
            ", numero='" + numero + "'" +
            ", niveauConfidentialite='" + niveauConfidentialite + "'" +
            ", publicationAnnuaire='" + publicationAnnuaire + "'" +
            '}';
    }
}
