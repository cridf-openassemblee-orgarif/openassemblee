package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.FonctionGroupePolitique;
import openassemblee.repository.FonctionGroupePolitiqueRepository;
import openassemblee.repository.search.FonctionGroupePolitiqueSearchRepository;
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
 * REST controller for managing FonctionGroupePolitique.
 */
@RestController
@RequestMapping("/api")
public class FonctionGroupePolitiqueResource {

    private final Logger log = LoggerFactory.getLogger(FonctionGroupePolitiqueResource.class);

    @Inject
    private FonctionGroupePolitiqueRepository fonctionGroupePolitiqueRepository;

    @Inject
    private FonctionGroupePolitiqueSearchRepository fonctionGroupePolitiqueSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    /**
     * POST  /fonctionGroupePolitiques -> Create a new fonctionGroupePolitique.
     */
    @RequestMapping(value = "/fonctionGroupePolitiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionGroupePolitique> createFonctionGroupePolitique(@RequestBody FonctionGroupePolitique fonctionGroupePolitique) throws URISyntaxException {
        log.debug("REST request to save FonctionGroupePolitique : {}", fonctionGroupePolitique);
        if (fonctionGroupePolitique.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new fonctionGroupePolitique cannot already have an ID").body(null);
        }
        FonctionGroupePolitique result = fonctionGroupePolitiqueRepository.save(fonctionGroupePolitique);
        fonctionGroupePolitiqueSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/fonctionGroupePolitiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fonctionGroupePolitique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fonctionGroupePolitiques -> Updates an existing fonctionGroupePolitique.
     */
    @RequestMapping(value = "/fonctionGroupePolitiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionGroupePolitique> updateFonctionGroupePolitique(@RequestBody FonctionGroupePolitique fonctionGroupePolitique) throws URISyntaxException {
        log.debug("REST request to update FonctionGroupePolitique : {}", fonctionGroupePolitique);
        if (fonctionGroupePolitique.getId() == null) {
            return createFonctionGroupePolitique(fonctionGroupePolitique);
        }
        FonctionGroupePolitique result = fonctionGroupePolitiqueRepository.save(fonctionGroupePolitique);
        fonctionGroupePolitiqueSearchRepository.save(fonctionGroupePolitique);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fonctionGroupePolitique", fonctionGroupePolitique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fonctionGroupePolitiques -> get all the fonctionGroupePolitiques.
     */
    @RequestMapping(value = "/fonctionGroupePolitiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FonctionGroupePolitique> getAllFonctionGroupePolitiques() {
        log.debug("REST request to get all FonctionGroupePolitiques");
        return fonctionGroupePolitiqueRepository.findAll();
    }

    /**
     * GET  /fonctionGroupePolitiques/:id -> get the "id" fonctionGroupePolitique.
     */
    @RequestMapping(value = "/fonctionGroupePolitiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionGroupePolitique> getFonctionGroupePolitique(@PathVariable Long id) {
        log.debug("REST request to get FonctionGroupePolitique : {}", id);
        return Optional.ofNullable(fonctionGroupePolitiqueRepository.findOne(id))
            .map(fonctionGroupePolitique -> new ResponseEntity<>(
                fonctionGroupePolitique,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fonctionGroupePolitiques/:id -> delete the "id" fonctionGroupePolitique.
     */
    @RequestMapping(value = "/fonctionGroupePolitiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFonctionGroupePolitique(@PathVariable Long id) {
        log.debug("REST request to delete FonctionGroupePolitique : {}", id);
        fonctionGroupePolitiqueRepository.delete(id);
        fonctionGroupePolitiqueSearchRepository.delete(id);
        auditTrailService.logDeletion(FonctionGroupePolitique.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fonctionGroupePolitique", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fonctionGroupePolitiques/:query -> search for the fonctionGroupePolitique corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/fonctionGroupePolitiques/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FonctionGroupePolitique> searchFonctionGroupePolitiques(@PathVariable String query) {
        return StreamSupport
            .stream(fonctionGroupePolitiqueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
