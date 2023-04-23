package openassemblee.repository;

import java.util.List;
import openassemblee.domain.AppartenanceCommissionPermanente;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AppartenanceCommissionPermanente entity.
 */
public interface AppartenanceCommissionPermanenteRepository
    extends JpaRepository<AppartenanceCommissionPermanente, Long> {
    List<AppartenanceCommissionPermanente> findByMandature(Mandature mandature);
}
