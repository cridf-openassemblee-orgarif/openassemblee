package openassemblee.repository;

import openassemblee.domain.Signature;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Signature entity.
 */
public interface SignatureRepository extends JpaRepository<Signature,Long> {

}
