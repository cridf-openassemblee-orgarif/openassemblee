package openassemblee.repository.search;

import openassemblee.domain.AppartenanceCommissionThematique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AppartenanceCommissionThematique entity.
 */
public interface AppartenanceCommissionThematiqueSearchRepository
    extends ElasticsearchRepository<AppartenanceCommissionThematique, Long> {}
