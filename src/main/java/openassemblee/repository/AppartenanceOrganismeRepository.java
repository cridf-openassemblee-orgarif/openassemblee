package openassemblee.repository;

import openassemblee.domain.AppartenanceOrganisme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the AppartenanceOrganisme entity.
 */
public interface AppartenanceOrganismeRepository extends JpaRepository<AppartenanceOrganisme, Long> {

    List<AppartenanceOrganisme> findAllByCodeRNE(String codeRNE);

}
