package fr.cridf.babylone14166.domain;

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
 * A ReunionCao.
 */
@Entity
@Table(name = "reunion_cao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reunioncao")
public class ReunionCao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "heure_debut")
    private String heureDebut;

    @Column(name = "heure_fin")
    private String heureFin;

    @OneToMany
    @JoinTable(name = "reunion_cao_presences_elus",
        joinColumns = @JoinColumn(name = "reunion_cao_id"),
        inverseJoinColumns = @JoinColumn(name = "presence_elu_id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PresenceElu> presenceElus = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public Set<PresenceElu> getPresenceElus() {
        return presenceElus;
    }

    public void setPresenceElus(Set<PresenceElu> presenceElus) {
        this.presenceElus = presenceElus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReunionCao reunionCao = (ReunionCao) o;
        return Objects.equals(id, reunionCao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReunionCao{" +
            "id=" + id +
            ", libelle='" + libelle + "'" +
            ", date='" + date + "'" +
            ", heureDebut='" + heureDebut + "'" +
            ", heureFin='" + heureFin + "'" +
            '}';
    }
}
