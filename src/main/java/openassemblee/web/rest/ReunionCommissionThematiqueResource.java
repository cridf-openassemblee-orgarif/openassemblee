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
import openassemblee.domain.ReunionCommissionThematique;
import openassemblee.repository.ReunionCommissionThematiqueRepository;
import openassemblee.repository.search.ReunionCommissionThematiqueSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.service.ReunionCommissionThematiqueService;
import openassemblee.service.SessionMandatureService;
import openassemblee.web.rest.util.HeaderUtil;
import openassemblee.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing ReunionCommissionThematique.
 */
@RestController
@RequestMapping("/api")
public class ReunionCommissionThematiqueResource {

    private final Logger log = LoggerFactory.getLogger(
        ReunionCommissionThematiqueResource.class
    );

    @Inject
    private ReunionCommissionThematiqueRepository reunionCommissionThematiqueRepository;

    @Inject
    private ReunionCommissionThematiqueSearchRepository reunionCommissionThematiqueSearchRepository;

    @Inject
    private ReunionCommissionThematiqueService reunionCommissionThematiqueService;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private SessionMandatureService sessionMandatureService;

    /**
     * POST  /reunionCommissionThematiques -> Create a new reunionCommissionThematique.
     */
    @RequestMapping(
        value = "/reunionCommissionThematiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<ReunionCommissionThematique> createReunionCommissionThematique(
        @RequestBody ReunionCommissionThematique reunionCommissionThematique
    ) throws URISyntaxException {
        log.debug(
            "REST request to save ReunionCommissionThematique : {}",
            reunionCommissionThematique
        );
        if (reunionCommissionThematique.getId() != null) {
            return ResponseEntity
                .badRequest()
                .header(
                    "Failure",
                    "A new reunionCommissionThematique cannot already have an ID"
                )
                .body(null);
        }
        ReunionCommissionThematique result =
            reunionCommissionThematiqueService.create(
                reunionCommissionThematique
            );
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity
            .created(
                new URI("/api/reunionCommissionThematiques/" + result.getId())
            )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    "reunionCommissionThematique",
                    result.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * PUT  /reunionCommissionThematiques -> Updates an existing reunionCommissionThematique.
     */
    @RequestMapping(
        value = "/reunionCommissionThematiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<ReunionCommissionThematique> updateReunionCommissionThematique(
        @RequestBody ReunionCommissionThematique reunionCommissionThematique
    ) throws URISyntaxException {
        log.debug(
            "REST request to update ReunionCommissionThematique : {}",
            reunionCommissionThematique
        );
        if (reunionCommissionThematique.getId() == null) {
            return createReunionCommissionThematique(
                reunionCommissionThematique
            );
        }
        ReunionCommissionThematique result =
            reunionCommissionThematiqueRepository.save(
                reunionCommissionThematique
            );
        reunionCommissionThematiqueSearchRepository.save(
            reunionCommissionThematique
        );
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    "reunionCommissionThematique",
                    reunionCommissionThematique.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * GET  /reunionCommissionThematiques -> get all the reunionCommissionThematiques.
     */
    @RequestMapping(
        value = "/reunionCommissionThematiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<List<ReunionCommissionThematique>> getAllReunionCommissionThematiques(
        Pageable pageable
    ) throws URISyntaxException {
        Page<ReunionCommissionThematique> page =
            reunionCommissionThematiqueRepository.findByMandature(
                sessionMandatureService.getMandature(),
                pageable
            );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            page,
            "/api/reunionCommissionThematiques"
        );
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reunionCommissionThematiques/:id -> get the "id" reunionCommissionThematique.
     */
    @RequestMapping(
        value = "/reunionCommissionThematiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<ReunionCommissionThematique> getReunionCommissionThematique(
        @PathVariable Long id
    ) {
        log.debug("REST request to get ReunionCommissionThematique : {}", id);
        return Optional
            .ofNullable(reunionCommissionThematiqueService.get(id))
            .map(reunionCommissionThematique ->
                new ResponseEntity<>(reunionCommissionThematique, HttpStatus.OK)
            )
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reunionCommissionThematiques/:id -> delete the "id" reunionCommissionThematique.
     */
    @RequestMapping(
        value = "/reunionCommissionThematiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<Void> deleteReunionCommissionThematique(
        @PathVariable Long id
    ) {
        log.debug(
            "REST request to delete ReunionCommissionThematique : {}",
            id
        );
        reunionCommissionThematiqueRepository.delete(id);
        reunionCommissionThematiqueSearchRepository.delete(id);
        auditTrailService.logDeletion(ReunionCommissionThematique.class, id);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    "reunionCommissionThematique",
                    id.toString()
                )
            )
            .build();
    }

    /**
     * SEARCH  /_search/reunionCommissionThematiques/:query -> search for the reunionCommissionThematique corresponding
     * to the query.
     */
    @RequestMapping(
        value = "/_search/reunionCommissionThematiques/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<ReunionCommissionThematique> searchReunionCommissionThematiques(
        @PathVariable String query
    ) {
        return StreamSupport
            .stream(
                reunionCommissionThematiqueSearchRepository
                    .search(queryStringQuery(query))
                    .spliterator(),
                false
            )
            .collect(Collectors.toList());
    }
}
