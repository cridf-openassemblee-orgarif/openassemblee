package openassemblee.repository.search;

import openassemblee.domain.IdentiteInternet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the IdentiteInternet entity.
 */
public interface IdentiteInternetSearchRepository
    extends ElasticsearchRepository<IdentiteInternet, Long> {}
