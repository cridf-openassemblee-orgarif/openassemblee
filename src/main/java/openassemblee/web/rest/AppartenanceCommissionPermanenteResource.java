package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.AppartenanceCommissionPermanente;
import openassemblee.repository.AppartenanceCommissionPermanenteRepository;
import openassemblee.repository.search.AppartenanceCommissionPermanenteSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.service.SessionMandatureService;
import openassemblee.web.rest.util.HeaderUtil;
import org.elasticsearch.common.collect.Lists;
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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing AppartenanceCommissionPermanente.
 */
@RestController
@RequestMapping("/api")
public class AppartenanceCommissionPermanenteResource {

    private final Logger log = LoggerFactory.getLogger(AppartenanceCommissionPermanenteResource.class);

    @Inject
    private AppartenanceCommissionPermanenteRepository appartenanceCommissionPermanenteRepository;

    @Inject
    private AppartenanceCommissionPermanenteSearchRepository appartenanceCommissionPermanenteSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private SessionMandatureService sessionMandatureService;

    /**
     * POST  /appartenanceCommissionPermanentes -> Create a new appartenanceCommissionPermanente.
     */
    @RequestMapping(value = "/appartenanceCommissionPermanentes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppartenanceCommissionPermanente> createAppartenanceCommissionPermanente(@RequestBody AppartenanceCommissionPermanente appartenanceCommissionPermanente) throws URISyntaxException {
        log.debug("REST request to save AppartenanceCommissionPermanente : {}", appartenanceCommissionPermanente);
        if (appartenanceCommissionPermanente.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new appartenanceCommissionPermanente cannot already have an ID").body(null);
        }
        AppartenanceCommissionPermanente result = appartenanceCommissionPermanenteRepository.save(appartenanceCommissionPermanente);
        appartenanceCommissionPermanenteSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/appartenanceCommissionPermanentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("appartenanceCommissionPermanente", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /appartenanceCommissionPermanentes -> Updates an existing appartenanceCommissionPermanente.
     */
    @RequestMapping(value = "/appartenanceCommissionPermanentes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppartenanceCommissionPermanente> updateAppartenanceCommissionPermanente(@RequestBody AppartenanceCommissionPermanente appartenanceCommissionPermanente) throws URISyntaxException {
        log.debug("REST request to update AppartenanceCommissionPermanente : {}", appartenanceCommissionPermanente);
        if (appartenanceCommissionPermanente.getId() == null) {
            return createAppartenanceCommissionPermanente(appartenanceCommissionPermanente);
        }
        AppartenanceCommissionPermanente result = appartenanceCommissionPermanenteRepository.save(appartenanceCommissionPermanente);
        appartenanceCommissionPermanenteSearchRepository.save(appartenanceCommissionPermanente);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("appartenanceCommissionPermanente", appartenanceCommissionPermanente.getId().toString()))
            .body(result);
    }

    /**
     * GET  /appartenanceCommissionPermanentes -> get all the appartenanceCommissionPermanentes.
     */
    @RequestMapping(value = "/appartenanceCommissionPermanentes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppartenanceCommissionPermanente> getAllAppartenanceCommissionPermanentes() {
        log.debug("REST request to get all AppartenanceCommissionPermanentes");
        return appartenanceCommissionPermanenteRepository.findByMandature(sessionMandatureService.getMandature());
    }

    /**
     * GET  /appartenanceCommissionPermanentes/:id -> get the "id" appartenanceCommissionPermanente.
     */
    @RequestMapping(value = "/appartenanceCommissionPermanentes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppartenanceCommissionPermanente> getAppartenanceCommissionPermanente(@PathVariable Long id) {
        log.debug("REST request to get AppartenanceCommissionPermanente : {}", id);
        AppartenanceCommissionPermanente a =
            appartenanceCommissionPermanenteRepository.findOne(id);
        if (a != null) {
            return new ResponseEntity<>(a, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETE  /appartenanceCommissionPermanentes/:id -> delete the "id" appartenanceCommissionPermanente.
     */
    @RequestMapping(value = "/appartenanceCommissionPermanentes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAppartenanceCommissionPermanente(@PathVariable Long id) {
        log.debug("REST request to delete AppartenanceCommissionPermanente : {}", id);
        appartenanceCommissionPermanenteRepository.delete(id);
        appartenanceCommissionPermanenteSearchRepository.delete(id);
        auditTrailService.logDeletion(AppartenanceCommissionPermanente.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("appartenanceCommissionPermanente", id.toString())).build();
    }

    /**
     * SEARCH  /_search/appartenanceCommissionPermanentes/:query -> search for the appartenanceCommissionPermanente corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/appartenanceCommissionPermanentes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppartenanceCommissionPermanente> searchAppartenanceCommissionPermanentes(@PathVariable String query) {
        return Lists.newArrayList(appartenanceCommissionPermanenteSearchRepository.search(queryStringQuery(query)));
    }
}
