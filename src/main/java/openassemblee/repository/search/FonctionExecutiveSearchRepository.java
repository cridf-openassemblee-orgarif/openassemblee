package openassemblee.repository.search;

import openassemblee.domain.FonctionExecutive;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FonctionExecutive entity.
 */
public interface FonctionExecutiveSearchRepository extends ElasticsearchRepository<FonctionExecutive, Long> {
}
