package openassemblee.repository;

import openassemblee.domain.CommissionThematique;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the CommissionThematique entity.
 */
public interface CommissionThematiqueRepository extends JpaRepository<CommissionThematique,Long> {

}
