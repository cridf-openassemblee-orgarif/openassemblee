package openassemblee.repository.search;

import openassemblee.domain.AppartenanceGroupePolitique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AppartenanceGroupePolitique entity.
 */
public interface AppartenanceGroupePolitiqueSearchRepository
    extends ElasticsearchRepository<AppartenanceGroupePolitique, Long> {}
