package openassemblee.repository;

import openassemblee.domain.AppartenanceCommissionThematique;
import openassemblee.domain.CommissionThematique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the AppartenanceCommissionThematique entity.
 */
public interface AppartenanceCommissionThematiqueRepository extends JpaRepository<AppartenanceCommissionThematique,Long> {

    List<AppartenanceCommissionThematique> findAllByCommissionThematique(CommissionThematique commissionThematique);

}
