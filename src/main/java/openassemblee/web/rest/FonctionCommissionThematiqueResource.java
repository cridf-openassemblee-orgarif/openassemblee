package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.FonctionCommissionThematique;
import openassemblee.repository.FonctionCommissionThematiqueRepository;
import openassemblee.repository.search.FonctionCommissionThematiqueSearchRepository;
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
 * REST controller for managing FonctionCommissionThematique.
 */
@RestController
@RequestMapping("/api")
public class FonctionCommissionThematiqueResource {

    private final Logger log = LoggerFactory.getLogger(FonctionCommissionThematiqueResource.class);

    @Inject
    private FonctionCommissionThematiqueRepository fonctionCommissionThematiqueRepository;

    @Inject
    private FonctionCommissionThematiqueSearchRepository fonctionCommissionThematiqueSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    /**
     * POST  /fonctionCommissionThematiques -> Create a new fonctionCommissionThematique.
     */
    @RequestMapping(value = "/fonctionCommissionThematiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionCommissionThematique> createFonctionCommissionThematique(@RequestBody FonctionCommissionThematique fonctionCommissionThematique) throws URISyntaxException {
        log.debug("REST request to save FonctionCommissionThematique : {}", fonctionCommissionThematique);
        if (fonctionCommissionThematique.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new fonctionCommissionThematique cannot already have an ID").body(null);
        }
        FonctionCommissionThematique result = fonctionCommissionThematiqueRepository.save(fonctionCommissionThematique);
        fonctionCommissionThematiqueSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/fonctionCommissionThematiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fonctionCommissionThematique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fonctionCommissionThematiques -> Updates an existing fonctionCommissionThematique.
     */
    @RequestMapping(value = "/fonctionCommissionThematiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionCommissionThematique> updateFonctionCommissionThematique(@RequestBody FonctionCommissionThematique fonctionCommissionThematique) throws URISyntaxException {
        log.debug("REST request to update FonctionCommissionThematique : {}", fonctionCommissionThematique);
        if (fonctionCommissionThematique.getId() == null) {
            return createFonctionCommissionThematique(fonctionCommissionThematique);
        }
        FonctionCommissionThematique result = fonctionCommissionThematiqueRepository.save(fonctionCommissionThematique);
        fonctionCommissionThematiqueSearchRepository.save(fonctionCommissionThematique);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fonctionCommissionThematique", fonctionCommissionThematique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fonctionCommissionThematiques -> get all the fonctionCommissionThematiques.
     */
    @RequestMapping(value = "/fonctionCommissionThematiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FonctionCommissionThematique> getAllFonctionCommissionThematiques() {
        log.debug("REST request to get all FonctionCommissionThematiques");
        return fonctionCommissionThematiqueRepository.findAll();
    }

    /**
     * GET  /fonctionCommissionThematiques/:id -> get the "id" fonctionCommissionThematique.
     */
    @RequestMapping(value = "/fonctionCommissionThematiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionCommissionThematique> getFonctionCommissionThematique(@PathVariable Long id) {
        log.debug("REST request to get FonctionCommissionThematique : {}", id);
        return Optional.ofNullable(fonctionCommissionThematiqueRepository.findOne(id))
            .map(fonctionCommissionThematique -> new ResponseEntity<>(
                fonctionCommissionThematique,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fonctionCommissionThematiques/:id -> delete the "id" fonctionCommissionThematique.
     */
    @RequestMapping(value = "/fonctionCommissionThematiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFonctionCommissionThematique(@PathVariable Long id) {
        log.debug("REST request to delete FonctionCommissionThematique : {}", id);
        fonctionCommissionThematiqueRepository.delete(id);
        fonctionCommissionThematiqueSearchRepository.delete(id);
        auditTrailService.logDeletion(FonctionCommissionThematique.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fonctionCommissionThematique", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fonctionCommissionThematiques/:query -> search for the fonctionCommissionThematique corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/fonctionCommissionThematiques/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FonctionCommissionThematique> searchFonctionCommissionThematiques(@PathVariable String query) {
        return StreamSupport
            .stream(fonctionCommissionThematiqueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
