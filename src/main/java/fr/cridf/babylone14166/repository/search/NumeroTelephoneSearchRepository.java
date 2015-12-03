package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.NumeroTelephone;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NumeroTelephone entity.
 */
public interface NumeroTelephoneSearchRepository extends ElasticsearchRepository<NumeroTelephone, Long> {
}
