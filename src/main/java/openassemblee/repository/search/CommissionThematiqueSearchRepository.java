package openassemblee.repository.search;

import openassemblee.domain.CommissionThematique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CommissionThematique entity.
 */
public interface CommissionThematiqueSearchRepository
    extends ElasticsearchRepository<CommissionThematique, Long> {}
