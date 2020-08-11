package openassemblee.repository;

import openassemblee.domain.AppartenanceOrganisme;
import openassemblee.domain.HemicyclePlan;

import openassemblee.domain.Seance;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HemicyclePlan entity.
 */
public interface HemicyclePlanRepository extends JpaRepository<HemicyclePlan,Long> {

    HemicyclePlan findOneBySeance(Seance seance);

}
