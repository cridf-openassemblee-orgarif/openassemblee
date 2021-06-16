package openassemblee.repository;

import openassemblee.domain.CommissionThematique;
import openassemblee.domain.Mandature;
import openassemblee.domain.ReunionCommissionThematique;

import openassemblee.domain.Seance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReunionCommissionThematique entity.
 */
public interface ReunionCommissionThematiqueRepository extends JpaRepository<ReunionCommissionThematique,Long> {

    Page<ReunionCommissionThematique> findByMandature(Mandature mandature, Pageable var1);

}
