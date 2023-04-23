package openassemblee.repository;

import java.util.List;
import openassemblee.domain.HemicyclePlan;
import openassemblee.domain.Mandature;
import openassemblee.domain.Seance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the HemicyclePlan entity.
 */
public interface HemicyclePlanRepository
    extends JpaRepository<HemicyclePlan, Long> {
    List<HemicyclePlan> findByMandature(Mandature mandature);

    HemicyclePlan findOneBySeance(Seance seance);
}
