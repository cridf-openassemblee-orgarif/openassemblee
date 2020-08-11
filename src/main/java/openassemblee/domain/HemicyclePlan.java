package openassemblee.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A HemicyclePlan.
 */
@Entity
@Table(name = "hemicycle_plan")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "hemicycleplan")
public class HemicyclePlan implements Serializable {

    public static class Association {
        public Integer chairNumber;
        public Long eluId;

        // for serialization
        public Association() {
        }

        public Association(Integer chairNumber, Long eluId) {
            this.chairNumber = chairNumber;
            this.eluId = eluId;
        }
    }

    public static class JsonPlan {
        public List<Association> associations;

        // for serialization
        public JsonPlan() {}

        public JsonPlan(List<Association> associations) {
            this.associations = associations;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "json_plan")
    private String jsonPlan;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_modification_date")
    private ZonedDateTime lastModificationDate;

    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private HemicycleConfiguration configuration;

    @OneToOne
    private Seance seance;

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

    public String getJsonPlan() {
        return jsonPlan;
    }

    public void setJsonPlan(String jsonPlan) {
        this.jsonPlan = jsonPlan;
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

    public HemicycleConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(HemicycleConfiguration hemicycleConfiguration) {
        this.configuration = hemicycleConfiguration;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance Seance) {
        this.seance = Seance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HemicyclePlan hemicyclePlan = (HemicyclePlan) o;
        return Objects.equals(id, hemicyclePlan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HemicyclePlan{" +
            "id=" + id +
            ", label='" + label + "'" +
            ", jsonPlan='" + jsonPlan + "'" +
            ", creationDate='" + creationDate + "'" +
            ", lastModificationDate='" + lastModificationDate + "'" +
            '}';
    }
}
