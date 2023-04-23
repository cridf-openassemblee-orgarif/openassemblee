package openassemblee.repository;

import java.util.List;
import openassemblee.domain.ListeElectorale;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the ListeElectorale entity.
 */
public interface ListeElectoraleRepository
    extends JpaRepository<ListeElectorale, Long> {
    List<ListeElectorale> findByMandature(Mandature mandature);
}
