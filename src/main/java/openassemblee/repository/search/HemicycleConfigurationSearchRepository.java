package openassemblee.repository.search;

import openassemblee.domain.HemicycleConfiguration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the HemicycleConfiguration entity.
 */
public interface HemicycleConfigurationSearchRepository
    extends ElasticsearchRepository<HemicycleConfiguration, Long> {}
