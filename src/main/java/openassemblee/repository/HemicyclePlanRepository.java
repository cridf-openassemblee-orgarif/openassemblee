package openassemblee.repository;

import openassemblee.domain.HemicyclePlan;
import openassemblee.domain.Mandature;
import openassemblee.domain.Seance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the HemicyclePlan entity.
 */
public interface HemicyclePlanRepository extends JpaRepository<HemicyclePlan, Long> {

    List<HemicyclePlan> findByMandature(Mandature mandature);

    HemicyclePlan findOneBySeance(Seance seance);

}
