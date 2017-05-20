package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.AppartenanceGroupePolitique;
import openassemblee.repository.AppartenanceGroupePolitiqueRepository;
import openassemblee.repository.search.AppartenanceGroupePolitiqueSearchRepository;
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
 * REST controller for managing AppartenanceGroupePolitique.
 */
@RestController
@RequestMapping("/api")
public class AppartenanceGroupePolitiqueResource {

    private final Logger log = LoggerFactory.getLogger(AppartenanceGroupePolitiqueResource.class);

    @Inject
    private AppartenanceGroupePolitiqueRepository appartenanceGroupePolitiqueRepository;

    @Inject
    private AppartenanceGroupePolitiqueSearchRepository appartenanceGroupePolitiqueSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    /**
     * POST  /appartenanceGroupePolitiques -> Create a new appartenanceGroupePolitique.
     */
    @RequestMapping(value = "/appartenanceGroupePolitiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppartenanceGroupePolitique> createAppartenanceGroupePolitique(@RequestBody AppartenanceGroupePolitique appartenanceGroupePolitique) throws URISyntaxException {
        log.debug("REST request to save AppartenanceGroupePolitique : {}", appartenanceGroupePolitique);
        if (appartenanceGroupePolitique.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new appartenanceGroupePolitique cannot already have an ID").body(null);
        }
        AppartenanceGroupePolitique result = appartenanceGroupePolitiqueRepository.save(appartenanceGroupePolitique);
        appartenanceGroupePolitiqueSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/appartenanceGroupePolitiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("appartenanceGroupePolitique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /appartenanceGroupePolitiques -> Updates an existing appartenanceGroupePolitique.
     */
    @RequestMapping(value = "/appartenanceGroupePolitiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppartenanceGroupePolitique> updateAppartenanceGroupePolitique(@RequestBody AppartenanceGroupePolitique appartenanceGroupePolitique) throws URISyntaxException {
        log.debug("REST request to update AppartenanceGroupePolitique : {}", appartenanceGroupePolitique);
        if (appartenanceGroupePolitique.getId() == null) {
            return createAppartenanceGroupePolitique(appartenanceGroupePolitique);
        }
        AppartenanceGroupePolitique result = appartenanceGroupePolitiqueRepository.save(appartenanceGroupePolitique);
        appartenanceGroupePolitiqueSearchRepository.save(appartenanceGroupePolitique);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("appartenanceGroupePolitique", appartenanceGroupePolitique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /appartenanceGroupePolitiques -> get all the appartenanceGroupePolitiques.
     */
    @RequestMapping(value = "/appartenanceGroupePolitiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppartenanceGroupePolitique> getAllAppartenanceGroupePolitiques() {
        log.debug("REST request to get all AppartenanceGroupePolitiques");
        return appartenanceGroupePolitiqueRepository.findAll();
    }

    /**
     * GET  /appartenanceGroupePolitiques/:id -> get the "id" appartenanceGroupePolitique.
     */
    @RequestMapping(value = "/appartenanceGroupePolitiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppartenanceGroupePolitique> getAppartenanceGroupePolitique(@PathVariable Long id) {
        log.debug("REST request to get AppartenanceGroupePolitique : {}", id);
        return Optional.ofNullable(appartenanceGroupePolitiqueRepository.findOne(id))
            .map(appartenanceGroupePolitique -> new ResponseEntity<>(
                appartenanceGroupePolitique,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /appartenanceGroupePolitiques/:id -> delete the "id" appartenanceGroupePolitique.
     */
    @RequestMapping(value = "/appartenanceGroupePolitiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAppartenanceGroupePolitique(@PathVariable Long id) {
        log.debug("REST request to delete AppartenanceGroupePolitique : {}", id);
        appartenanceGroupePolitiqueRepository.delete(id);
        appartenanceGroupePolitiqueSearchRepository.delete(id);
        auditTrailService.logDeletion(AppartenanceGroupePolitique.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("appartenanceGroupePolitique", id.toString())).build();
    }

    /**
     * SEARCH  /_search/appartenanceGroupePolitiques/:query -> search for the appartenanceGroupePolitique corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/appartenanceGroupePolitiques/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppartenanceGroupePolitique> searchAppartenanceGroupePolitiques(@PathVariable String query) {
        return StreamSupport
            .stream(appartenanceGroupePolitiqueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
