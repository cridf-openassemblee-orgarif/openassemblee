package openassemblee.repository;

import openassemblee.domain.Mandat;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Mandat entity.
 */
public interface MandatRepository extends JpaRepository<Mandat, Long> {}
