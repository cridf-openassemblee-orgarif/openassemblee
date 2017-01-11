package fr.cridf.babylone14166.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cridf.babylone14166.domain.ReunionCao;
import fr.cridf.babylone14166.repository.ReunionCaoRepository;
import fr.cridf.babylone14166.repository.search.ReunionCaoSearchRepository;
import fr.cridf.babylone14166.web.rest.util.HeaderUtil;
import fr.cridf.babylone14166.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ReunionCao.
 */
@RestController
@RequestMapping("/api")
public class ReunionCaoResource {

    private final Logger log = LoggerFactory.getLogger(ReunionCaoResource.class);

    @Inject
    private ReunionCaoRepository reunionCaoRepository;

    @Inject
    private ReunionCaoSearchRepository reunionCaoSearchRepository;

    /**
     * POST  /reunionCaos -> Create a new reunionCao.
     */
    @RequestMapping(value = "/reunionCaos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReunionCao> createReunionCao(@RequestBody ReunionCao reunionCao) throws URISyntaxException {
        log.debug("REST request to save ReunionCao : {}", reunionCao);
        if (reunionCao.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new reunionCao cannot already have an ID").body(null);
        }
        ReunionCao result = reunionCaoRepository.save(reunionCao);
        reunionCaoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/reunionCaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("reunionCao", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reunionCaos -> Updates an existing reunionCao.
     */
    @RequestMapping(value = "/reunionCaos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReunionCao> updateReunionCao(@RequestBody ReunionCao reunionCao) throws URISyntaxException {
        log.debug("REST request to update ReunionCao : {}", reunionCao);
        if (reunionCao.getId() == null) {
            return createReunionCao(reunionCao);
        }
        ReunionCao result = reunionCaoRepository.save(reunionCao);
        reunionCaoSearchRepository.save(reunionCao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("reunionCao", reunionCao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reunionCaos -> get all the reunionCaos.
     */
    @RequestMapping(value = "/reunionCaos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ReunionCao>> getAllReunionCaos(Pageable pageable)
        throws URISyntaxException {
        Page<ReunionCao> page = reunionCaoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reunionCaos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reunionCaos/:id -> get the "id" reunionCao.
     */
    @RequestMapping(value = "/reunionCaos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReunionCao> getReunionCao(@PathVariable Long id) {
        log.debug("REST request to get ReunionCao : {}", id);
        return Optional.ofNullable(reunionCaoRepository.findOne(id))
            .map(reunionCao -> new ResponseEntity<>(
                reunionCao,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reunionCaos/:id -> delete the "id" reunionCao.
     */
    @RequestMapping(value = "/reunionCaos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReunionCao(@PathVariable Long id) {
        log.debug("REST request to delete ReunionCao : {}", id);
        reunionCaoRepository.delete(id);
        reunionCaoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("reunionCao", id.toString())).build();
    }

    /**
     * SEARCH  /_search/reunionCaos/:query -> search for the reunionCao corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/reunionCaos/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ReunionCao> searchReunionCaos(@PathVariable String query) {
        return StreamSupport
            .stream(reunionCaoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
