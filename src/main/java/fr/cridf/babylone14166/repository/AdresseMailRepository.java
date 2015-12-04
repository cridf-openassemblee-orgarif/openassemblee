package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.AdresseMail;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AdresseMail entity.
 */
public interface AdresseMailRepository extends JpaRepository<AdresseMail,Long> {

}
