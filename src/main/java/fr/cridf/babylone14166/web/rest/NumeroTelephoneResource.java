package fr.cridf.babylone14166.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.domain.NumeroTelephone;
import fr.cridf.babylone14166.repository.NumeroTelephoneRepository;
import fr.cridf.babylone14166.repository.search.NumeroTelephoneSearchRepository;
import fr.cridf.babylone14166.web.rest.util.HeaderUtil;

/**
 * REST controller for managing NumeroTelephone.
 */
@RestController
@RequestMapping("/api")
public class NumeroTelephoneResource {

    private final Logger log = LoggerFactory.getLogger(NumeroTelephoneResource.class);

    @Inject
    private NumeroTelephoneRepository numeroTelephoneRepository;

    @Inject
    private NumeroTelephoneSearchRepository numeroTelephoneSearchRepository;

    /**
     * POST  /numeroTelephones -> Create a new numeroTelephone.
     */
    @RequestMapping(value = "/numeroTelephones",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NumeroTelephone> createNumeroTelephone(@RequestBody NumeroTelephone numeroTelephone) throws URISyntaxException {
        log.debug("REST request to save NumeroTelephone : {}", numeroTelephone);
        if (numeroTelephone.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new numeroTelephone cannot already have an ID").body(null);
        }
        NumeroTelephone result = numeroTelephoneRepository.save(numeroTelephone);
        numeroTelephoneSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/numeroTelephones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("numeroTelephone", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /numeroTelephones -> Updates an existing numeroTelephone.
     */
    @RequestMapping(value = "/numeroTelephones",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NumeroTelephone> updateNumeroTelephone(@RequestBody NumeroTelephone numeroTelephone) throws URISyntaxException {
        log.debug("REST request to update NumeroTelephone : {}", numeroTelephone);
        if (numeroTelephone.getId() == null) {
            return createNumeroTelephone(numeroTelephone);
        }
        NumeroTelephone result = numeroTelephoneRepository.save(numeroTelephone);
        numeroTelephoneSearchRepository.save(numeroTelephone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("numeroTelephone", numeroTelephone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /numeroTelephones -> get all the numeroTelephones.
     */
    @RequestMapping(value = "/numeroTelephones",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NumeroTelephone> getAllNumeroTelephones() {
        log.debug("REST request to get all NumeroTelephones");
        return numeroTelephoneRepository.findAll();
    }

    /**
     * DELETE  /numeroTelephones/:id -> delete the "id" numeroTelephone.
     */
    @RequestMapping(value = "/numeroTelephones/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNumeroTelephone(@PathVariable Long id) {
        log.debug("REST request to delete NumeroTelephone : {}", id);
        numeroTelephoneRepository.delete(id);
        numeroTelephoneSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("numeroTelephone", id.toString())).build();
    }

}
