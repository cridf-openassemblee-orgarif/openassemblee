package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.Organisme;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Organisme entity.
 */
public interface OrganismeRepository extends JpaRepository<Organisme,Long> {

}
