package openassemblee.repository;

import openassemblee.domain.CommissionThematique;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the CommissionThematique entity.
 */
public interface CommissionThematiqueRepository extends JpaRepository<CommissionThematique,Long> {

    List<CommissionThematique> findByMandature(Mandature mandature);

}
