package openassemblee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import openassemblee.domain.AuditTrail;
import openassemblee.domain.enumeration.AuditTrailAction;
import openassemblee.repository.AuditTrailRepository;
import openassemblee.repository.search.AuditTrailSearchRepository;
import openassemblee.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.ZonedDateTime;

import static openassemblee.domain.enumeration.AuditTrailAction.*;

@Service
public class AuditTrailService {

    private final Logger logger = LoggerFactory.getLogger(AuditTrailService.class);

    @Inject
    private AuditTrailRepository auditTrailRepository;

    @Inject
    private AuditTrailSearchRepository auditTrailSearchRepository;

    @Inject
    private ObjectMapper objectMapper;

    public <T extends Serializable> void logCreation(T entity, Long id) {
        logAuditTrail(CREATE, entity.getClass(), id, entity, null, null, null);
    }

    public <T extends Serializable, P extends Serializable> void logCreation(T entity, Long id,
                                                                             Class<P> parentEntityClass,
                                                                             Long parentId) {
        logAuditTrail(CREATE, entity.getClass(), id, entity, parentEntityClass, parentId, null);
    }

    public <T extends Serializable> void logUpdate(T entity, Long id) {
        logAuditTrail(UPDATE, entity.getClass(), id, entity, null, null, null);
    }

    public <T extends Serializable, P extends Serializable> void logUpdate(T entity, Long id,
                                                                           Class<P> parentEntityClass,
                                                                           Long parentId) {
        logAuditTrail(UPDATE, entity.getClass(), id, entity, parentEntityClass, parentId, null);
    }

    public <T extends Serializable> void logDeletion(Class<T> entityClass, Long id) {
        logAuditTrail(DELETE, entityClass, id, null, null, null, null);
    }

    public <T extends Serializable, P extends Serializable> void logDeletion(Class<T> entityClass, Long id,
                                                                             Class<P> parentEntityClass, Long parentId) {
        logAuditTrail(DELETE, entityClass, id, null, parentEntityClass, parentId, null);
    }

    private <T extends Serializable, P extends Serializable> void
    logAuditTrail(AuditTrailAction action, Class<T> entityClass, Long entityId, Serializable entity,
                  Class<P> parentEntityClass, Long parentEntityId, String reason) {
        AuditTrail at = new AuditTrail();
        at.setEntity(entityClass.getSimpleName());
        at.setEntityId(entityId);
        if (parentEntityClass != null) {
            at.setParentEntity(parentEntityClass.getSimpleName());
            at.setParentEntityId(parentEntityId);
        }
        at.setAction(action);
        if(SecurityUtils.isAuthenticated()) {
            at.setUser(SecurityUtils.getCurrentUser().getUsername());
        }
        at.setDate(ZonedDateTime.now());
        try {
            String jsonEntity = objectMapper.writeValueAsString(entity);
            at.setDetails(jsonEntity);
        } catch (JsonProcessingException e) {
            logger.error("Couldn't write auditTrail json", e);
            // TODO est peut-être un peu hard ... ?
            throw new RuntimeException(e);
        }
        at.setReason(reason);
        auditTrailRepository.save(at);
        auditTrailSearchRepository.save(at);
    }

}
