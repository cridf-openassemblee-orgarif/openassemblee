package openassemblee.repository;

import openassemblee.domain.*;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HemicyclePlan entity.
 */
public interface HemicyclePlanRepository extends JpaRepository<HemicyclePlan,Long> {

    List<HemicyclePlan> findByMandature(Mandature mandature);

    HemicyclePlan findOneBySeance(Seance seance);

}
