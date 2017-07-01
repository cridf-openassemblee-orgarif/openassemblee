package openassemblee.repository.search;

import openassemblee.domain.DistinctionHonorifique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DistinctionHonorifique entity.
 */
public interface DistinctionHonorifiqueSearchRepository extends ElasticsearchRepository<DistinctionHonorifique, Long> {
}
