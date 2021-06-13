package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.Mandature;
import openassemblee.repository.MandatureRepository;
import openassemblee.repository.search.MandatureSearchRepository;
import openassemblee.service.SessionMandatureService;
import openassemblee.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
 * REST controller for managing Mandature.
 */
@RestController
@RequestMapping("/api")
public class MandatureResource {

    private final Logger log = LoggerFactory.getLogger(MandatureResource.class);

    @Inject
    private MandatureRepository mandatureRepository;

    @Inject
    private MandatureSearchRepository mandatureSearchRepository;

    @Inject
    private SessionMandatureService sessionMandatureService;

    /**
     * POST  /mandatures -> Create a new mandature.
     */
    @RequestMapping(value = "/mandatures",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mandature> createMandature(@RequestBody Mandature mandature) throws URISyntaxException {
        log.debug("REST request to save Mandature : {}", mandature);
        if (mandature.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new mandature cannot already have an ID").body(null);
        }
        Mandature result = mandatureRepository.save(mandature);
        mandatureSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/mandatures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mandature", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mandatures -> Updates an existing mandature.
     */
    @RequestMapping(value = "/mandatures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mandature> updateMandature(@RequestBody Mandature mandature) throws URISyntaxException {
        log.debug("REST request to update Mandature : {}", mandature);
        if (mandature.getId() == null) {
            return createMandature(mandature);
        }
        Mandature result = mandatureRepository.save(mandature);
        mandatureSearchRepository.save(mandature);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mandature", mandature.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mandatures -> get all the mandatures.
     */
    @RequestMapping(value = "/mandatures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Mandature> getAllMandatures() {
        log.debug("REST request to get all Mandatures");
        return mandatureRepository.findAll();
    }

    /**
     * GET  /mandatures/:id -> get the "id" mandature.
     */
    @RequestMapping(value = "/mandatures/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mandature> getMandature(@PathVariable Long id) {
        log.debug("REST request to get Mandature : {}", id);
        return Optional.ofNullable(mandatureRepository.findOne(id))
            .map(mandature -> new ResponseEntity<>(
                mandature,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mandatures/:id -> delete the "id" mandature.
     */
    @RequestMapping(value = "/mandatures/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMandature(@PathVariable Long id) {
        log.debug("REST request to delete Mandature : {}", id);
        mandatureRepository.delete(id);
        mandatureSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mandature", id.toString())).build();
    }

    /**
     * SEARCH  /_search/mandatures/:query -> search for the mandature corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/mandatures/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Mandature> searchMandatures(@PathVariable String query) {
        return StreamSupport
            .stream(mandatureSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/mandatures/set-current/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Void> setCurrent(@PathVariable Long id) {
        Mandature oldMandature = mandatureRepository.findOneByCurrent(true);
        // for first time, if the app is re-used
        if (oldMandature != null) {
            oldMandature.setCurrent(false);
            mandatureRepository.save(oldMandature);
        }
        Mandature newMandature = mandatureRepository.getOne(id);
        newMandature.setCurrent(true);
        mandatureRepository.save(newMandature);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/mandatures/set-current-for-session/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Void> setCurrentForSession(@PathVariable Long id) {
        sessionMandatureService.setMandature(mandatureRepository.findOne(id));
        return ResponseEntity.ok().build();
    }
}
