package openassemblee.repository.search;

import openassemblee.domain.ReunionCao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ReunionCao entity.
 */
public interface ReunionCaoSearchRepository extends ElasticsearchRepository<ReunionCao, Long> {
}
