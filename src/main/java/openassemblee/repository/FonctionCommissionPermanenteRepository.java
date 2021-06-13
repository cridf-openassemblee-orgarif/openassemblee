package openassemblee.repository;

import openassemblee.domain.CommissionThematique;
import openassemblee.domain.FonctionCommissionPermanente;

import openassemblee.domain.Mandature;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FonctionCommissionPermanente entity.
 */
public interface FonctionCommissionPermanenteRepository extends JpaRepository<FonctionCommissionPermanente,Long> {

    @Deprecated
    @Override
    List<FonctionCommissionPermanente> findAll();

    @Deprecated
    @Override
    List<FonctionCommissionPermanente> findAll(Sort var1);

    @Deprecated
    @Override
    List<FonctionCommissionPermanente> findAll(Iterable<Long> var1);

    List<FonctionCommissionPermanente> findByMandature(Mandature mandature);

}
