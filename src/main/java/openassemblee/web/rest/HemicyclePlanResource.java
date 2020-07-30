package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.HemicyclePlan;
import openassemblee.repository.HemicyclePlanRepository;
import openassemblee.repository.search.HemicyclePlanSearchRepository;
import openassemblee.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing HemicyclePlan.
 */
@RestController
@RequestMapping("/api")
public class HemicyclePlanResource {

    private final Logger log = LoggerFactory.getLogger(HemicyclePlanResource.class);

    @Inject
    private HemicyclePlanRepository hemicyclePlanRepository;

    @Inject
    private HemicyclePlanSearchRepository hemicyclePlanSearchRepository;

    /**
     * POST  /hemicyclePlans -> Create a new hemicyclePlan.
     */
    @RequestMapping(value = "/hemicyclePlans",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HemicyclePlan> createHemicyclePlan(@Valid @RequestBody HemicyclePlan hemicyclePlan) throws URISyntaxException {
        log.debug("REST request to save HemicyclePlan : {}", hemicyclePlan);
        if (hemicyclePlan.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hemicyclePlan cannot already have an ID").body(null);
        }
        HemicyclePlan result = hemicyclePlanRepository.save(hemicyclePlan);
        hemicyclePlanSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hemicyclePlans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hemicyclePlan", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hemicyclePlans -> Updates an existing hemicyclePlan.
     */
    @RequestMapping(value = "/hemicyclePlans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HemicyclePlan> updateHemicyclePlan(@Valid @RequestBody HemicyclePlan hemicyclePlan) throws URISyntaxException {
        log.debug("REST request to update HemicyclePlan : {}", hemicyclePlan);
        if (hemicyclePlan.getId() == null) {
            return createHemicyclePlan(hemicyclePlan);
        }
        HemicyclePlan result = hemicyclePlanRepository.save(hemicyclePlan);
        hemicyclePlanSearchRepository.save(hemicyclePlan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hemicyclePlan", hemicyclePlan.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hemicyclePlans -> get all the hemicyclePlans.
     */
    @RequestMapping(value = "/hemicyclePlans",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HemicyclePlan> getAllHemicyclePlans() {
        if ("seance-is-null".equals(filter)) {
            log.debug("REST request to get all HemicyclePlans where seance is null");
            return StreamSupport
                .stream(hemicyclePlanRepository.findAll().spliterator(), false)
                .filter(hemicyclePlan -> hemicyclePlan.getSeance() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all HemicyclePlans");
        return hemicyclePlanRepository.findAll();
    }

    /**
     * GET  /hemicyclePlans/:id -> get the "id" hemicyclePlan.
     */
    @RequestMapping(value = "/hemicyclePlans/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HemicyclePlan> getHemicyclePlan(@PathVariable Long id) {
        log.debug("REST request to get HemicyclePlan : {}", id);
        return Optional.ofNullable(hemicyclePlanRepository.findOne(id))
            .map(hemicyclePlan -> new ResponseEntity<>(
                hemicyclePlan,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hemicyclePlans/:id -> delete the "id" hemicyclePlan.
     */
    @RequestMapping(value = "/hemicyclePlans/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHemicyclePlan(@PathVariable Long id) {
        log.debug("REST request to delete HemicyclePlan : {}", id);
        hemicyclePlanRepository.delete(id);
        hemicyclePlanSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hemicyclePlan", id.toString())).build();
    }

    /**
     * SEARCH  /_search/hemicyclePlans/:query -> search for the hemicyclePlan corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/hemicyclePlans/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HemicyclePlan> searchHemicyclePlans(@PathVariable String query) {
        return StreamSupport
            .stream(hemicyclePlanSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
