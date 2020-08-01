package openassemblee.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HemicycleArchive.
 */
@Entity
@Table(name = "hemicycle_archive")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "hemicyclearchive")
public class HemicycleArchive implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "json_plan")
    private String jsonPlan;

    @Column(name = "svg_plan")
    private String svgPlan;

    @Column(name = "date")
    private ZonedDateTime date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonPlan() {
        return jsonPlan;
    }

    public void setJsonPlan(String jsonPlan) {
        this.jsonPlan = jsonPlan;
    }

    public String getSvgPlan() {
        return svgPlan;
    }

    public void setSvgPlan(String svgPlan) {
        this.svgPlan = svgPlan;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
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
        HemicycleArchive hemicycleArchive = (HemicycleArchive) o;
        return Objects.equals(id, hemicycleArchive.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HemicycleArchive{" +
            "id=" + id +
            ", jsonPlan='" + jsonPlan + "'" +
            ", svgPlan='" + svgPlan + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
