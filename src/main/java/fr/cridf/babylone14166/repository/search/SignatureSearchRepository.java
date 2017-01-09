package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.Signature;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Signature entity.
 */
public interface SignatureSearchRepository extends ElasticsearchRepository<Signature, Long> {
}
