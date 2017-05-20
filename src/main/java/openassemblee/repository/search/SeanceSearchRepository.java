package openassemblee.repository.search;

import openassemblee.domain.Seance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Seance entity.
 */
public interface SeanceSearchRepository extends ElasticsearchRepository<Seance, Long> {
}
