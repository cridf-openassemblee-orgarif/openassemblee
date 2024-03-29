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
 * A FonctionExecutive.
 */
@Entity
@Table(name = "fonction_executive")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fonctionexecutive")
public class FonctionExecutive implements Serializable {

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

    @Column(name = "import_uid")
    private String importUid;

    @ManyToOne
    @JoinColumn(name = "elu_id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonSerialize(using = JacksonEluIdSerializer.class)
    private Elu elu;

    @ManyToOne
    @JoinColumn(name = "mandature_id")
    private Mandature mandature;

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

    public String getImportUid() {
        return importUid;
    }

    public String exportUid() {
        return "FE" + id;
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
        FonctionExecutive fonctionExecutive = (FonctionExecutive) o;
        return Objects.equals(id, fonctionExecutive.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return (
            "FonctionExecutive{" +
            "id=" +
            id +
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

    public static boolean isFonctionCourante(FonctionExecutive f) {
        // later remettre || a.getDateFin().isAfter(LocalDate.now());
        return f.getDateFin() == null;
    }
}
