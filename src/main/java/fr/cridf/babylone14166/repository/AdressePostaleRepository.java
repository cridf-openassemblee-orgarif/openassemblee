package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.AdressePostale;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AdressePostale entity.
 */
public interface AdressePostaleRepository extends JpaRepository<AdressePostale,Long> {

}
