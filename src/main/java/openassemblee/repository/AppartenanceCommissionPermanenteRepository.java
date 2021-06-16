package openassemblee.repository;

import openassemblee.domain.AppartenanceCommissionPermanente;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the AppartenanceCommissionPermanente entity.
 */
public interface AppartenanceCommissionPermanenteRepository extends JpaRepository<AppartenanceCommissionPermanente,Long> {

    List<AppartenanceCommissionPermanente> findByMandature(Mandature mandature);

}
