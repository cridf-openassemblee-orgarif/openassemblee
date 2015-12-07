package fr.cridf.babylone14166.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FonctionCommissionPermanente.
 */
@Entity
@Table(name = "fonction_commission_permanente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fonctioncommissionpermanente")
public class FonctionCommissionPermanente implements Serializable {

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
        FonctionCommissionPermanente fonctionCommissionPermanente = (FonctionCommissionPermanente) o;
        return Objects.equals(id, fonctionCommissionPermanente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FonctionCommissionPermanente{" +
            "id=" + id +
            '}';
    }
}
