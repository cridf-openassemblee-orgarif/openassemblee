package openassemblee.repository;

import java.util.List;
import openassemblee.domain.FonctionExecutive;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the FonctionExecutive entity.
 */
public interface FonctionExecutiveRepository
    extends JpaRepository<FonctionExecutive, Long> {
    List<FonctionExecutive> findByMandature(Mandature mandature);
}
