package openassemblee.repository;

import openassemblee.domain.AutreMandat;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the AutreMandat entity.
 */
public interface AutreMandatRepository extends JpaRepository<AutreMandat,Long> {

}
