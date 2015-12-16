package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.CommissionThematique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CommissionThematique entity.
 */
public interface CommissionThematiqueSearchRepository extends ElasticsearchRepository<CommissionThematique, Long> {
}
