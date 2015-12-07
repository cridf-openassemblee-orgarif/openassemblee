package fr.cridf.babylone14166.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AppartenanceCommissionPermanente.
 */
@Entity
@Table(name = "appartenance_commission_permanente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appartenancecommissionpermanente")
public class AppartenanceCommissionPermanente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppartenanceCommissionPermanente appartenanceCommissionPermanente = (AppartenanceCommissionPermanente) o;
        return Objects.equals(id, appartenanceCommissionPermanente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AppartenanceCommissionPermanente{" +
            "id=" + id +
            '}';
    }
}
