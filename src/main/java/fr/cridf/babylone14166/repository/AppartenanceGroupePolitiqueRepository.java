package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.AppartenanceGroupePolitique;
import fr.cridf.babylone14166.domain.GroupePolitique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the AppartenanceGroupePolitique entity.
 */
public interface AppartenanceGroupePolitiqueRepository extends JpaRepository<AppartenanceGroupePolitique,Long> {

    List<AppartenanceGroupePolitique> findAllByGroupePolitique(GroupePolitique groupePolitique);

}
