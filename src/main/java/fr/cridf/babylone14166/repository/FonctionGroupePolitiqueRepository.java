package fr.cridf.babylone14166.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cridf.babylone14166.domain.FonctionGroupePolitique;
import fr.cridf.babylone14166.domain.GroupePolitique;

/**
 * Spring Data JPA repository for the FonctionGroupePolitique entity.
 */
public interface FonctionGroupePolitiqueRepository extends JpaRepository<FonctionGroupePolitique,Long> {

    List<FonctionGroupePolitique> findAllByGroupePolitique(GroupePolitique groupePolitique);

}
