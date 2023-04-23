package openassemblee.repository;

import java.time.LocalDate;
import java.util.List;
import openassemblee.domain.Pouvoir;
import openassemblee.domain.Seance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Pouvoir entity.
 */
public interface PouvoirRepository extends JpaRepository<Pouvoir, Long> {
    List<Pouvoir> findAllBySeance(Seance seance);

    List<Pouvoir> findAllBySeanceAndDateFinAndHeureFin(
        Seance seance,
        LocalDate dateFin,
        String heureFin
    );
}
