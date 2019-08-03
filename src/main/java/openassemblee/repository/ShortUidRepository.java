package openassemblee.repository;

import openassemblee.domain.ShortUid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the ShortUid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShortUidRepository extends JpaRepository<ShortUid, UUID> {

    ShortUid findOneByShortUid(String shortUid);

}
