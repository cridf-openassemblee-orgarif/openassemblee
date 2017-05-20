package openassemblee.repository.search;

import openassemblee.domain.Organisme;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Organisme entity.
 */
public interface OrganismeSearchRepository extends ElasticsearchRepository<Organisme, Long> {
}
