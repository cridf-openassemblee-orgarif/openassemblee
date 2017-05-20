package openassemblee.repository.search;

import openassemblee.domain.AdressePostale;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AdressePostale entity.
 */
public interface AdressePostaleSearchRepository extends ElasticsearchRepository<AdressePostale, Long> {
}
