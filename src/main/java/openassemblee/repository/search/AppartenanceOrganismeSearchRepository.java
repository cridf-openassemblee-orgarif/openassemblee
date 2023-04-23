package openassemblee.repository.search;

import openassemblee.domain.AppartenanceOrganisme;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AppartenanceOrganisme entity.
 */
public interface AppartenanceOrganismeSearchRepository
    extends ElasticsearchRepository<AppartenanceOrganisme, Long> {}
