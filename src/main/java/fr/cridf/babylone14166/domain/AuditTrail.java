package fr.cridf.babylone14166.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import fr.cridf.babylone14166.domain.enumeration.AuditLogAction;

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
    private String entityId;

    @Column(name = "parent_entity")
    private String parentEntity;

    @Column(name = "parent_entity_id")
    private String parentEntityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private AuditLogAction action;

    @Column(name = "user")
    private String user;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "details")
    private String details;

    @Column(name = "reason")
    private String reason;

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

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(String parentEntity) {
        this.parentEntity = parentEntity;
    }

    public String getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(String parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public AuditLogAction getAction() {
        return action;
    }

    public void setAction(AuditLogAction action) {
        this.action = action;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
        return "AuditTrail{" +
            "id=" + id +
            ", entity='" + entity + "'" +
            ", entityId='" + entityId + "'" +
            ", parentEntity='" + parentEntity + "'" +
            ", parentEntityId='" + parentEntityId + "'" +
            ", action='" + action + "'" +
            ", user='" + user + "'" +
            ", date='" + date + "'" +
            ", details='" + details + "'" +
            ", reason='" + reason + "'" +
            '}';
    }
}
