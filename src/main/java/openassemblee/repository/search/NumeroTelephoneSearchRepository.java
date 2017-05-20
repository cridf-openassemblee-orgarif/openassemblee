package openassemblee.repository.search;

import openassemblee.domain.NumeroTelephone;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NumeroTelephone entity.
 */
public interface NumeroTelephoneSearchRepository extends ElasticsearchRepository<NumeroTelephone, Long> {
}
