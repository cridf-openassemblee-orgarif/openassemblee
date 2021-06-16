package openassemblee.repository;

import openassemblee.domain.GroupePolitique;
import openassemblee.domain.Mandature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the GroupePolitique entity.
 */
public interface GroupePolitiqueRepository extends JpaRepository<GroupePolitique, Long> {

    List<GroupePolitique> findByMandature(Mandature mandature);

}
