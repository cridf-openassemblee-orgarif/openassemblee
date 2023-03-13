package openassemblee.repository;

import openassemblee.domain.Elu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;

/**
 * Spring Data JPA repository for the Elu entity.
 */
public interface EluRepository extends JpaRepository<Elu, Long> {

//    Elu findOneByImportUid(String importUid);
//
//    Elu findOneByShortUid(Long uuid);

    Elu findOneByUid(String uuid) throws SQLException;

}
