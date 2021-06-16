package openassemblee.repository;

import openassemblee.domain.AdresseMail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AdresseMail entity.
 */
public interface AdresseMailRepository extends JpaRepository<AdresseMail,Long> {

}
