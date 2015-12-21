package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.AppartenanceOrganisme;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AppartenanceOrganisme entity.
 */
public interface AppartenanceOrganismeSearchRepository extends ElasticsearchRepository<AppartenanceOrganisme, Long> {
}
