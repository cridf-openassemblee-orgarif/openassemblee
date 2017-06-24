package openassemblee.repository;

import openassemblee.domain.AppartenanceOrganisme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the AppartenanceOrganisme entity.
 */
public interface AppartenanceOrganismeRepository extends JpaRepository<AppartenanceOrganisme, Long> {

    // TODO mais du coup quand on avait pas le code RNE ? Quel est l'usage de ce truc ?
    List<AppartenanceOrganisme> findAllByCodeRNE(String codeRNE);

}
