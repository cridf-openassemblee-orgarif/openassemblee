package fr.cridf.babylone14166.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.domain.Organisme;
import fr.cridf.babylone14166.repository.OrganismeRepository;
import fr.cridf.babylone14166.repository.search.OrganismeSearchRepository;
import fr.cridf.babylone14166.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Organisme.
 */
@RestController
@RequestMapping("/api")
public class OrganismeResource {

    private final Logger log = LoggerFactory.getLogger(OrganismeResource.class);

    @Inject
    private OrganismeRepository organismeRepository;

    @Inject
    private OrganismeSearchRepository organismeSearchRepository;

    /**
     * POST  /organismes -> Create a new organisme.
     */
    @RequestMapping(value = "/organismes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organisme> createOrganisme(@RequestBody Organisme organisme) throws URISyntaxException {
        log.debug("REST request to save Organisme : {}", organisme);
        if (organisme.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new organisme cannot already have an ID").body(null);
        }
        Organisme result = organismeRepository.save(organisme);
        organismeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/organismes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("organisme", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /organismes -> Updates an existing organisme.
     */
    @RequestMapping(value = "/organismes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organisme> updateOrganisme(@RequestBody Organisme organisme) throws URISyntaxException {
        log.debug("REST request to update Organisme : {}", organisme);
        if (organisme.getId() == null) {
            return createOrganisme(organisme);
        }
        Organisme result = organismeRepository.save(organisme);
        organismeSearchRepository.save(organisme);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("organisme", organisme.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organismes -> get all the organismes.
     */
    @RequestMapping(value = "/organismes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Organisme> getAllOrganismes() {
        log.debug("REST request to get all Organismes");
        return organismeRepository.findAll();
    }

    /**
     * GET  /organismes/:id -> get the "id" organisme.
     */
    @RequestMapping(value = "/organismes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organisme> getOrganisme(@PathVariable Long id) {
        log.debug("REST request to get Organisme : {}", id);
        return Optional.ofNullable(organismeRepository.findOne(id))
            .map(organisme -> new ResponseEntity<>(
                organisme,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /organismes/:id -> delete the "id" organisme.
     */
    @RequestMapping(value = "/organismes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrganisme(@PathVariable Long id) {
        log.debug("REST request to delete Organisme : {}", id);
        organismeRepository.delete(id);
        organismeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("organisme", id.toString())).build();
    }

    /**
     * SEARCH  /_search/organismes/:query -> search for the organisme corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/organismes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Organisme> searchOrganismes(@PathVariable String query) {
        return StreamSupport
            .stream(organismeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
