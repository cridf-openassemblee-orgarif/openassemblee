package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.Signature;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Signature entity.
 */
public interface SignatureRepository extends JpaRepository<Signature,Long> {

}
