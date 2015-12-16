package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.CommissionThematique;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CommissionThematique entity.
 */
public interface CommissionThematiqueRepository extends JpaRepository<CommissionThematique,Long> {

}
