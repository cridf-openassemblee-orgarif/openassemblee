package openassemblee.repository.search;

import openassemblee.domain.GroupePolitique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GroupePolitique entity.
 */
public interface GroupePolitiqueSearchRepository extends ElasticsearchRepository<GroupePolitique, Long> {
}
