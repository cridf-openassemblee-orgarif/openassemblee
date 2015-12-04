package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.GroupePolitique;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GroupePolitique entity.
 */
public interface GroupePolitiqueRepository extends JpaRepository<GroupePolitique,Long> {

}
