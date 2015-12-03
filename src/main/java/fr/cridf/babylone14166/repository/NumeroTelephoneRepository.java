package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.NumeroTelephone;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NumeroTelephone entity.
 */
public interface NumeroTelephoneRepository extends JpaRepository<NumeroTelephone,Long> {

}
