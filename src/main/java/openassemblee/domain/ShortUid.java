package openassemblee.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

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
    @Column(name = "uid", columnDefinition = "BINARY(16)")
    private UUID uid;

    @Column(name = "short_uid")
    private String shortUid;

    public ShortUid() {
    }

    public ShortUid(UUID uid, String shortUid) {
        this.uid = uid;
        this.shortUid = shortUid;
    }

    public UUID getUid() {
        return uid;
    }

    public ShortUid uid(UUID uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getShortUid() {
        return shortUid;
    }

    public ShortUid shortUid(String shortUid) {
        this.shortUid = shortUid;
        return this;
    }

    public void setShortUid(String shortUid) {
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
        return "ShortUid{" +
            "uid=" + getUid() +
            ", shortUid='" + getShortUid() + "'" +
            "}";
    }
}
