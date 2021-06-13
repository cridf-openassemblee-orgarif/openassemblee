package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.FonctionCommissionPermanente;
import openassemblee.domain.Mandature;
import openassemblee.repository.FonctionCommissionPermanenteRepository;
import openassemblee.repository.search.FonctionCommissionPermanenteSearchRepository;
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
 * REST controller for managing FonctionCommissionPermanente.
 */
@RestController
@RequestMapping("/api")
public class FonctionCommissionPermanenteResource {

    private final Logger log = LoggerFactory.getLogger(FonctionCommissionPermanenteResource.class);

    @Inject
    private FonctionCommissionPermanenteRepository fonctionCommissionPermanenteRepository;

    @Inject
    private FonctionCommissionPermanenteSearchRepository fonctionCommissionPermanenteSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private SessionMandatureService sessionMandatureService;

    /**
     * POST  /fonctionCommissionPermanentes -> Create a new fonctionCommissionPermanente.
     */
    @RequestMapping(value = "/fonctionCommissionPermanentes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionCommissionPermanente> createFonctionCommissionPermanente(@RequestBody FonctionCommissionPermanente fonctionCommissionPermanente) throws URISyntaxException {
        log.debug("REST request to save FonctionCommissionPermanente : {}", fonctionCommissionPermanente);
        if (fonctionCommissionPermanente.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new fonctionCommissionPermanente cannot already have an ID").body(null);
        }
        FonctionCommissionPermanente result = fonctionCommissionPermanenteRepository.save(fonctionCommissionPermanente);
        fonctionCommissionPermanenteSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/fonctionCommissionPermanentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fonctionCommissionPermanente", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fonctionCommissionPermanentes -> Updates an existing fonctionCommissionPermanente.
     */
    @RequestMapping(value = "/fonctionCommissionPermanentes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionCommissionPermanente> updateFonctionCommissionPermanente(@RequestBody FonctionCommissionPermanente fonctionCommissionPermanente) throws URISyntaxException {
        log.debug("REST request to update FonctionCommissionPermanente : {}", fonctionCommissionPermanente);
        if (fonctionCommissionPermanente.getId() == null) {
            return createFonctionCommissionPermanente(fonctionCommissionPermanente);
        }
        FonctionCommissionPermanente result = fonctionCommissionPermanenteRepository.save(fonctionCommissionPermanente);
        fonctionCommissionPermanenteSearchRepository.save(fonctionCommissionPermanente);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fonctionCommissionPermanente", fonctionCommissionPermanente.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fonctionCommissionPermanentes -> get all the fonctionCommissionPermanentes.
     */
    @RequestMapping(value = "/fonctionCommissionPermanentes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FonctionCommissionPermanente> getAllFonctionCommissionPermanentes() {
        log.debug("REST request to get all FonctionCommissionPermanentes");
        return fonctionCommissionPermanenteRepository.findByMandature(sessionMandatureService.getMandature());
    }

    /**
     * GET  /fonctionCommissionPermanentes/:id -> get the "id" fonctionCommissionPermanente.
     */
    @RequestMapping(value = "/fonctionCommissionPermanentes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FonctionCommissionPermanente> getFonctionCommissionPermanente(@PathVariable Long id) {
        log.debug("REST request to get FonctionCommissionPermanente : {}", id);
        FonctionCommissionPermanente fonctionCommissionPermanente = fonctionCommissionPermanenteRepository.findOne(id);
        if (fonctionCommissionPermanente != null) {
            return new ResponseEntity<>(fonctionCommissionPermanente, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETE  /fonctionCommissionPermanentes/:id -> delete the "id" fonctionCommissionPermanente.
     */
    @RequestMapping(value = "/fonctionCommissionPermanentes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFonctionCommissionPermanente(@PathVariable Long id) {
        log.debug("REST request to delete FonctionCommissionPermanente : {}", id);
        fonctionCommissionPermanenteRepository.delete(id);
        fonctionCommissionPermanenteSearchRepository.delete(id);
        auditTrailService.logDeletion(FonctionCommissionPermanente.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fonctionCommissionPermanente", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fonctionCommissionPermanentes/:query -> search for the fonctionCommissionPermanente corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/fonctionCommissionPermanentes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FonctionCommissionPermanente> searchFonctionCommissionPermanentes(@PathVariable String query) {
        return Lists.newArrayList(fonctionCommissionPermanenteSearchRepository.search(queryStringQuery(query)));
    }
}
