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

    List<FonctionExecutive> findByMandature(Mandature mandature);

}
