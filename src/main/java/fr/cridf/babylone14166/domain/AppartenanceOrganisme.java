package fr.cridf.babylone14166.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A AppartenanceOrganisme.
 */
@Entity
@Table(name = "appartenance_organisme")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appartenanceorganisme")
public class AppartenanceOrganisme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "statut")
    private String statut;

    @Column(name = "code_rne")
    private String codeRNE;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "motif_fin")
    private String motifFin;

    @Column(name = "date_nomination")
    private LocalDate dateNomination;

    @Column(name = "reference")
    private String reference;

    @Column(name = "type")
    private String type;

    @Column(name = "lien_piece")
    private String lienPiece;

    @ManyToOne
    @JoinColumn(name = "elu_id")
    private Elu elu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCodeRNE() {
        return codeRNE;
    }

    public void setCodeRNE(String codeRNE) {
        this.codeRNE = codeRNE;
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

    public LocalDate getDateNomination() {
        return dateNomination;
    }

    public void setDateNomination(LocalDate dateNomination) {
        this.dateNomination = dateNomination;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLienPiece() {
        return lienPiece;
    }

    public void setLienPiece(String lienPiece) {
        this.lienPiece = lienPiece;
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
        AppartenanceOrganisme appartenanceOrganisme = (AppartenanceOrganisme) o;
        return Objects.equals(id, appartenanceOrganisme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AppartenanceOrganisme{" +
            "id=" + id +
            ", statut='" + statut + "'" +
            ", codeRNE='" + codeRNE + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateFin='" + dateFin + "'" +
            ", motifFin='" + motifFin + "'" +
            ", dateNomination='" + dateNomination + "'" +
            ", reference='" + reference + "'" +
            ", type='" + type + "'" +
            ", lienPiece='" + lienPiece + "'" +
            '}';
    }
}
