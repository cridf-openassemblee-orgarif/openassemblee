package openassemblee.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.*;
import openassemblee.domain.enumeration.AuditTrailAction;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A AuditTrail.
 */
@Entity
@Table(name = "audit_trail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "audittrail")
public class AuditTrail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "entity")
    private String entity;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "parent_entity")
    private String parentEntity;

    @Column(name = "parent_entity_id")
    private Long parentEntityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private AuditTrailAction action;

    @Column(name = "user")
    private String user;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "details")
    private String details;

    @Column(name = "dto")
    private String dto;

    @Column(name = "reason")
    private String reason;

    public AuditTrail() {}

    public AuditTrail(AuditTrailAction action, String details, String reason) {
        this.action = action;
        this.details = details;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(String parentEntity) {
        this.parentEntity = parentEntity;
    }

    public Long getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(Long parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public AuditTrailAction getAction() {
        return action;
    }

    public void setAction(AuditTrailAction action) {
        this.action = action;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDto() {
        return dto;
    }

    public void setDto(String dto) {
        this.dto = dto;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuditTrail auditTrail = (AuditTrail) o;
        return Objects.equals(id, auditTrail.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return (
            "AuditTrail{" +
            "id=" +
            id +
            ", entity='" +
            entity +
            "'" +
            ", entityId='" +
            entityId +
            "'" +
            ", parentEntity='" +
            parentEntity +
            "'" +
            ", parentEntityId='" +
            parentEntityId +
            "'" +
            ", action='" +
            action +
            "'" +
            ", user='" +
            user +
            "'" +
            ", date='" +
            date +
            "'" +
            ", details='" +
            details +
            "'" +
            ", reason='" +
            reason +
            "'" +
            '}'
        );
    }
}
