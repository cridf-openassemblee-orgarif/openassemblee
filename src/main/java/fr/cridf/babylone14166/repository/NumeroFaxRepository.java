package fr.cridf.babylone14166.repository;

import fr.cridf.babylone14166.domain.NumeroFax;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NumeroFax entity.
 */
public interface NumeroFaxRepository extends JpaRepository<NumeroFax,Long> {

}
