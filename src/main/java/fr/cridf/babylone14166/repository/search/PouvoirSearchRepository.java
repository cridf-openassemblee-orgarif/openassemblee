package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.Pouvoir;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Pouvoir entity.
 */
public interface PouvoirSearchRepository extends ElasticsearchRepository<Pouvoir, Long> {
}
