package openassemblee.repository;

import java.util.List;
import openassemblee.domain.AutreMandat;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AutreMandat entity.
 */
public interface AutreMandatRepository
    extends JpaRepository<AutreMandat, Long> {
    List<AutreMandat> findByMandature(Mandature mandature);
}
