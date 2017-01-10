package fr.cridf.babylone14166.service;

import fr.cridf.babylone14166.config.audit.AuditEventConverter;
import fr.cridf.babylone14166.domain.PersistentAuditEvent;
import fr.cridf.babylone14166.repository.PersistenceAuditEventRepository;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Service for managing audit events.
 * <p/>
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 * </p>
 */
@Service
public class AuditEventService {

    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    private AuditEventConverter auditEventConverter;

    @Inject
    public AuditEventService(
        PersistenceAuditEventRepository persistenceAuditEventRepository,
        AuditEventConverter auditEventConverter) {

        this.persistenceAuditEventRepository = persistenceAuditEventRepository;
        this.auditEventConverter = auditEventConverter;
    }

    @Transactional(readOnly = true)
    public List<AuditEvent> findAll() {
        return auditEventConverter.convertToAuditEvent(persistenceAuditEventRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate) {
        List<PersistentAuditEvent> persistentAuditEvents =
            persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate);

        return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
    }

    @Transactional(readOnly = true)
    public Optional<AuditEvent> find(Long id) {
        return Optional.ofNullable(persistenceAuditEventRepository.findOne(id)).map
            (new Function<PersistentAuditEvent, AuditEvent>() {
                @Override
                public AuditEvent apply(PersistentAuditEvent persistentAuditEvent) {
                    return auditEventConverter.convertToAuditEvent(persistentAuditEvent);
                }
            });
    }
}
