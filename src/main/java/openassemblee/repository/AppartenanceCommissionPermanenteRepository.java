package openassemblee.repository;

import openassemblee.domain.AppartenanceCommissionPermanente;

import openassemblee.domain.Mandature;
import openassemblee.domain.Seance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AppartenanceCommissionPermanente entity.
 */
public interface AppartenanceCommissionPermanenteRepository extends JpaRepository<AppartenanceCommissionPermanente,Long> {

    @Deprecated
    @Override
    List<AppartenanceCommissionPermanente> findAll();

    @Deprecated
    @Override
    List<AppartenanceCommissionPermanente> findAll(Sort var1);

    @Deprecated
    @Override
    List<AppartenanceCommissionPermanente> findAll(Iterable<Long> var1);

    List<AppartenanceCommissionPermanente> findByMandature(Mandature mandature);

}
