package fr.cridf.babylone14166.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.cridf.babylone14166.domain.jackson.JacksonEluLightSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A PresenceElu.
 */
@Entity
@Table(name = "presence_elu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "presenceelu")
public class PresenceElu extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "elu_id")
    @JsonSerialize(using = JacksonEluLightSerializer.class)
    private Elu elu;

    @OneToMany(mappedBy = "presenceElu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private Set<Signature> signatures = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Elu getElu() {
        return elu;
    }

    public void setElu(Elu elu) {
        this.elu = elu;
    }

    public Set<Signature> getSignatures() {
        return signatures;
    }

    public void setSignatures(Set<Signature> signatures) {
        this.signatures = signatures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PresenceElu presenceElu = (PresenceElu) o;
        return Objects.equals(id, presenceElu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PresenceElu{" +
            "id=" + id +
            '}';
    }
}
