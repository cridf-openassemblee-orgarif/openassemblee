package openassemblee.repository;

import openassemblee.domain.CommissionThematique;
import openassemblee.domain.Mandature;
import openassemblee.domain.ReunionCao;
import openassemblee.domain.Seance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Seance entity.
 */
public interface SeanceRepository extends JpaRepository<Seance,Long> {

    @Deprecated
    @Override
    List<Seance> findAll();

    @Deprecated
    @Override
    List<Seance> findAll(Sort var1);

    @Deprecated
    @Override
    List<Seance> findAll(Iterable<Long> var1);

    Page<Seance> findByMandature(Mandature mandature, Pageable var1);

}
