package fr.cridf.babylone14166.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cridf.babylone14166.domain.AppartenanceCommissionThematique;
import fr.cridf.babylone14166.domain.CommissionThematique;

/**
 * Spring Data JPA repository for the AppartenanceCommissionThematique entity.
 */
public interface AppartenanceCommissionThematiqueRepository extends JpaRepository<AppartenanceCommissionThematique,Long> {

    List<AppartenanceCommissionThematique> findAllByCommissionThematique(CommissionThematique commissionThematique);

}
