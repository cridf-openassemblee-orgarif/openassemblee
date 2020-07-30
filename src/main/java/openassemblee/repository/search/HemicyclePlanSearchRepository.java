package openassemblee.repository.search;

import openassemblee.domain.HemicyclePlan;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the HemicyclePlan entity.
 */
public interface HemicyclePlanSearchRepository extends ElasticsearchRepository<HemicyclePlan, Long> {
}
