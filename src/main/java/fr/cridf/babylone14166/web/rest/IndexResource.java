package fr.cridf.babylone14166.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.repository.EluRepository;
import fr.cridf.babylone14166.repository.GroupePolitiqueRepository;
import fr.cridf.babylone14166.repository.search.EluSearchRepository;
import fr.cridf.babylone14166.repository.search.GroupePolitiqueSearchRepository;

@RestController
@RequestMapping("/api")
public class IndexResource {

    private final Logger log = LoggerFactory.getLogger(IndexResource.class);

    @Autowired
    protected EluRepository eluRepository;
    @Autowired
    protected EluSearchRepository eluSearchRepository;

    @Autowired
    protected GroupePolitiqueRepository groupePolitiqueRepository;
    @Autowired
    protected GroupePolitiqueSearchRepository groupePolitiqueSearchRepository;

    @RequestMapping(value = "/index-reset", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> resetIndex() {
        log.debug("REST reset search index");
        resetRepository(eluRepository, eluSearchRepository);
        resetRepository(groupePolitiqueRepository, groupePolitiqueSearchRepository);
        return ResponseEntity.ok().build();
    }

    private <T> void resetRepository(JpaRepository<T, Long> jpaRepository,
        ElasticsearchRepository<T, Long> elasticsearchRepository) {
        elasticsearchRepository.deleteAll();
        for (T t : jpaRepository.findAll()) {
            elasticsearchRepository.save(t);
        }
    }

    @RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<Object>> search(@PathVariable String query) {
        log.debug("REST reset search index");
        List<Object> result = new ArrayList<>();
        addResults(query, eluSearchRepository, result);
        addResults(query, groupePolitiqueSearchRepository, result);
        return ResponseEntity.ok(result);
    }

    private <T> void addResults(String query, ElasticsearchRepository<T, Long> searchRepository, List<Object> result) {
        for (T t : searchRepository.search(queryStringQuery(query))) {
            result.add(t);
        }
    }
}
