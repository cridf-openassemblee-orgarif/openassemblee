package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.AuditTrail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AuditTrail entity.
 */
public interface AuditTrailSearchRepository extends ElasticsearchRepository<AuditTrail, Long> {
}
