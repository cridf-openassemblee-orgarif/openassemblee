package openassemblee.repository.search;

import openassemblee.domain.ListeElectorale;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ListeElectorale entity.
 */
public interface ListeElectoraleSearchRepository
    extends ElasticsearchRepository<ListeElectorale, Long> {}
