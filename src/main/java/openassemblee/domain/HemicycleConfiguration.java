package openassemblee.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HemicycleConfiguration.
 */
@Entity
@Table(name = "hemicycle_configuration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "hemicycleconfiguration")
public class HemicycleConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "json_configuration")
    private String jsonConfiguration;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_modification_date")
    private ZonedDateTime lastModificationDate;

    @Column(name = "frozen")
    private Boolean frozen;

    @Column(name = "frozen_date")
    private ZonedDateTime frozenDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getJsonConfiguration() {
        return jsonConfiguration;
    }

    public void setJsonConfiguration(String jsonConfiguration) {
        this.jsonConfiguration = jsonConfiguration;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(ZonedDateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public ZonedDateTime getFrozenDate() {
        return frozenDate;
    }

    public void setFrozenDate(ZonedDateTime frozenDate) {
        this.frozenDate = frozenDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HemicycleConfiguration hemicycleConfiguration = (HemicycleConfiguration) o;
        return Objects.equals(id, hemicycleConfiguration.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HemicycleConfiguration{" +
            "id=" + id +
            ", label='" + label + "'" +
            ", jsonConfiguration='" + jsonConfiguration + "'" +
            ", creationDate='" + creationDate + "'" +
            ", lastModificationDate='" + lastModificationDate + "'" +
            ", frozen='" + frozen + "'" +
            ", frozenDate='" + frozenDate + "'" +
            '}';
    }
}
