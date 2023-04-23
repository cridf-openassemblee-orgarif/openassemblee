package openassemblee.repository;

import java.util.List;
import openassemblee.domain.FonctionGroupePolitique;
import openassemblee.domain.GroupePolitique;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the FonctionGroupePolitique entity.
 */
public interface FonctionGroupePolitiqueRepository
    extends JpaRepository<FonctionGroupePolitique, Long> {
    List<FonctionGroupePolitique> findAllByGroupePolitique(
        GroupePolitique groupePolitique
    );
}
