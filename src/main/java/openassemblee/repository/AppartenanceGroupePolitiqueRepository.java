package openassemblee.repository;

import openassemblee.domain.AppartenanceGroupePolitique;
import openassemblee.domain.GroupePolitique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the AppartenanceGroupePolitique entity.
 */
public interface AppartenanceGroupePolitiqueRepository extends JpaRepository<AppartenanceGroupePolitique, Long> {

    List<AppartenanceGroupePolitique> findAllByGroupePolitique(GroupePolitique groupePolitique);

}
