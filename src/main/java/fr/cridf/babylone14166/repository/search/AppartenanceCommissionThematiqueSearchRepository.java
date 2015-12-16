package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.AppartenanceCommissionThematique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AppartenanceCommissionThematique entity.
 */
public interface AppartenanceCommissionThematiqueSearchRepository extends ElasticsearchRepository<AppartenanceCommissionThematique, Long> {
}
