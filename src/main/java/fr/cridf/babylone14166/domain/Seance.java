package fr.cridf.babylone14166.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import fr.cridf.babylone14166.domain.enumeration.TypeSeance;

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
            '}';
    }
}
