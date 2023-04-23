package openassemblee.repository;

import java.util.List;
import openassemblee.domain.AppartenanceOrganisme;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AppartenanceOrganisme entity.
 */
public interface AppartenanceOrganismeRepository
    extends JpaRepository<AppartenanceOrganisme, Long> {
    List<AppartenanceOrganisme> findAllByOrganisme(String organismeNom);

    // TODO mais du coup quand on avait pas le code RNE ? Quel est l'usage de ce truc ?
    List<AppartenanceOrganisme> findAllByCodeRNE(String codeRNE);
}
