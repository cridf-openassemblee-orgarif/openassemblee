package openassemblee.repository;

import openassemblee.domain.AppartenanceGroupePolitique;
import openassemblee.domain.GroupePolitique;
import openassemblee.domain.Mandature;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Mandature entity.
 */
public interface MandatureRepository extends JpaRepository<Mandature,Long> {

    Mandature findOneByCurrent(Boolean current);

}
