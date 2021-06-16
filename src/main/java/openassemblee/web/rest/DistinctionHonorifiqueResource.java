package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.DistinctionHonorifique;
import openassemblee.repository.DistinctionHonorifiqueRepository;
import openassemblee.repository.search.DistinctionHonorifiqueSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing DistinctionHonorifique.
 */
@RestController
@RequestMapping("/api")
public class DistinctionHonorifiqueResource {

    private final Logger log = LoggerFactory.getLogger(DistinctionHonorifiqueResource.class);

    @Inject
    private DistinctionHonorifiqueRepository distinctionHonorifiqueRepository;

    @Inject
    private DistinctionHonorifiqueSearchRepository distinctionHonorifiqueSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    /**
     * POST  /distinctionHonorifiques -> Create a new distinctionHonorifique.
     */
    @RequestMapping(value = "/distinctionHonorifiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DistinctionHonorifique> createDistinctionHonorifique(@RequestBody DistinctionHonorifique distinctionHonorifique) throws URISyntaxException {
        log.debug("REST request to save DistinctionHonorifique : {}", distinctionHonorifique);
        if (distinctionHonorifique.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new distinctionHonorifique cannot already have an ID").body(null);
        }
        DistinctionHonorifique result = distinctionHonorifiqueRepository.save(distinctionHonorifique);
        distinctionHonorifiqueSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/distinctionHonorifiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("distinctionHonorifique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /distinctionHonorifiques -> Updates an existing distinctionHonorifique.
     */
    @RequestMapping(value = "/distinctionHonorifiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DistinctionHonorifique> updateDistinctionHonorifique(@RequestBody DistinctionHonorifique distinctionHonorifique) throws URISyntaxException {
        log.debug("REST request to update DistinctionHonorifique : {}", distinctionHonorifique);
        if (distinctionHonorifique.getId() == null) {
            return createDistinctionHonorifique(distinctionHonorifique);
        }
        DistinctionHonorifique result = distinctionHonorifiqueRepository.save(distinctionHonorifique);
        distinctionHonorifiqueSearchRepository.save(distinctionHonorifique);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("distinctionHonorifique", distinctionHonorifique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /distinctionHonorifiques -> get all the distinctionHonorifiques.
     */
    @RequestMapping(value = "/distinctionHonorifiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DistinctionHonorifique> getAllDistinctionHonorifiques() {
        log.debug("REST request to get all DistinctionHonorifiques");
        return distinctionHonorifiqueRepository.findAll();
    }

    /**
     * GET  /distinctionHonorifiques/:id -> get the "id" distinctionHonorifique.
     */
    @RequestMapping(value = "/distinctionHonorifiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DistinctionHonorifique> getDistinctionHonorifique(@PathVariable Long id) {
        log.debug("REST request to get DistinctionHonorifique : {}", id);
        return Optional.ofNullable(distinctionHonorifiqueRepository.findOne(id))
            .map(distinctionHonorifique -> new ResponseEntity<>(
                distinctionHonorifique,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /distinctionHonorifiques/:id -> delete the "id" distinctionHonorifique.
     */
    @RequestMapping(value = "/distinctionHonorifiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDistinctionHonorifique(@PathVariable Long id) {
        log.debug("REST request to delete DistinctionHonorifique : {}", id);
        distinctionHonorifiqueRepository.delete(id);
        distinctionHonorifiqueSearchRepository.delete(id);
        auditTrailService.logDeletion(DistinctionHonorifique.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("distinctionHonorifique", id.toString())).build();
    }

    /**
     * SEARCH  /_search/distinctionHonorifiques/:query -> search for the distinctionHonorifique corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/distinctionHonorifiques/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DistinctionHonorifique> searchDistinctionHonorifiques(@PathVariable String query) {
        return StreamSupport
            .stream(distinctionHonorifiqueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
