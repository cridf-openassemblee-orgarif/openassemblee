package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.AdressePostale;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AdressePostale entity.
 */
public interface AdressePostaleSearchRepository extends ElasticsearchRepository<AdressePostale, Long> {
}
