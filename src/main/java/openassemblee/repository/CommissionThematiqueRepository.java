package openassemblee.repository;

import java.util.List;
import openassemblee.domain.CommissionThematique;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the CommissionThematique entity.
 */
public interface CommissionThematiqueRepository
    extends JpaRepository<CommissionThematique, Long> {
    List<CommissionThematique> findByMandature(Mandature mandature);
}
