package openassemblee.repository;

import openassemblee.domain.Seance;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Seance entity.
 */
public interface SeanceRepository extends JpaRepository<Seance,Long> {

}
