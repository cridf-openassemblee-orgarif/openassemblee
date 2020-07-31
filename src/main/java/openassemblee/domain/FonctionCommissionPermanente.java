package openassemblee.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import openassemblee.domain.jackson.JacksonEluIdSerializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A FonctionCommissionPermanente.
 */
@Entity
@Table(name = "fonction_commission_permanente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fonctioncommissionpermanente")
public class FonctionCommissionPermanente implements Serializable {

    protected static final Log logger = LogFactory.getLog(FonctionCommissionPermanente.class);

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
        return "FCP" + id;
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

    public static String getShortFonction(String fonction) {
        String cleanFonction = StringUtils.stripAccents(fonction != null ? fonction.toLowerCase() : "");
        if (cleanFonction.contains("vice") && cleanFonction.contains("president")) {
            return "VP";
        }
        if (cleanFonction.contains("delegue") && cleanFonction.contains("special")) {
            return "DS";
        }
        if (cleanFonction.contains("presidente")) {
            return "Présidente";
        }
        if (cleanFonction.contains("president")) {
            return "Président";
        }
        if(cleanFonction.trim().equals("")) {
            return null;
        }
        logger.error("Unknown short fonction " + fonction);
        return null;
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

    public static boolean isFonctionCourante(FonctionCommissionPermanente f) {
        // later remettre || a.getDateFin().isAfter(LocalDate.now());
        return f.getDateFin() == null;
    }
}
