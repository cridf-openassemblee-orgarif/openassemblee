package openassemblee.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;
import openassemblee.domain.enumeration.AuditTrailAction;

/**
 * A DTO for the AuditTrail entity.
 */
public class AuditTrailDTO implements Serializable {

    private Long id;

    private String entity;

    private Long entityId;

    private String parentEntity;

    private Long parentEntityId;

    private AuditTrailAction action;

    private String user;

    private String date;

    private String details;

    private String dto;

    private String reason;

    public AuditTrailDTO() {}

    public AuditTrailDTO(
        Long id,
        String entity,
        Long entityId,
        String parentEntity,
        Long parentEntityId,
        AuditTrailAction action,
        String user,
        String date,
        String details,
        String dto,
        String reason
    ) {
        this.id = id;
        this.entity = entity;
        this.entityId = entityId;
        this.parentEntity = parentEntity;
        this.parentEntityId = parentEntityId;
        this.action = action;
        this.user = user;
        this.date = date;
        this.details = details;
        this.dto = dto;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

        AuditTrailDTO auditTrailDTO = (AuditTrailDTO) o;

        if (!Objects.equals(id, auditTrailDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return (
            "AuditTrailDTO{" +
            "id=" +
            id +
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
