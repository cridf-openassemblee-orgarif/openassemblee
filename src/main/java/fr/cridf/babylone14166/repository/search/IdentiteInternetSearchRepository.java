package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.IdentiteInternet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the IdentiteInternet entity.
 */
public interface IdentiteInternetSearchRepository extends ElasticsearchRepository<IdentiteInternet, Long> {
}
