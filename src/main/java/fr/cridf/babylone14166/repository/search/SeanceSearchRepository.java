package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.Seance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Seance entity.
 */
public interface SeanceSearchRepository extends ElasticsearchRepository<Seance, Long> {
}
