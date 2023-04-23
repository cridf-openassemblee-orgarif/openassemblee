package openassemblee.repository;

import openassemblee.domain.HemicycleArchive;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the HemicycleArchive entity.
 */
public interface HemicycleArchiveRepository
    extends JpaRepository<HemicycleArchive, Long> {}
