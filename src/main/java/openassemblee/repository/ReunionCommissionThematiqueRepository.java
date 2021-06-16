package openassemblee.repository;

import openassemblee.domain.Mandature;
import openassemblee.domain.ReunionCommissionThematique;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the ReunionCommissionThematique entity.
 */
public interface ReunionCommissionThematiqueRepository extends JpaRepository<ReunionCommissionThematique,Long> {

    Page<ReunionCommissionThematique> findByMandature(Mandature mandature, Pageable var1);

}
