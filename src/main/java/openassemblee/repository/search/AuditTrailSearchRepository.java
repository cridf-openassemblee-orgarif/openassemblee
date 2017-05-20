package openassemblee.repository.search;

import openassemblee.domain.AuditTrail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AuditTrail entity.
 */
public interface AuditTrailSearchRepository extends ElasticsearchRepository<AuditTrail, Long> {
}
