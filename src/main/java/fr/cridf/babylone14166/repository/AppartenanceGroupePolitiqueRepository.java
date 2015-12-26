package fr.cridf.babylone14166.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cridf.babylone14166.domain.AppartenanceGroupePolitique;
import fr.cridf.babylone14166.domain.GroupePolitique;

/**
 * Spring Data JPA repository for the AppartenanceGroupePolitique entity.
 */
public interface AppartenanceGroupePolitiqueRepository extends JpaRepository<AppartenanceGroupePolitique,Long> {

    List<AppartenanceGroupePolitique> findAllByGroupePolitique(GroupePolitique groupePolitique);

}
