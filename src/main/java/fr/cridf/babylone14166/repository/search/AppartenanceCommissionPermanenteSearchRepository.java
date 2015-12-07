package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.AppartenanceCommissionPermanente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AppartenanceCommissionPermanente entity.
 */
public interface AppartenanceCommissionPermanenteSearchRepository extends ElasticsearchRepository<AppartenanceCommissionPermanente, Long> {
}
