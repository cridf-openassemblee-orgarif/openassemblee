package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.FonctionGroupePolitique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FonctionGroupePolitique entity.
 */
public interface FonctionGroupePolitiqueSearchRepository extends ElasticsearchRepository<FonctionGroupePolitique, Long> {
}
