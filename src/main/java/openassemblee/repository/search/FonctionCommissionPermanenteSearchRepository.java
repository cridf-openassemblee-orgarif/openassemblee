package openassemblee.repository.search;

import openassemblee.domain.FonctionCommissionPermanente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FonctionCommissionPermanente entity.
 */
public interface FonctionCommissionPermanenteSearchRepository extends ElasticsearchRepository<FonctionCommissionPermanente, Long> {
}
