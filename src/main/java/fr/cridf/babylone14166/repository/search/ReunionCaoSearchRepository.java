package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.ReunionCao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ReunionCao entity.
 */
public interface ReunionCaoSearchRepository extends ElasticsearchRepository<ReunionCao, Long> {
}
