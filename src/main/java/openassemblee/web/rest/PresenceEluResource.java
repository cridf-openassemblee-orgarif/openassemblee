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
import openassemblee.domain.PresenceElu;
import openassemblee.domain.Signature;
import openassemblee.repository.PresenceEluRepository;
import openassemblee.repository.search.PresenceEluSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.service.PresenceEluService;
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
 * REST controller for managing PresenceElu.
 */
@RestController
@RequestMapping("/api")
public class PresenceEluResource {

    private final Logger log = LoggerFactory.getLogger(
        PresenceEluResource.class
    );

    @Inject
    private PresenceEluRepository presenceEluRepository;

    @Inject
    private PresenceEluSearchRepository presenceEluSearchRepository;

    @Inject
    private PresenceEluService presenceEluService;

    @Inject
    private AuditTrailService auditTrailService;

    /**
     * POST  /presenceElus -> Create a new presenceElu.
     */
    @RequestMapping(
        value = "/presenceElus",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<PresenceElu> createPresenceElu(
        @RequestBody PresenceElu presenceElu
    ) throws URISyntaxException {
        log.debug("REST request to save PresenceElu : {}", presenceElu);
        if (presenceElu.getId() != null) {
            return ResponseEntity
                .badRequest()
                .header(
                    "Failure",
                    "A new presenceElu cannot already have an ID"
                )
                .body(null);
        }
        PresenceElu result = presenceEluRepository.save(presenceElu);
        presenceEluSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity
            .created(new URI("/api/presenceElus/" + result.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    "presenceElu",
                    result.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * PUT  /presenceElus -> Updates an existing presenceElu.
     */
    @RequestMapping(
        value = "/presenceElus",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<PresenceElu> updatePresenceElu(
        @RequestBody PresenceElu presenceElu
    ) throws URISyntaxException {
        log.debug("REST request to update PresenceElu : {}", presenceElu);
        if (presenceElu.getId() == null) {
            return createPresenceElu(presenceElu);
        }
        PresenceElu result = presenceEluRepository.save(presenceElu);
        presenceEluSearchRepository.save(presenceElu);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    "presenceElu",
                    presenceElu.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * GET  /presenceElus -> get all the presenceElus.
     */
    @RequestMapping(
        value = "/presenceElus",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<List<PresenceElu>> getAllPresenceElus(
        Pageable pageable
    ) throws URISyntaxException {
        Page<PresenceElu> page = presenceEluRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            page,
            "/api/presenceElus"
        );
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /presenceElus/:id -> get the "id" presenceElu.
     */
    @RequestMapping(
        value = "/presenceElus/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<PresenceElu> getPresenceElu(@PathVariable Long id) {
        log.debug("REST request to get PresenceElu : {}", id);
        return Optional
            .ofNullable(presenceEluRepository.findOne(id))
            .map(presenceElu -> new ResponseEntity<>(presenceElu, HttpStatus.OK)
            )
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /presenceElus/:id -> delete the "id" presenceElu.
     */
    @RequestMapping(
        value = "/presenceElus/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<Void> deletePresenceElu(@PathVariable Long id) {
        log.debug("REST request to delete PresenceElu : {}", id);
        presenceEluRepository.delete(id);
        presenceEluSearchRepository.delete(id);
        auditTrailService.logDeletion(PresenceElu.class, id);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    "presenceElu",
                    id.toString()
                )
            )
            .build();
    }

    /**
     * SEARCH  /_search/presenceElus/:query -> search for the presenceElu corresponding
     * to the query.
     */
    @RequestMapping(
        value = "/_search/presenceElus/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<PresenceElu> searchPresenceElus(@PathVariable String query) {
        return StreamSupport
            .stream(
                presenceEluSearchRepository
                    .search(queryStringQuery(query))
                    .spliterator(),
                false
            )
            .collect(Collectors.toList());
    }

    @RequestMapping(
        value = "/presenceElus/{presenceId}/signature",
        method = RequestMethod.POST
    )
    @Timed
    public ResponseEntity<Void> createSignature(
        @PathVariable Long id,
        @RequestBody Signature signature
    ) throws URISyntaxException {
        presenceEluService.saveSignature(id, signature);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
        value = "/presenceElus/{presenceId}/signature",
        method = RequestMethod.PUT
    )
    @Timed
    public ResponseEntity<Void> updateSignature(
        @PathVariable Long presenceId,
        @RequestBody Signature signature
    ) throws URISyntaxException {
        presenceEluService.updateSignature(signature);
        auditTrailService.logUpdate(
            signature,
            signature.getId(),
            PresenceElu.class,
            presenceId
        );
        return ResponseEntity.ok().build();
    }
}
