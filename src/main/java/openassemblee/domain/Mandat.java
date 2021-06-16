package openassemblee.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import openassemblee.domain.jackson.JacksonEluIdSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Mandat.
 */
@Entity
@Table(name = "mandat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "mandat")
public class Mandat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "code_departement")
    private String codeDepartement;

    @Column(name = "departement")
    private String departement;

    @Column(name = "date_demission")
    private LocalDate dateDemission;

    @Column(name = "motif_demission")
    private String motifDemission;

    @ManyToOne
    @JoinColumn(name = "elu_id")
    @JsonSerialize(using = JacksonEluIdSerializer.class)
    private Elu elu;

    @ManyToOne
    @JoinColumn(name = "mandature_id")
    private Mandature mandature;

    @ManyToOne
    @JoinColumn(name = "liste_electorale_id")
    private ListeElectorale listeElectorale;

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

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public LocalDate getDateDemission() {
        return dateDemission;
    }

    public void setDateDemission(LocalDate dateDemission) {
        this.dateDemission = dateDemission;
    }

    public String getMotifDemission() {
        return motifDemission;
    }

    public void setMotifDemission(String motifDemission) {
        this.motifDemission = motifDemission;
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

    public ListeElectorale getListeElectorale() {
        return listeElectorale;
    }

    public void setListeElectorale(ListeElectorale listeElectorale) {
        this.listeElectorale = listeElectorale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mandat mandat = (Mandat) o;
        return Objects.equals(id, mandat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Mandat{" +
            "id=" + id +
            ", dateDebut='" + dateDebut + "'" +
            ", codeDepartement='" + codeDepartement + "'" +
            ", departement='" + departement + "'" +
            ", dateDemission='" + dateDemission + "'" +
            ", motifDemission='" + motifDemission + "'" +
            '}';
    }
}
