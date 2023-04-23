package openassemblee.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.codahale.metrics.annotation.Timed;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import openassemblee.domain.AppartenanceOrganisme;
import openassemblee.repository.AppartenanceOrganismeRepository;
import openassemblee.repository.search.AppartenanceOrganismeSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing AppartenanceOrganisme.
 */
@RestController
@RequestMapping("/api")
public class AppartenanceOrganismeResource {

    private final Logger log = LoggerFactory.getLogger(
        AppartenanceOrganismeResource.class
    );

    @Inject
    private AppartenanceOrganismeRepository appartenanceOrganismeRepository;

    @Inject
    private AppartenanceOrganismeSearchRepository appartenanceOrganismeSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    /**
     * POST  /appartenanceOrganismes -> Create a new appartenanceOrganisme.
     */
    @RequestMapping(
        value = "/appartenanceOrganismes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<AppartenanceOrganisme> createAppartenanceOrganisme(
        @RequestBody AppartenanceOrganisme appartenanceOrganisme
    ) throws URISyntaxException {
        log.debug(
            "REST request to save AppartenanceOrganisme : {}",
            appartenanceOrganisme
        );
        if (appartenanceOrganisme.getId() != null) {
            return ResponseEntity
                .badRequest()
                .header(
                    "Failure",
                    "A new appartenanceOrganisme cannot already have an ID"
                )
                .body(null);
        }
        AppartenanceOrganisme result = appartenanceOrganismeRepository.save(
            appartenanceOrganisme
        );
        appartenanceOrganismeSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity
            .created(new URI("/api/appartenanceOrganismes/" + result.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    "appartenanceOrganisme",
                    result.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * PUT  /appartenanceOrganismes -> Updates an existing appartenanceOrganisme.
     */
    @RequestMapping(
        value = "/appartenanceOrganismes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<AppartenanceOrganisme> updateAppartenanceOrganisme(
        @RequestBody AppartenanceOrganisme appartenanceOrganisme
    ) throws URISyntaxException {
        log.debug(
            "REST request to update AppartenanceOrganisme : {}",
            appartenanceOrganisme
        );
        if (appartenanceOrganisme.getId() == null) {
            return createAppartenanceOrganisme(appartenanceOrganisme);
        }
        AppartenanceOrganisme result = appartenanceOrganismeRepository.save(
            appartenanceOrganisme
        );
        appartenanceOrganismeSearchRepository.save(appartenanceOrganisme);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    "appartenanceOrganisme",
                    appartenanceOrganisme.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * GET  /appartenanceOrganismes -> get all the appartenanceOrganismes.
     */
    @RequestMapping(
        value = "/appartenanceOrganismes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<AppartenanceOrganisme> getAllAppartenanceOrganismes() {
        log.debug("REST request to get all AppartenanceOrganismes");
        return appartenanceOrganismeRepository.findAll();
    }

    /**
     * GET  /appartenanceOrganismes/:id -> get the "id" appartenanceOrganisme.
     */
    @RequestMapping(
        value = "/appartenanceOrganismes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<AppartenanceOrganisme> getAppartenanceOrganisme(
        @PathVariable Long id
    ) {
        log.debug("REST request to get AppartenanceOrganisme : {}", id);
        return Optional
            .ofNullable(appartenanceOrganismeRepository.findOne(id))
            .map(appartenanceOrganisme ->
                new ResponseEntity<>(appartenanceOrganisme, HttpStatus.OK)
            )
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /appartenanceOrganismes/:id -> delete the "id" appartenanceOrganisme.
     */
    @RequestMapping(
        value = "/appartenanceOrganismes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<Void> deleteAppartenanceOrganisme(
        @PathVariable Long id
    ) {
        log.debug("REST request to delete AppartenanceOrganisme : {}", id);
        appartenanceOrganismeRepository.delete(id);
        appartenanceOrganismeSearchRepository.delete(id);
        auditTrailService.logDeletion(AppartenanceOrganisme.class, id);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    "appartenanceOrganisme",
                    id.toString()
                )
            )
            .build();
    }

    /**
     * SEARCH  /_search/appartenanceOrganismes/:query -> search for the appartenanceOrganisme corresponding
     * to the query.
     */
    @RequestMapping(
        value = "/_search/appartenanceOrganismes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<AppartenanceOrganisme> searchAppartenanceOrganismes(
        @PathVariable String query
    ) {
        return StreamSupport
            .stream(
                appartenanceOrganismeSearchRepository
                    .search(queryStringQuery(query))
                    .spliterator(),
                false
            )
            .collect(Collectors.toList());
    }
}
