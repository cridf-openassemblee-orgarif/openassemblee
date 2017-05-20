package openassemblee.repository;

import openassemblee.domain.NumeroFax;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the NumeroFax entity.
 */
public interface NumeroFaxRepository extends JpaRepository<NumeroFax,Long> {

}
