package openassemblee.repository;

import java.util.List;

import openassemblee.domain.FonctionGroupePolitique;
import org.springframework.data.jpa.repository.JpaRepository;

import openassemblee.domain.GroupePolitique;

/**
 * Spring Data JPA repository for the FonctionGroupePolitique entity.
 */
public interface FonctionGroupePolitiqueRepository extends JpaRepository<FonctionGroupePolitique,Long> {

    List<FonctionGroupePolitique> findAllByGroupePolitique(GroupePolitique groupePolitique);

}
