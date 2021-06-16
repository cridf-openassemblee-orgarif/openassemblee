package openassemblee.repository;

import openassemblee.domain.CommissionThematique;
import openassemblee.domain.FonctionCommissionThematique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the FonctionCommissionThematique entity.
 */
public interface FonctionCommissionThematiqueRepository extends JpaRepository<FonctionCommissionThematique, Long> {

    List<FonctionCommissionThematique> findAllByCommissionThematique(CommissionThematique commissionThematique);

}
