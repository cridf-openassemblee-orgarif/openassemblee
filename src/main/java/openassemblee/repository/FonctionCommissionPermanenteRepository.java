package openassemblee.repository;

import java.util.List;
import openassemblee.domain.FonctionCommissionPermanente;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the FonctionCommissionPermanente entity.
 */
public interface FonctionCommissionPermanenteRepository
    extends JpaRepository<FonctionCommissionPermanente, Long> {
    List<FonctionCommissionPermanente> findByMandature(Mandature mandature);
}
