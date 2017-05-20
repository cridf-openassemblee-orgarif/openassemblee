package openassemblee.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Pouvoir.
 */
@Entity
@Table(name = "pouvoir")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pouvoir")
public class Pouvoir implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "heure_debut")
    private String heureDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "heure_fin")
    private String heureFin;

    @ManyToOne
    @JoinColumn(name = "elu_cedeur_id")
    private Elu eluCedeur;

    @ManyToOne
    @JoinColumn(name = "elu_beneficiaire_id")
    private Elu eluBeneficiaire;

    @ManyToOne
    @JoinColumn(name = "seance_id")
    private Seance seance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public Elu getEluCedeur() {
        return eluCedeur;
    }

    public void setEluCedeur(Elu elu) {
        this.eluCedeur = elu;
    }

    public Elu getEluBeneficiaire() {
        return eluBeneficiaire;
    }

    public void setEluBeneficiaire(Elu elu) {
        this.eluBeneficiaire = elu;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pouvoir pouvoir = (Pouvoir) o;
        return Objects.equals(id, pouvoir.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pouvoir{" +
            "id=" + id +
            ", dateDebut='" + dateDebut + "'" +
            ", heureDebut='" + heureDebut + "'" +
            ", dateFin='" + dateFin + "'" +
            ", heureFin='" + heureFin + "'" +
            '}';
    }
}
