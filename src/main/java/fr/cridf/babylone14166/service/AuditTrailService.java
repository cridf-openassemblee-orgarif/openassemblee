package fr.cridf.babylone14166.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cridf.babylone14166.domain.AuditTrail;
import fr.cridf.babylone14166.domain.enumeration.AuditTrailAction;
import fr.cridf.babylone14166.repository.AuditTrailRepository;
import fr.cridf.babylone14166.repository.search.AuditTrailSearchRepository;
import fr.cridf.babylone14166.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;

@Service
public class AuditTrailService {

    private final Logger logger = LoggerFactory.getLogger(AuditTrailService.class);

    @Inject
    private AuditTrailRepository auditTrailRepository;

    @Inject
    private AuditTrailSearchRepository auditTrailSearchRepository;

    @Inject
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = jackson2ObjectMapperBuilder.build();
    }

    public void logAuditTrail(AuditTrailAction action, Serializable entity) {
        logAuditTrail(action, entity, null, null, null);
    }

    public void logAuditTrail(AuditTrailAction action, Serializable entity
        , Class<Serializable> parentEntityClass, Long parentEntityId) {
        logAuditTrail(action, entity, parentEntityClass, parentEntityId, null);
    }

    public void logAuditTrail(AuditTrailAction action, Serializable entity, String reason) {
        logAuditTrail(action, entity, null, null, reason);
    }

    public void logAuditTrail(AuditTrailAction action, Serializable entity, Class<Serializable> parentEntityClass,
                              Long parentEntityId, String reason) {
        AuditTrail at = new AuditTrail();
        at.setEntity(entity.getClass().getSimpleName());
        at.setEntityId(getId(entity));
        if (parentEntityClass != null) {
            at.setParentEntity(parentEntityClass.getSimpleName());
            at.setParentEntityId(parentEntityId);
        }
        at.setAction(action);
        at.setUser(SecurityUtils.getCurrentUser().getUsername());
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

    // pas super clean mais pour le moment ne vaut pas le coup de tout impacter pour ça
    // l'autre possibilité simple étant de mettre l'id dans la signature de logAuditTrail()
    private Long getId(Serializable entity) {
        try {
            Method method = entity.getClass().getMethod("getId");
            return (Long) method.invoke(entity);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
