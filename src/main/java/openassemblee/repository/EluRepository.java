package openassemblee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import openassemblee.domain.Elu;

/**
 * Spring Data JPA repository for the Elu entity.
 */
public interface EluRepository extends JpaRepository<Elu,Long> {

}
