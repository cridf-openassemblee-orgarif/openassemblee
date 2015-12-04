package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.GroupePolitique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GroupePolitique entity.
 */
public interface GroupePolitiqueSearchRepository extends ElasticsearchRepository<GroupePolitique, Long> {
}
