package fr.cridf.babylone14166.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.domain.AppartenanceCommissionPermanente;
import fr.cridf.babylone14166.repository.AppartenanceCommissionPermanenteRepository;
import fr.cridf.babylone14166.repository.search.AppartenanceCommissionPermanenteSearchRepository;
import fr.cridf.babylone14166.web.rest.util.HeaderUtil;

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
        return appartenanceCommissionPermanenteRepository.findAll();
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
