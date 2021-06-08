package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.ListeElectorale;
import openassemblee.repository.ListeElectoraleRepository;
import openassemblee.repository.search.ListeElectoraleSearchRepository;
import openassemblee.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ListeElectorale.
 */
@RestController
@RequestMapping("/api")
public class ListeElectoraleResource {

    private final Logger log = LoggerFactory.getLogger(ListeElectoraleResource.class);

    @Inject
    private ListeElectoraleRepository listeElectoraleRepository;

    @Inject
    private ListeElectoraleSearchRepository listeElectoraleSearchRepository;

    /**
     * POST  /listeElectorales -> Create a new listeElectorale.
     */
    @RequestMapping(value = "/listeElectorales",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ListeElectorale> createListeElectorale(@RequestBody ListeElectorale listeElectorale) throws URISyntaxException {
        log.debug("REST request to save ListeElectorale : {}", listeElectorale);
        if (listeElectorale.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new listeElectorale cannot already have an ID").body(null);
        }
        ListeElectorale result = listeElectoraleRepository.save(listeElectorale);
        listeElectoraleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/listeElectorales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("listeElectorale", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /listeElectorales -> Updates an existing listeElectorale.
     */
    @RequestMapping(value = "/listeElectorales",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ListeElectorale> updateListeElectorale(@RequestBody ListeElectorale listeElectorale) throws URISyntaxException {
        log.debug("REST request to update ListeElectorale : {}", listeElectorale);
        if (listeElectorale.getId() == null) {
            return createListeElectorale(listeElectorale);
        }
        ListeElectorale result = listeElectoraleRepository.save(listeElectorale);
        listeElectoraleSearchRepository.save(listeElectorale);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("listeElectorale", listeElectorale.getId().toString()))
            .body(result);
    }

    /**
     * GET  /listeElectorales -> get all the listeElectorales.
     */
    @RequestMapping(value = "/listeElectorales",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ListeElectorale> getAllListeElectorales() {
        log.debug("REST request to get all ListeElectorales");
        return listeElectoraleRepository.findAll();
    }

    /**
     * GET  /listeElectorales/:id -> get the "id" listeElectorale.
     */
    @RequestMapping(value = "/listeElectorales/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ListeElectorale> getListeElectorale(@PathVariable Long id) {
        log.debug("REST request to get ListeElectorale : {}", id);
        return Optional.ofNullable(listeElectoraleRepository.findOne(id))
            .map(listeElectorale -> new ResponseEntity<>(
                listeElectorale,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /listeElectorales/:id -> delete the "id" listeElectorale.
     */
    @RequestMapping(value = "/listeElectorales/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteListeElectorale(@PathVariable Long id) {
        log.debug("REST request to delete ListeElectorale : {}", id);
        listeElectoraleRepository.delete(id);
        listeElectoraleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("listeElectorale", id.toString())).build();
    }

    /**
     * SEARCH  /_search/listeElectorales/:query -> search for the listeElectorale corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/listeElectorales/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ListeElectorale> searchListeElectorales(@PathVariable String query) {
        return StreamSupport
            .stream(listeElectoraleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
