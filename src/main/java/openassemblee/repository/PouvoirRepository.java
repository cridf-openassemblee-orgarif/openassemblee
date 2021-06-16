package openassemblee.repository;

import openassemblee.domain.Pouvoir;
import openassemblee.domain.Seance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Pouvoir entity.
 */
public interface PouvoirRepository extends JpaRepository<Pouvoir, Long> {

    List<Pouvoir> findAllBySeance(Seance seance);

    List<Pouvoir> findAllBySeanceAndDateFinAndHeureFin(Seance seance, LocalDate dateFin, String heureFin);
}
