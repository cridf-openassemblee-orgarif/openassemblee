package openassemblee.web.rest.dto;

import java.io.Serializable;
import javax.persistence.Column;

public class InconsistencyDTO {

    @Column(name = "entity")
    private String entity;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "parent_entity")
    private String parentEntity;

    @Column(name = "parent_entity_id")
    private Long parentEntityId;

    public InconsistencyDTO() {}

    public <T extends Serializable, P extends Serializable> InconsistencyDTO(
        Class<T> entityClass,
        Long entityId,
        Class<P> parentEntityClass,
        Long parentEntityId
    ) {
        this.entity = entityClass.getSimpleName();
        this.entityId = entityId;
        this.parentEntity = parentEntityClass.getSimpleName();
        this.parentEntityId = parentEntityId;
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
}
