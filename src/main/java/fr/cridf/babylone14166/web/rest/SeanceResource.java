package fr.cridf.babylone14166.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cridf.babylone14166.domain.Seance;
import fr.cridf.babylone14166.repository.SeanceRepository;
import fr.cridf.babylone14166.repository.search.SeanceSearchRepository;
import fr.cridf.babylone14166.service.AuditTrailService;
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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Seance.
 */
@RestController
@RequestMapping("/api")
public class SeanceResource {

    private final Logger log = LoggerFactory.getLogger(SeanceResource.class);

    @Inject
    private SeanceRepository seanceRepository;

    @Inject
    private SeanceSearchRepository seanceSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    /**
     * POST  /seances -> Create a new seance.
     */
    @RequestMapping(value = "/seances",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Seance> createSeance(@RequestBody Seance seance) throws URISyntaxException {
        log.debug("REST request to save Seance : {}", seance);
        if (seance.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new seance cannot already have an ID").body(null);
        }
        Seance result = seanceRepository.save(seance);
        seanceSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/seances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("seance", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /seances -> Updates an existing seance.
     */
    @RequestMapping(value = "/seances",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Seance> updateSeance(@RequestBody Seance seance) throws URISyntaxException {
        log.debug("REST request to update Seance : {}", seance);
        if (seance.getId() == null) {
            return createSeance(seance);
        }
        Seance result = seanceRepository.save(seance);
        seanceSearchRepository.save(seance);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("seance", seance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /seances -> get all the seances.
     */
    @RequestMapping(value = "/seances",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Seance>> getAllSeances(Pageable pageable)
        throws URISyntaxException {
        Page<Seance> page = seanceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/seances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /seances/:id -> get the "id" seance.
     */
    @RequestMapping(value = "/seances/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Seance> getSeance(@PathVariable Long id) {
        log.debug("REST request to get Seance : {}", id);
        return Optional.ofNullable(seanceRepository.findOne(id))
            .map(seance -> new ResponseEntity<>(
                seance,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /seances/:id -> delete the "id" seance.
     */
    @RequestMapping(value = "/seances/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) {
        log.debug("REST request to delete Seance : {}", id);
        seanceRepository.delete(id);
        seanceSearchRepository.delete(id);
        auditTrailService.logDeletion(Seance.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("seance", id.toString())).build();
    }

    /**
     * SEARCH  /_search/seances/:query -> search for the seance corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/seances/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Seance> searchSeances(@PathVariable String query) {
        return StreamSupport
            .stream(seanceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
