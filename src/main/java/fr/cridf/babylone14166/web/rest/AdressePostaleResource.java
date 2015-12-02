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

import fr.cridf.babylone14166.domain.AdressePostale;
import fr.cridf.babylone14166.repository.AdressePostaleRepository;
import fr.cridf.babylone14166.repository.search.AdressePostaleSearchRepository;
import fr.cridf.babylone14166.web.rest.util.HeaderUtil;

/**
 * REST controller for managing AdressePostale.
 */
@RestController
@RequestMapping("/api")
public class AdressePostaleResource {

    private final Logger log = LoggerFactory.getLogger(AdressePostaleResource.class);

    @Inject
    private AdressePostaleRepository adressePostaleRepository;

    @Inject
    private AdressePostaleSearchRepository adressePostaleSearchRepository;

    /**
     * POST  /adressePostales -> Create a new adressePostale.
     */
    @RequestMapping(value = "/adressePostales",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdressePostale> createAdressePostale(@RequestBody AdressePostale adressePostale) throws URISyntaxException {
        log.debug("REST request to save AdressePostale : {}", adressePostale);
        if (adressePostale.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new adressePostale cannot already have an ID").body(null);
        }
        AdressePostale result = adressePostaleRepository.save(adressePostale);
        adressePostaleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/adressePostales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("adressePostale", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adressePostales -> Updates an existing adressePostale.
     */
    @RequestMapping(value = "/adressePostales",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdressePostale> updateAdressePostale(@RequestBody AdressePostale adressePostale) throws URISyntaxException {
        log.debug("REST request to update AdressePostale : {}", adressePostale);
        if (adressePostale.getId() == null) {
            return createAdressePostale(adressePostale);
        }
        AdressePostale result = adressePostaleRepository.save(adressePostale);
        adressePostaleSearchRepository.save(adressePostale);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("adressePostale", adressePostale.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adressePostales -> get all the adressePostales.
     */
    @RequestMapping(value = "/adressePostales",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AdressePostale> getAllAdressePostales() {
        log.debug("REST request to get all AdressePostales");
        return adressePostaleRepository.findAll();
    }

    /**
     * GET  /adressePostales/:id -> get the "id" adressePostale.
     */
    @RequestMapping(value = "/adressePostales/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdressePostale> getAdressePostale(@PathVariable Long id) {
        log.debug("REST request to get AdressePostale : {}", id);
        AdressePostale adressePostale = adressePostaleRepository.findOne(id);
        if (adressePostale != null) {
            return new ResponseEntity<>(adressePostale, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETE  /adressePostales/:id -> delete the "id" adressePostale.
     */
    @RequestMapping(value = "/adressePostales/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdressePostale(@PathVariable Long id) {
        log.debug("REST request to delete AdressePostale : {}", id);
        adressePostaleRepository.delete(id);
        adressePostaleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("adressePostale", id.toString())).build();
    }

    /**
     * SEARCH  /_search/adressePostales/:query -> search for the adressePostale corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/adressePostales/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AdressePostale> searchAdressePostales(@PathVariable String query) {
        return Lists.newArrayList(adressePostaleSearchRepository.search(queryStringQuery(query)));
    }
}
