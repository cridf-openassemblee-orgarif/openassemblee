package openassemblee.repository;

import openassemblee.domain.CommissionThematique;
import openassemblee.domain.Mandature;
import openassemblee.domain.ReunionCao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReunionCao entity.
 */
public interface ReunionCaoRepository extends JpaRepository<ReunionCao,Long> {

    Page<ReunionCao> findByMandature(Mandature mandature, Pageable var1);

}
