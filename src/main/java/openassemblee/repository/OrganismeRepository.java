package openassemblee.repository;

import openassemblee.domain.Organisme;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Organisme entity.
 */
public interface OrganismeRepository extends JpaRepository<Organisme, Long> {

    Organisme findFirstByCodeRNE(String codeRNE);

}
