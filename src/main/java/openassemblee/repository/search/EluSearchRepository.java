package openassemblee.repository.search;

import openassemblee.domain.Elu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Elu entity.
 */
public interface EluSearchRepository
    extends ElasticsearchRepository<Elu, Long> {}
