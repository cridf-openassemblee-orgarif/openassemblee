package openassemblee.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A ShortUid.
 */
@Entity
@Table(name = "short_uid")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "shortuid")
public class ShortUid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "uid", columnDefinition = "VARCHAR(16)")
    private String uid;

    @Column(name = "short_uid")
    private Long shortUid;

    public ShortUid() {}

    public ShortUid(String uid, Long shortUid) {
        this.uid = uid;
        this.shortUid = shortUid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public ShortUid uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getShortUid() {
        return shortUid;
    }

    public ShortUid shortUid(Long shortUid) {
        this.shortUid = shortUid;
        return this;
    }

    public void setShortUid(Long shortUid) {
        this.shortUid = shortUid;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShortUid shortUid = (ShortUid) o;
        if (shortUid.getUid() == null || getUid() == null) {
            return false;
        }
        return Objects.equals(getUid(), shortUid.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUid());
    }

    @Override
    public String toString() {
        return (
            "ShortUid{" +
            "uid=" +
            getUid() +
            ", shortUid='" +
            getShortUid() +
            "'" +
            "}"
        );
    }
}
