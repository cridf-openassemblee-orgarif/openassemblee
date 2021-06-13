package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.AutreMandat;
import openassemblee.domain.Mandature;
import openassemblee.repository.AutreMandatRepository;
import openassemblee.repository.search.AutreMandatSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.service.SessionMandatureService;
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
 * REST controller for managing AutreMandat.
 */
@RestController
@RequestMapping("/api")
public class AutreMandatResource {

    private final Logger log = LoggerFactory.getLogger(AutreMandatResource.class);

    @Inject
    private AutreMandatRepository autreMandatRepository;

    @Inject
    private AutreMandatSearchRepository autreMandatSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private SessionMandatureService sessionMandatureService;

    /**
     * POST  /autreMandats -> Create a new autreMandat.
     */
    @RequestMapping(value = "/autreMandats",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AutreMandat> createAutreMandat(@RequestBody AutreMandat autreMandat) throws URISyntaxException {
        log.debug("REST request to save AutreMandat : {}", autreMandat);
        if (autreMandat.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new autreMandat cannot already have an ID").body(null);
        }
        AutreMandat result = autreMandatRepository.save(autreMandat);
        autreMandatSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/autreMandats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("autreMandat", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /autreMandats -> Updates an existing autreMandat.
     */
    @RequestMapping(value = "/autreMandats",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AutreMandat> updateAutreMandat(@RequestBody AutreMandat autreMandat) throws URISyntaxException {
        log.debug("REST request to update AutreMandat : {}", autreMandat);
        if (autreMandat.getId() == null) {
            return createAutreMandat(autreMandat);
        }
        AutreMandat result = autreMandatRepository.save(autreMandat);
        autreMandatSearchRepository.save(autreMandat);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("autreMandat", autreMandat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /autreMandats -> get all the autreMandats.
     */
    @RequestMapping(value = "/autreMandats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AutreMandat> getAllAutreMandats() {
        log.debug("REST request to get all AutreMandats");
        return autreMandatRepository.findByMandature(sessionMandatureService.getMandature());
    }

    /**
     * GET  /autreMandats/:id -> get the "id" autreMandat.
     */
    @RequestMapping(value = "/autreMandats/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AutreMandat> getAutreMandat(@PathVariable Long id) {
        log.debug("REST request to get AutreMandat : {}", id);
        return Optional.ofNullable(autreMandatRepository.findOne(id))
            .map(autreMandat -> new ResponseEntity<>(
                autreMandat,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /autreMandats/:id -> delete the "id" autreMandat.
     */
    @RequestMapping(value = "/autreMandats/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAutreMandat(@PathVariable Long id) {
        log.debug("REST request to delete AutreMandat : {}", id);
        autreMandatRepository.delete(id);
        autreMandatSearchRepository.delete(id);
        auditTrailService.logDeletion(AutreMandat.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("autreMandat", id.toString())).build();
    }

    /**
     * SEARCH  /_search/autreMandats/:query -> search for the autreMandat corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/autreMandats/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AutreMandat> searchAutreMandats(@PathVariable String query) {
        return StreamSupport
            .stream(autreMandatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
