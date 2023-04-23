package openassemblee.repository;

import java.sql.SQLException;
import openassemblee.domain.Elu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Elu entity.
 */
public interface EluRepository extends JpaRepository<Elu, Long> {
    //    Elu findOneByImportUid(String importUid);
    //
    //    Elu findOneByShortUid(Long uuid);

    Elu findOneByUid(String uuid) throws SQLException;
}
