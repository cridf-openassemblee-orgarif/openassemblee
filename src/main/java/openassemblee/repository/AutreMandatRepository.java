package openassemblee.repository;

import openassemblee.domain.AutreMandat;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the AutreMandat entity.
 */
public interface AutreMandatRepository extends JpaRepository<AutreMandat, Long> {

    List<AutreMandat> findByMandature(Mandature mandature);

}
