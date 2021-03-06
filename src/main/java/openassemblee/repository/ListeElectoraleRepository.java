package openassemblee.repository;

import openassemblee.domain.ListeElectorale;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the ListeElectorale entity.
 */
public interface ListeElectoraleRepository extends JpaRepository<ListeElectorale, Long> {

    List<ListeElectorale> findByMandature(Mandature mandature);

}
