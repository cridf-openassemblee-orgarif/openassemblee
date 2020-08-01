package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.HemicycleArchive;
import openassemblee.repository.HemicycleArchiveRepository;
import openassemblee.repository.search.HemicycleArchiveSearchRepository;
import openassemblee.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing HemicycleArchive.
 */
@RestController
@RequestMapping("/api")
public class HemicycleArchiveResource {

    private final Logger log = LoggerFactory.getLogger(HemicycleArchiveResource.class);

    @Inject
    private HemicycleArchiveRepository hemicycleArchiveRepository;

    @Inject
    private HemicycleArchiveSearchRepository hemicycleArchiveSearchRepository;

    /**
     * POST  /hemicycleArchives -> Create a new hemicycleArchive.
     */
    @RequestMapping(value = "/hemicycleArchives",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HemicycleArchive> createHemicycleArchive(@RequestBody HemicycleArchive hemicycleArchive) throws URISyntaxException {
        log.debug("REST request to save HemicycleArchive : {}", hemicycleArchive);
        if (hemicycleArchive.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hemicycleArchive cannot already have an ID").body(null);
        }
        HemicycleArchive result = hemicycleArchiveRepository.save(hemicycleArchive);
        hemicycleArchiveSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hemicycleArchives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hemicycleArchive", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hemicycleArchives -> Updates an existing hemicycleArchive.
     */
    @RequestMapping(value = "/hemicycleArchives",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HemicycleArchive> updateHemicycleArchive(@RequestBody HemicycleArchive hemicycleArchive) throws URISyntaxException {
        log.debug("REST request to update HemicycleArchive : {}", hemicycleArchive);
        if (hemicycleArchive.getId() == null) {
            return createHemicycleArchive(hemicycleArchive);
        }
        HemicycleArchive result = hemicycleArchiveRepository.save(hemicycleArchive);
        hemicycleArchiveSearchRepository.save(hemicycleArchive);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hemicycleArchive", hemicycleArchive.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hemicycleArchives -> get all the hemicycleArchives.
     */
    @RequestMapping(value = "/hemicycleArchives",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HemicycleArchive> getAllHemicycleArchives() {
        log.debug("REST request to get all HemicycleArchives");
        return hemicycleArchiveRepository.findAll();
    }

    /**
     * GET  /hemicycleArchives/:id -> get the "id" hemicycleArchive.
     */
    @RequestMapping(value = "/hemicycleArchives/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HemicycleArchive> getHemicycleArchive(@PathVariable Long id) {
        log.debug("REST request to get HemicycleArchive : {}", id);
        return Optional.ofNullable(hemicycleArchiveRepository.findOne(id))
            .map(hemicycleArchive -> new ResponseEntity<>(
                hemicycleArchive,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hemicycleArchives/:id -> delete the "id" hemicycleArchive.
     */
    @RequestMapping(value = "/hemicycleArchives/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHemicycleArchive(@PathVariable Long id) {
        log.debug("REST request to delete HemicycleArchive : {}", id);
        hemicycleArchiveRepository.delete(id);
        hemicycleArchiveSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hemicycleArchive", id.toString())).build();
    }

    /**
     * SEARCH  /_search/hemicycleArchives/:query -> search for the hemicycleArchive corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/hemicycleArchives/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HemicycleArchive> searchHemicycleArchives(@PathVariable String query) {
        return StreamSupport
            .stream(hemicycleArchiveSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
