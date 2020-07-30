package openassemblee.repository;

import openassemblee.domain.HemicyclePlan;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HemicyclePlan entity.
 */
public interface HemicyclePlanRepository extends JpaRepository<HemicyclePlan,Long> {

}
