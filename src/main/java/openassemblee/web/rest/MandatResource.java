package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.Mandat;
import openassemblee.repository.MandatRepository;
import openassemblee.repository.search.MandatSearchRepository;
import openassemblee.service.MandatService;
import openassemblee.service.dto.MandatEditionDTO;
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
 * REST controller for managing Mandat.
 */
@RestController
@RequestMapping("/api")
public class MandatResource {

    private final Logger log = LoggerFactory.getLogger(MandatResource.class);

    @Inject
    private MandatRepository mandatRepository;

    @Inject
    private MandatSearchRepository mandatSearchRepository;

    @Inject
    private MandatService mandatService;

    /**
     * POST  /mandats -> Create a new mandat.
     */
    @RequestMapping(value = "/mandats",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mandat> createMandat(@RequestBody MandatEditionDTO dto) throws URISyntaxException {
        log.debug("REST request to save Mandat : {}", dto);
        if (dto.getMandat().getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new mandat cannot already have an ID").body(null);
        }
        Mandat result = mandatService.save(dto);
        return ResponseEntity.created(new URI("/api/mandats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mandat", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mandats -> Updates an existing mandat.
     */
    @RequestMapping(value = "/mandats",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mandat> updateMandat(@RequestBody MandatEditionDTO dto) throws URISyntaxException {
        log.debug("REST request to update Mandat : {}", dto);
        if (dto.getMandat().getId() == null) {
           throw new IllegalArgumentException(dto.getMandat().getId().toString());
        }
        Mandat result = mandatService.save(dto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mandat", result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mandats -> get all the mandats.
     */
    @RequestMapping(value = "/mandats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Mandat> getAllMandats() {
        log.debug("REST request to get all Mandats");
        return mandatRepository.findAll();
    }

    /**
     * GET  /mandats/:id -> get the "id" mandat.
     */
    @RequestMapping(value = "/mandats/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mandat> getMandat(@PathVariable Long id) {
        log.debug("REST request to get Mandat : {}", id);
        return Optional.ofNullable(mandatRepository.findOne(id))
            .map(mandat -> new ResponseEntity<>(
                mandat,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mandats/:id -> delete the "id" mandat.
     */
    @RequestMapping(value = "/mandats/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMandat(@PathVariable Long id) {
        log.debug("REST request to delete Mandat : {}", id);
        mandatRepository.delete(id);
        mandatSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mandat", id.toString())).build();
    }

    /**
     * SEARCH  /_search/mandats/:query -> search for the mandat corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/mandats/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Mandat> searchMandats(@PathVariable String query) {
        return StreamSupport
            .stream(mandatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
