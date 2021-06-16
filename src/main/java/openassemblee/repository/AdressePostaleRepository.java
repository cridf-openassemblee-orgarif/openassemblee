package openassemblee.repository;

import openassemblee.domain.AdressePostale;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AdressePostale entity.
 */
public interface AdressePostaleRepository extends JpaRepository<AdressePostale,Long> {

}
