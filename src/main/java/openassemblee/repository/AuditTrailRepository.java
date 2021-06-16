package openassemblee.repository;

import openassemblee.domain.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AuditTrail entity.
 */
public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {

}
