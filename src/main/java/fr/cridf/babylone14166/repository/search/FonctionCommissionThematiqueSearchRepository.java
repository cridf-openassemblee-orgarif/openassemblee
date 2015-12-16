package fr.cridf.babylone14166.repository.search;

import fr.cridf.babylone14166.domain.FonctionCommissionThematique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FonctionCommissionThematique entity.
 */
public interface FonctionCommissionThematiqueSearchRepository extends ElasticsearchRepository<FonctionCommissionThematique, Long> {
}
