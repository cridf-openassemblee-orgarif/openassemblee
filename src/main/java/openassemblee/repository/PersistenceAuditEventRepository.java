package openassemblee.repository;

import java.time.LocalDateTime;
import java.util.List;
import openassemblee.domain.PersistentAuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the PersistentAuditEvent entity.
 */
public interface PersistenceAuditEventRepository
    extends JpaRepository<PersistentAuditEvent, Long> {
    List<PersistentAuditEvent> findByPrincipal(String principal);

    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(
        String principal,
        LocalDateTime after
    );

    List<PersistentAuditEvent> findAllByAuditEventDateBetween(
        LocalDateTime fromDate,
        LocalDateTime toDate
    );
}
