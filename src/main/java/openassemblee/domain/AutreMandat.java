package openassemblee.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;
import openassemblee.domain.jackson.JacksonEluIdSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A AutreMandat.
 */
@Entity
@Table(name = "autre_mandat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "autremandat")
public class AutreMandat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "collectivite_ou_organisme")
    private String collectiviteOuOrganisme;

    @Column(name = "fonction")
    private String fonction;

    @Column(name = "date_debut_string")
    private String dateDebutString;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "motif_fin")
    private String motifFin;

    @ManyToOne
    @JoinColumn(name = "elu_id")
    @JsonSerialize(using = JacksonEluIdSerializer.class)
    private Elu elu;

    @ManyToOne
    @JoinColumn(name = "mandature_id")
    private Mandature mandature;

    public AutreMandat() {}

    public AutreMandat(
        String collectiviteOuOrganisme,
        String fonction,
        String dateDebutString,
        Elu elu
    ) {
        this.collectiviteOuOrganisme = collectiviteOuOrganisme;
        this.fonction = fonction;
        this.dateDebutString = dateDebutString;
        this.elu = elu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCollectiviteOuOrganisme() {
        return collectiviteOuOrganisme;
    }

    public void setCollectiviteOuOrganisme(String collectiviteOuOrganisme) {
        this.collectiviteOuOrganisme = collectiviteOuOrganisme;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getDateDebutString() {
        return dateDebutString;
    }

    public void setDateDebutString(String dateDebutString) {
        this.dateDebutString = dateDebutString;
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
        AutreMandat autreMandat = (AutreMandat) o;
        return Objects.equals(id, autreMandat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return (
            "AutreMandat{" +
            "id=" +
            id +
            ", collectiviteOuOrganisme='" +
            collectiviteOuOrganisme +
            "'" +
            ", fonction='" +
            fonction +
            "'" +
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
