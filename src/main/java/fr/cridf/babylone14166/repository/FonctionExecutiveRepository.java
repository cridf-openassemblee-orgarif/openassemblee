package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.FonctionExecutive;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FonctionExecutive entity.
 */
public interface FonctionExecutiveRepository extends JpaRepository<FonctionExecutive,Long> {

}
