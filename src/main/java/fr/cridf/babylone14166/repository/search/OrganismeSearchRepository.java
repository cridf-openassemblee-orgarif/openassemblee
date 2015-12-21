package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.Organisme;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Organisme entity.
 */
public interface OrganismeSearchRepository extends ElasticsearchRepository<Organisme, Long> {
}
