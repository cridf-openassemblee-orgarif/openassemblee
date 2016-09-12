package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.AuditTrail;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AuditTrail entity.
 */
public interface AuditTrailRepository extends JpaRepository<AuditTrail,Long> {

}
