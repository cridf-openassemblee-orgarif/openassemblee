package openassemblee.repository;

import java.util.List;
import openassemblee.domain.AppartenanceGroupePolitique;
import openassemblee.domain.GroupePolitique;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AppartenanceGroupePolitique entity.
 */
public interface AppartenanceGroupePolitiqueRepository
    extends JpaRepository<AppartenanceGroupePolitique, Long> {
    List<AppartenanceGroupePolitique> findAllByGroupePolitique(
        GroupePolitique groupePolitique
    );
}
