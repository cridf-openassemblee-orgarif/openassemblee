package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.PresenceElu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PresenceElu entity.
 */
public interface PresenceEluSearchRepository extends ElasticsearchRepository<PresenceElu, Long> {
}
