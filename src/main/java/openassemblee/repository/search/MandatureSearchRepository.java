package openassemblee.repository.search;

import openassemblee.domain.Mandature;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Mandature entity.
 */
public interface MandatureSearchRepository extends ElasticsearchRepository<Mandature, Long> {
}
