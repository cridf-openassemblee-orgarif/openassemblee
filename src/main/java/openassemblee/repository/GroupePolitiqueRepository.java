package openassemblee.repository;

import openassemblee.domain.GroupePolitique;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the GroupePolitique entity.
 */
public interface GroupePolitiqueRepository extends JpaRepository<GroupePolitique,Long> {

}
