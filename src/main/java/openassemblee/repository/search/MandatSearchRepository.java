package openassemblee.repository.search;

import openassemblee.domain.Mandat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Mandat entity.
 */
public interface MandatSearchRepository extends ElasticsearchRepository<Mandat, Long> {
}
