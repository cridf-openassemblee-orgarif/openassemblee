package openassemblee.repository;

import openassemblee.domain.FonctionCommissionPermanente;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the FonctionCommissionPermanente entity.
 */
public interface FonctionCommissionPermanenteRepository extends JpaRepository<FonctionCommissionPermanente,Long> {

    List<FonctionCommissionPermanente> findByMandature(Mandature mandature);

}
