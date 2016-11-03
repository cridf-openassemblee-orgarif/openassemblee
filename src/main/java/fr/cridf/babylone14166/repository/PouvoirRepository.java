package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.Pouvoir;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Pouvoir entity.
 */
public interface PouvoirRepository extends JpaRepository<Pouvoir,Long> {

    List<Pouvoir> findAllByDateDebut(LocalDate dateDebut);

}
