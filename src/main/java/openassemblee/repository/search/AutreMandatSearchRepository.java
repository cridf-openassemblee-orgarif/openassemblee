package openassemblee.repository.search;

import openassemblee.domain.AutreMandat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AutreMandat entity.
 */
public interface AutreMandatSearchRepository extends ElasticsearchRepository<AutreMandat, Long> {
}
