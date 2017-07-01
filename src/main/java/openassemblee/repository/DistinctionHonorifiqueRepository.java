package openassemblee.repository;

import openassemblee.domain.DistinctionHonorifique;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DistinctionHonorifique entity.
 */
public interface DistinctionHonorifiqueRepository extends JpaRepository<DistinctionHonorifique,Long> {

}
