package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.Elu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Elu entity.
 */
public interface EluSearchRepository extends ElasticsearchRepository<Elu, Long> {
}
