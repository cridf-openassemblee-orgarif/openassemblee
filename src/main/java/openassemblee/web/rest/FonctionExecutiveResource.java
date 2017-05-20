package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.FonctionExecutive;
import openassemblee.repository.FonctionExecutiveRepository;
import openassemblee.repository.search.FonctionExecutiveSearchRepository;
import openassemblee.service.AuditTrailService;
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
 * REST controller for managing FonctionExecutive.
 */
@RestController
@RequestMapping("/api")
public class FonctionExecutiveResource {

    private final Logger log = LoggerFactory.getLogger(FonctionExecutiveResource.class);

    @Inject
    private FonctionExecutiveRepository fonctionExecutiveRepository;

    @Inject
    private FonctionExecutiveSearchRepository fonctionExecutiveSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    /**
     * POST  /fonctionExecutives -> Create a new fonctionExecutive.
     */
    @RequestMapping(value = "/fonctionExecutives",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionExecutive> createFonctionExecutive(@RequestBody FonctionExecutive fonctionExecutive) throws URISyntaxException {
        log.debug("REST request to save FonctionExecutive : {}", fonctionExecutive);
        if (fonctionExecutive.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new fonctionExecutive cannot already have an ID").body(null);
        }
        FonctionExecutive result = fonctionExecutiveRepository.save(fonctionExecutive);
        fonctionExecutiveSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/fonctionExecutives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fonctionExecutive", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fonctionExecutives -> Updates an existing fonctionExecutive.
     */
    @RequestMapping(value = "/fonctionExecutives",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionExecutive> updateFonctionExecutive(@RequestBody FonctionExecutive fonctionExecutive) throws URISyntaxException {
        log.debug("REST request to update FonctionExecutive : {}", fonctionExecutive);
        if (fonctionExecutive.getId() == null) {
            return createFonctionExecutive(fonctionExecutive);
        }
        FonctionExecutive result = fonctionExecutiveRepository.save(fonctionExecutive);
        fonctionExecutiveSearchRepository.save(fonctionExecutive);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fonctionExecutive", fonctionExecutive.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fonctionExecutives -> get all the fonctionExecutives.
     */
    @RequestMapping(value = "/fonctionExecutives",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FonctionExecutive> getAllFonctionExecutives() {
        log.debug("REST request to get all FonctionExecutives");
        return fonctionExecutiveRepository.findAll();
    }

    /**
     * GET  /fonctionExecutives/:id -> get the "id" fonctionExecutive.
     */
    @RequestMapping(value = "/fonctionExecutives/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionExecutive> getFonctionExecutive(@PathVariable Long id) {
        log.debug("REST request to get FonctionExecutive : {}", id);
        FonctionExecutive f = fonctionExecutiveRepository.findOne(id);
        if (f != null) {
            return new ResponseEntity<>(f, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETE  /fonctionExecutives/:id -> delete the "id" fonctionExecutive.
     */
    @RequestMapping(value = "/fonctionExecutives/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFonctionExecutive(@PathVariable Long id) {
        log.debug("REST request to delete FonctionExecutive : {}", id);
        fonctionExecutiveRepository.delete(id);
        fonctionExecutiveSearchRepository.delete(id);
        auditTrailService.logDeletion(FonctionExecutive.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fonctionExecutive", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fonctionExecutives/:query -> search for the fonctionExecutive corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/fonctionExecutives/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FonctionExecutive> searchFonctionExecutives(@PathVariable String query) {
        return Lists.newArrayList(fonctionExecutiveSearchRepository.search(queryStringQuery(query)));
    }
}
