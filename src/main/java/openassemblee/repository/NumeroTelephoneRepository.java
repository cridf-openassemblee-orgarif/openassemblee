package openassemblee.repository;

import openassemblee.domain.NumeroTelephone;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the NumeroTelephone entity.
 */
public interface NumeroTelephoneRepository extends JpaRepository<NumeroTelephone,Long> {

}
