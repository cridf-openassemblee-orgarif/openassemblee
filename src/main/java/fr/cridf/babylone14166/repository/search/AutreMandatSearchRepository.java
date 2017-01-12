package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.AutreMandat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AutreMandat entity.
 */
public interface AutreMandatSearchRepository extends ElasticsearchRepository<AutreMandat, Long> {
}
