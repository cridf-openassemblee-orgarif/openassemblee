package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.ReunionCommissionThematique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ReunionCommissionThematique entity.
 */
public interface ReunionCommissionThematiqueSearchRepository extends ElasticsearchRepository<ReunionCommissionThematique, Long> {
}
