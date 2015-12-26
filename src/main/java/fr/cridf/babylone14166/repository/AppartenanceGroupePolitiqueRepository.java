package fr.cridf.babylone14166.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cridf.babylone14166.domain.*;

/**
 * Spring Data JPA repository for the AppartenanceGroupePolitique entity.
 */
public interface AppartenanceGroupePolitiqueRepository extends JpaRepository<AppartenanceGroupePolitique,Long> {

    List<AppartenanceGroupePolitique> findAllByElu(Elu elu);

    List<AppartenanceGroupePolitique> findAllByGroupePolitique(GroupePolitique groupePolitique);

}
