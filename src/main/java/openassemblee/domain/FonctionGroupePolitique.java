package openassemblee.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import openassemblee.domain.jackson.JacksonEluIdSerializer;
import openassemblee.domain.jackson.JacksonGroupePolitiqueIdSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A FonctionGroupePolitique.
 */
@Entity
@Table(name = "fonction_groupe_politique")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fonctiongroupepolitique")
public class FonctionGroupePolitique implements Serializable {

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
    @JsonSerialize(using = JacksonEluIdSerializer.class)
    private Elu elu;

    @ManyToOne
    @JoinColumn(name = "groupe_politique_id")
    @JsonSerialize(using = JacksonGroupePolitiqueIdSerializer.class)
    private GroupePolitique groupePolitique;

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
        return "FGP" + id;
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

    public GroupePolitique getGroupePolitique() {
        return groupePolitique;
    }

    public void setGroupePolitique(GroupePolitique groupePolitique) {
        this.groupePolitique = groupePolitique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FonctionGroupePolitique fonctionGroupePolitique = (FonctionGroupePolitique) o;
        return Objects.equals(id, fonctionGroupePolitique.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FonctionGroupePolitique{" +
            "id=" + id +
            ", fonction='" + fonction + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateFin='" + dateFin + "'" +
            ", motifFin='" + motifFin + "'" +
            '}';
    }
}
