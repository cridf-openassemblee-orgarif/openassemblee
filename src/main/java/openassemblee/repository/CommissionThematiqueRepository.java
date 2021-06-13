package openassemblee.repository;

import openassemblee.domain.AppartenanceCommissionPermanente;
import openassemblee.domain.CommissionThematique;

import openassemblee.domain.GroupePolitique;
import openassemblee.domain.Mandature;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CommissionThematique entity.
 */
public interface CommissionThematiqueRepository extends JpaRepository<CommissionThematique,Long> {

    @Deprecated
    @Override
    List<CommissionThematique> findAll();

    @Deprecated
    @Override
    List<CommissionThematique> findAll(Sort var1);

    @Deprecated
    @Override
    List<CommissionThematique> findAll(Iterable<Long> var1);

    List<CommissionThematique> findByMandature(Mandature mandature);

}
