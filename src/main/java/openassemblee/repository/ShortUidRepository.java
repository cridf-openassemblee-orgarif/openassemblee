package openassemblee.repository;

import java.util.UUID;
import openassemblee.domain.ShortUid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ShortUid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShortUidRepository extends JpaRepository<ShortUid, UUID> {
    ShortUid findOneByUid(String uid);

    ShortUid findOneByShortUid(Long shortUid);
}
