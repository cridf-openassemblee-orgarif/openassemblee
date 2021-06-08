package openassemblee.repository;

import openassemblee.domain.ListeElectorale;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ListeElectorale entity.
 */
public interface ListeElectoraleRepository extends JpaRepository<ListeElectorale,Long> {

}
