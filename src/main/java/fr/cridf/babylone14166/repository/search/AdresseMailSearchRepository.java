package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.AdresseMail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AdresseMail entity.
 */
public interface AdresseMailSearchRepository extends ElasticsearchRepository<AdresseMail, Long> {
}
