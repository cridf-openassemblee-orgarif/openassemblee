package openassemblee.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import openassemblee.domain.jackson.JacksonEluIdSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DistinctionHonorifique.
 */
@Entity
@Table(name = "distinction_honorifique")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "distinctionhonorifique")
public class DistinctionHonorifique implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "date")
    private String date;

    @ManyToOne
    @JoinColumn(name = "elu_id")
    @JsonSerialize(using = JacksonEluIdSerializer.class)
    private Elu elu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        DistinctionHonorifique distinctionHonorifique = (DistinctionHonorifique) o;
        return Objects.equals(id, distinctionHonorifique.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DistinctionHonorifique{" +
            "id=" + id +
            ", titre='" + titre + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
