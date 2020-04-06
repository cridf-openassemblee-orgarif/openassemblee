package openassemblee.repository;

import openassemblee.domain.HemicycleConfiguration;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HemicycleConfiguration entity.
 */
public interface HemicycleConfigurationRepository extends JpaRepository<HemicycleConfiguration,Long> {

}
