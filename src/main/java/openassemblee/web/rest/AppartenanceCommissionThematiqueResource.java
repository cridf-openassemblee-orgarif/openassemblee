package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.AppartenanceCommissionThematique;
import openassemblee.repository.AppartenanceCommissionThematiqueRepository;
import openassemblee.repository.search.AppartenanceCommissionThematiqueSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing AppartenanceCommissionThematique.
 */
@RestController
@RequestMapping("/api")
public class AppartenanceCommissionThematiqueResource {

    private final Logger log = LoggerFactory.getLogger(AppartenanceCommissionThematiqueResource.class);

    @Inject
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;

    @Inject
    private AppartenanceCommissionThematiqueSearchRepository appartenanceCommissionThematiqueSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    private Random random = new Random();

    /**
     * POST  /appartenanceCommissionThematiques -> Create a new appartenanceCommissionThematique.
     */
    @RequestMapping(value = "/appartenanceCommissionThematiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppartenanceCommissionThematique> createAppartenanceCommissionThematique(@RequestBody AppartenanceCommissionThematique appartenanceCommissionThematique) throws URISyntaxException {
        log.debug("REST request to save AppartenanceCommissionThematique : {}", appartenanceCommissionThematique);
        if (appartenanceCommissionThematique.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new appartenanceCommissionThematique cannot already have an ID").body(null);
        }
        appartenanceCommissionThematique.setImportUid(new BigInteger(50, random).toString(16));
        AppartenanceCommissionThematique result = appartenanceCommissionThematiqueRepository.save(appartenanceCommissionThematique);
        appartenanceCommissionThematiqueSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/appartenanceCommissionThematiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("appartenanceCommissionThematique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /appartenanceCommissionThematiques -> Updates an existing appartenanceCommissionThematique.
     */
    @RequestMapping(value = "/appartenanceCommissionThematiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppartenanceCommissionThematique> updateAppartenanceCommissionThematique(@RequestBody AppartenanceCommissionThematique appartenanceCommissionThematique) throws URISyntaxException {
        log.debug("REST request to update AppartenanceCommissionThematique : {}", appartenanceCommissionThematique);
        if (appartenanceCommissionThematique.getId() == null) {
            return createAppartenanceCommissionThematique(appartenanceCommissionThematique);
        }
        AppartenanceCommissionThematique result = appartenanceCommissionThematiqueRepository.save(appartenanceCommissionThematique);
        appartenanceCommissionThematiqueSearchRepository.save(appartenanceCommissionThematique);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("appartenanceCommissionThematique", appartenanceCommissionThematique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /appartenanceCommissionThematiques -> get all the appartenanceCommissionThematiques.
     */
    @RequestMapping(value = "/appartenanceCommissionThematiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppartenanceCommissionThematique> getAllAppartenanceCommissionThematiques() {
        log.debug("REST request to get all AppartenanceCommissionThematiques");
        return appartenanceCommissionThematiqueRepository.findAll();
    }

    /**
     * GET  /appartenanceCommissionThematiques/:id -> get the "id" appartenanceCommissionThematique.
     */
    @RequestMapping(value = "/appartenanceCommissionThematiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppartenanceCommissionThematique> getAppartenanceCommissionThematique(@PathVariable Long id) {
        log.debug("REST request to get AppartenanceCommissionThematique : {}", id);
        return Optional.ofNullable(appartenanceCommissionThematiqueRepository.findOne(id))
            .map(appartenanceCommissionThematique -> new ResponseEntity<>(
                appartenanceCommissionThematique,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /appartenanceCommissionThematiques/:id -> delete the "id" appartenanceCommissionThematique.
     */
    @RequestMapping(value = "/appartenanceCommissionThematiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAppartenanceCommissionThematique(@PathVariable Long id) {
        log.debug("REST request to delete AppartenanceCommissionThematique : {}", id);
        appartenanceCommissionThematiqueRepository.delete(id);
        appartenanceCommissionThematiqueSearchRepository.delete(id);
        auditTrailService.logDeletion(AppartenanceCommissionThematique.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("appartenanceCommissionThematique", id.toString())).build();
    }

    /**
     * SEARCH  /_search/appartenanceCommissionThematiques/:query -> search for the appartenanceCommissionThematique corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/appartenanceCommissionThematiques/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppartenanceCommissionThematique> searchAppartenanceCommissionThematiques(@PathVariable String query) {
        return StreamSupport
            .stream(appartenanceCommissionThematiqueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
