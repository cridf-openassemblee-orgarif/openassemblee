package openassemblee.repository;

import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Mandature entity.
 */
public interface MandatureRepository extends JpaRepository<Mandature, Long> {
    Mandature findOneByCurrent(Boolean current);
}
