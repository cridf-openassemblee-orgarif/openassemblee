package openassemblee.repository.search;

import openassemblee.domain.Pouvoir;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Pouvoir entity.
 */
public interface PouvoirSearchRepository extends ElasticsearchRepository<Pouvoir, Long> {
}
