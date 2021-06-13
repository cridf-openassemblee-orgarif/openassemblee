package openassemblee.repository;

import openassemblee.domain.CommissionThematique;
import openassemblee.domain.GroupePolitique;

import openassemblee.domain.Mandature;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GroupePolitique entity.
 */
public interface GroupePolitiqueRepository extends JpaRepository<GroupePolitique, Long> {

    @Deprecated
    @Override
    List<GroupePolitique> findAll();

    @Deprecated
    @Override
    List<GroupePolitique> findAll(Sort var1);

    @Deprecated
    @Override
    List<GroupePolitique> findAll(Iterable<Long> var1);

    List<GroupePolitique> findByMandature(Mandature mandature);

}
