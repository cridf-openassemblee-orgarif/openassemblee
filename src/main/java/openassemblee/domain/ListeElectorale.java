package openassemblee.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ListeElectorale.
 */
@Entity
@Table(name = "liste_electorale")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "listeelectorale")
public class ListeElectorale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "nom_court")
    private String nomCourt;

    @ManyToOne
    @JoinColumn(name = "mandature_id")
    private Mandature mandature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomCourt() {
        return nomCourt;
    }

    public void setNomCourt(String nomCourt) {
        this.nomCourt = nomCourt;
    }

    public Mandature getMandature() {
        return mandature;
    }

    public void setMandature(Mandature mandature) {
        this.mandature = mandature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListeElectorale listeElectorale = (ListeElectorale) o;
        return Objects.equals(id, listeElectorale.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ListeElectorale{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", nomCourt='" + nomCourt + "'" +
            '}';
    }
}
