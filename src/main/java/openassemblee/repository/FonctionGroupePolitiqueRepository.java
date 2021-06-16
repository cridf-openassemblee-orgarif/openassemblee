package openassemblee.repository;

import openassemblee.domain.FonctionGroupePolitique;
import openassemblee.domain.GroupePolitique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the FonctionGroupePolitique entity.
 */
public interface FonctionGroupePolitiqueRepository extends JpaRepository<FonctionGroupePolitique, Long> {

    List<FonctionGroupePolitique> findAllByGroupePolitique(GroupePolitique groupePolitique);

}
