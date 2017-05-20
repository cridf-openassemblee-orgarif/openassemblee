package openassemblee.repository.search;

import openassemblee.domain.PresenceElu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PresenceElu entity.
 */
public interface PresenceEluSearchRepository extends ElasticsearchRepository<PresenceElu, Long> {
}
