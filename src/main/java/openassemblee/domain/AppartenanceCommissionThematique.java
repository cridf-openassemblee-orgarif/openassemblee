package openassemblee.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;
import openassemblee.domain.jackson.JacksonCommissionThematiqueIdSerializer;
import openassemblee.domain.jackson.JacksonEluIdSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A AppartenanceCommissionThematique.
 */
@Entity
@Table(name = "appartenance_commission_thematique")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appartenancecommissionthematique")
public class AppartenanceCommissionThematique implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "motif_fin")
    private String motifFin;

    @Column(name = "import_uid")
    private String importUid;

    @ManyToOne
    @JoinColumn(name = "elu_id")
    @JsonSerialize(using = JacksonEluIdSerializer.class)
    private Elu elu;

    @ManyToOne
    @JoinColumn(name = "commission_thematique_id")
    @JsonSerialize(using = JacksonCommissionThematiqueIdSerializer.class)
    private CommissionThematique commissionThematique;

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

    public String getImportUid() {
        return importUid;
    }

    public String exportUid() {
        return "ACT" + id;
    }

    public void setImportUid(String importUid) {
        this.importUid = importUid;
    }

    public Elu getElu() {
        return elu;
    }

    public void setElu(Elu elu) {
        this.elu = elu;
    }

    public CommissionThematique getCommissionThematique() {
        return commissionThematique;
    }

    public void setCommissionThematique(
        CommissionThematique commissionThematique
    ) {
        this.commissionThematique = commissionThematique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppartenanceCommissionThematique appartenanceCommissionThematique =
            (AppartenanceCommissionThematique) o;
        return Objects.equals(id, appartenanceCommissionThematique.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return (
            "AppartenanceCommissionThematique{" +
            "id=" +
            id +
            ", dateDebut='" +
            dateDebut +
            "'" +
            ", dateFin='" +
            dateFin +
            "'" +
            ", motifFin='" +
            motifFin +
            "'" +
            '}'
        );
    }
}
