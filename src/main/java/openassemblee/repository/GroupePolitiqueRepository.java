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

    List<GroupePolitique> findByMandature(Mandature mandature);

}
