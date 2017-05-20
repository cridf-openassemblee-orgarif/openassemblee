package openassemblee.repository.search;

import openassemblee.domain.NumeroFax;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NumeroFax entity.
 */
public interface NumeroFaxSearchRepository extends ElasticsearchRepository<NumeroFax, Long> {
}
