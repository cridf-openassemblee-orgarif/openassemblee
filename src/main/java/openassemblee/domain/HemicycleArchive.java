package openassemblee.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
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

    @Column(name = "json_archive")
    private String jsonArchive;

    @Column(name = "svg_plan")
    private String svgPlan;

    @Column(name = "date")
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "hemicycle_plan_id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private HemicyclePlan hemicyclePlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonArchive() {
        return jsonArchive;
    }

    public void setJsonArchive(String jsonArchive) {
        this.jsonArchive = jsonArchive;
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

    public HemicyclePlan getHemicyclePlan() {
        return hemicyclePlan;
    }

    public void setHemicyclePlan(HemicyclePlan hemicyclePlan) {
        this.hemicyclePlan = hemicyclePlan;
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
            ", jsonPlan='" + jsonArchive + "'" +
            ", svgPlan='" + svgPlan + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
