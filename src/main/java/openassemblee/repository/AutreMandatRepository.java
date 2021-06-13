package openassemblee.repository;

import openassemblee.domain.AutreMandat;

import openassemblee.domain.CommissionThematique;
import openassemblee.domain.Mandature;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AutreMandat entity.
 */
public interface AutreMandatRepository extends JpaRepository<AutreMandat,Long> {

    @Deprecated
    @Override
    List<AutreMandat> findAll();

    @Deprecated
    @Override
    List<AutreMandat> findAll(Sort var1);

    @Deprecated
    @Override
    List<AutreMandat> findAll(Iterable<Long> var1);

    List<AutreMandat> findByMandature(Mandature mandature);

}
