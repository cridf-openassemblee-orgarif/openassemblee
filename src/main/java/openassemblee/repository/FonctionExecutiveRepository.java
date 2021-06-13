package openassemblee.repository;

import openassemblee.domain.CommissionThematique;
import openassemblee.domain.FonctionExecutive;

import openassemblee.domain.Mandature;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FonctionExecutive entity.
 */
public interface FonctionExecutiveRepository extends JpaRepository<FonctionExecutive,Long> {

    @Deprecated
    @Override
    List<FonctionExecutive> findAll();

    @Deprecated
    @Override
    List<FonctionExecutive> findAll(Sort var1);

    @Deprecated
    @Override
    List<FonctionExecutive> findAll(Iterable<Long> var1);

    List<FonctionExecutive> findByMandature(Mandature mandature);

}
