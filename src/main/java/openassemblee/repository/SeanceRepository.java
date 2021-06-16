package openassemblee.repository;

import openassemblee.domain.Mandature;
import openassemblee.domain.Seance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Seance entity.
 */
public interface SeanceRepository extends JpaRepository<Seance,Long> {

    Page<Seance> findByMandature(Mandature mandature, Pageable var1);

}
