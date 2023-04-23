package openassemblee.repository.search;

import openassemblee.domain.AdresseMail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AdresseMail entity.
 */
public interface AdresseMailSearchRepository
    extends ElasticsearchRepository<AdresseMail, Long> {}
