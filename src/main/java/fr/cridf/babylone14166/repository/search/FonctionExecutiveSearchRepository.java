package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.FonctionExecutive;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FonctionExecutive entity.
 */
public interface FonctionExecutiveSearchRepository extends ElasticsearchRepository<FonctionExecutive, Long> {
}
