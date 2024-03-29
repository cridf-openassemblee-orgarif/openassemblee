package openassemblee.repository.search;

import openassemblee.domain.AppartenanceCommissionPermanente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AppartenanceCommissionPermanente entity.
 */
public interface AppartenanceCommissionPermanenteSearchRepository
    extends ElasticsearchRepository<AppartenanceCommissionPermanente, Long> {}
