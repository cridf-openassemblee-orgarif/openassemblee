package openassemblee.repository.search;

import openassemblee.domain.ReunionCommissionThematique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ReunionCommissionThematique entity.
 */
public interface ReunionCommissionThematiqueSearchRepository
    extends ElasticsearchRepository<ReunionCommissionThematique, Long> {}
