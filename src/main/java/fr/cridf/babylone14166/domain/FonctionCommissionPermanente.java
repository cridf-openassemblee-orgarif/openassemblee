package fr.cridf.babylone14166.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * A FonctionCommissionPermanente.
 */
@Entity
@Table(name = "fonction_commission_permanente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fonctioncommissionpermanente")
public class FonctionCommissionPermanente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fonction")
    private String fonction;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "motif_fin")
    private String motifFin;

    @ManyToOne
    @JoinColumn(name = "elu_id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonBackReference
    private Elu elu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotifFin() {
        return motifFin;
    }

    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    public Elu getElu() {
        return elu;
    }

    public void setElu(Elu elu) {
        this.elu = elu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FonctionCommissionPermanente fonctionCommissionPermanente = (FonctionCommissionPermanente) o;
        return Objects.equals(id, fonctionCommissionPermanente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FonctionCommissionPermanente{" +
            "id=" + id +
            ", fonction='" + fonction + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateFin='" + dateFin + "'" +
            ", motifFin='" + motifFin + "'" +
            '}';
    }
}
