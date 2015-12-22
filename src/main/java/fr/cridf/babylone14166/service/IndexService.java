package fr.cridf.babylone14166.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import fr.cridf.babylone14166.repository.EluRepository;
import fr.cridf.babylone14166.repository.GroupePolitiqueRepository;
import fr.cridf.babylone14166.repository.search.EluSearchRepository;
import fr.cridf.babylone14166.repository.search.GroupePolitiqueSearchRepository;

@Service
public class IndexService {

    private final Logger logger = LoggerFactory.getLogger(IndexService.class);

    @Autowired
    protected EluRepository eluRepository;
    @Autowired
    protected EluSearchRepository eluSearchRepository;

    @Autowired
    protected GroupePolitiqueRepository groupePolitiqueRepository;
    @Autowired
    protected GroupePolitiqueSearchRepository groupePolitiqueSearchRepository;

    public void resetIndex() {
        logger.debug("Reset search index");
        resetRepository(eluRepository, eluSearchRepository);
        resetRepository(groupePolitiqueRepository, groupePolitiqueSearchRepository);
    }

    private <T> void resetRepository(JpaRepository<T, Long> jpaRepository,
        ElasticsearchRepository<T, Long> elasticsearchRepository) {
        elasticsearchRepository.deleteAll();
        for (T t : jpaRepository.findAll()) {
            elasticsearchRepository.save(t);
        }
    }

    public List<Object> search(String query) {
        List<Object> result = new ArrayList<>();
        addResults(query, eluSearchRepository, result);
        addResults(query, groupePolitiqueSearchRepository, result);
        return result;
    }

    private <T> void addResults(String query, ElasticsearchRepository<T, Long> searchRepository, List<java.lang
        .Object> result) {
        for (T t : searchRepository.search(queryStringQuery(query))) {
            result.add(t);
        }
    }

}
