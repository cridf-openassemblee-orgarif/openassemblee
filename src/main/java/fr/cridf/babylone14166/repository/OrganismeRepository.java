package fr.cridf.babylone14166.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cridf.babylone14166.domain.Organisme;

/**
 * Spring Data JPA repository for the Organisme entity.
 */
public interface OrganismeRepository extends JpaRepository<Organisme,Long> {

    Organisme findOneByCodeRNE(String codeRNE);

}
