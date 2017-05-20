package openassemblee.repository.search;

import openassemblee.domain.FonctionCommissionThematique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FonctionCommissionThematique entity.
 */
public interface FonctionCommissionThematiqueSearchRepository extends ElasticsearchRepository<FonctionCommissionThematique, Long> {
}
