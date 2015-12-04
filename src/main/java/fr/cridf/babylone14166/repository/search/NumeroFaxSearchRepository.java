package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.NumeroFax;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NumeroFax entity.
 */
public interface NumeroFaxSearchRepository extends ElasticsearchRepository<NumeroFax, Long> {
}
