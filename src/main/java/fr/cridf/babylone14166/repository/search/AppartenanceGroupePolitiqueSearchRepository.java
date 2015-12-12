package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.AppartenanceGroupePolitique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AppartenanceGroupePolitique entity.
 */
public interface AppartenanceGroupePolitiqueSearchRepository extends ElasticsearchRepository<AppartenanceGroupePolitique, Long> {
}
