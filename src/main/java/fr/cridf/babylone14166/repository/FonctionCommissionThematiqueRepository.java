package fr.cridf.babylone14166.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cridf.babylone14166.domain.CommissionThematique;
import fr.cridf.babylone14166.domain.FonctionCommissionThematique;

/**
 * Spring Data JPA repository for the FonctionCommissionThematique entity.
 */
public interface FonctionCommissionThematiqueRepository extends JpaRepository<FonctionCommissionThematique,Long> {

    List<FonctionCommissionThematique> findAllByCommissionThematique(CommissionThematique commissionThematique);

}
