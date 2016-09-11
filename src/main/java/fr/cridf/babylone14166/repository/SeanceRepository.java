package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.Seance;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Seance entity.
 */
public interface SeanceRepository extends JpaRepository<Seance,Long> {

}
