package openassemblee.repository;

import java.util.List;
import openassemblee.domain.AppartenanceCommissionThematique;
import openassemblee.domain.CommissionThematique;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AppartenanceCommissionThematique entity.
 */
public interface AppartenanceCommissionThematiqueRepository
    extends JpaRepository<AppartenanceCommissionThematique, Long> {
    List<AppartenanceCommissionThematique> findAllByCommissionThematique(
        CommissionThematique commissionThematique
    );
}
