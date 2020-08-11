package openassemblee.domain;

import openassemblee.domain.enumeration.TypeSeance;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Seance.
 */
@Entity
@Table(name = "seance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "seance")
public class Seance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "intitule")
    private String intitule;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeSeance type;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "nombre_signatures")
    private Integer nombreSignatures;

    @OneToMany
    @JoinTable(name = "seance_presences_elus",
        joinColumns = @JoinColumn(name = "seance_id"),
        inverseJoinColumns = @JoinColumn(name = "presence_elu_id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PresenceElu> presenceElus = new HashSet<>();

    @OneToMany
    @JoinTable(name = "seance_hemicycle_archive",
        joinColumns = @JoinColumn(name = "seance_id"),
        inverseJoinColumns = @JoinColumn(name = "hemicycle_archive_id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<HemicycleArchive> archives = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public TypeSeance getType() {
        return type;
    }

    public void setType(TypeSeance type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getNombreSignatures() {
        return nombreSignatures;
    }

    public void setNombreSignatures(Integer nombreSignatures) {
        this.nombreSignatures = nombreSignatures;
    }

    public Set<PresenceElu> getPresenceElus() {
        return presenceElus;
    }

    public void setPresenceElus(Set<PresenceElu> PresenceElus) {
        this.presenceElus = PresenceElus;
    }

    public Set<HemicycleArchive> getArchives() {
        return archives;
    }

    public void setArchives(Set<HemicycleArchive> archives) {
        this.archives = archives;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Seance seance = (Seance) o;
        return Objects.equals(id, seance.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Seance{" +
            "id=" + id +
            ", intitule='" + intitule + "'" +
            ", type='" + type + "'" +
            ", date='" + date + "'" +
            ", nombreSignatures='" + nombreSignatures + "'" +
            '}';
    }
}
