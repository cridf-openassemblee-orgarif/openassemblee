package openassemblee.repository.search;

import openassemblee.domain.FonctionGroupePolitique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FonctionGroupePolitique entity.
 */
public interface FonctionGroupePolitiqueSearchRepository
    extends ElasticsearchRepository<FonctionGroupePolitique, Long> {}
