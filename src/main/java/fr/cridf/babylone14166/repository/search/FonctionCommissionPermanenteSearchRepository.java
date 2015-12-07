package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.FonctionCommissionPermanente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FonctionCommissionPermanente entity.
 */
public interface FonctionCommissionPermanenteSearchRepository extends ElasticsearchRepository<FonctionCommissionPermanente, Long> {
}
