package fr.cridf.babylone14166.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fr.cridf.babylone14166.domain.enumeration.SignatureStatus;

/**
 * A Signature.
 */
@Entity
@Table(name = "signature")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "signature")
public class Signature implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "position")
    private Integer position;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private SignatureStatus statut;

    @ManyToOne
    @JoinColumn(name = "presence_elu_id")
    private PresenceElu presenceElu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public SignatureStatus getStatut() {
        return statut;
    }

    public void setStatut(SignatureStatus statut) {
        this.statut = statut;
    }

    public PresenceElu getPresenceElu() {
        return presenceElu;
    }

    public void setPresenceElu(PresenceElu presenceElu) {
        this.presenceElu = presenceElu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Signature signature = (Signature) o;
        return Objects.equals(id, signature.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Signature{" +
            "id=" + id +
            ", position='" + position + "'" +
            ", statut='" + statut + "'" +
            '}';
    }
}
