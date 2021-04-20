package openassemblee.repository;

import openassemblee.domain.Mandature;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Mandature entity.
 */
public interface MandatureRepository extends JpaRepository<Mandature,Long> {

}
