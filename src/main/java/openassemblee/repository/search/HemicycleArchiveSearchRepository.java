package openassemblee.repository.search;

import openassemblee.domain.HemicycleArchive;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the HemicycleArchive entity.
 */
public interface HemicycleArchiveSearchRepository extends ElasticsearchRepository<HemicycleArchive, Long> {
}
