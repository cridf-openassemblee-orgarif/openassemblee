package fr.cridf.babylone14166.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cridf.babylone14166.domain.IdentiteInternet;
import fr.cridf.babylone14166.repository.IdentiteInternetRepository;
import fr.cridf.babylone14166.repository.search.IdentiteInternetSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Optional;

/**
 * REST controller for managing IdentiteInternet.
 */
@RestController
@RequestMapping("/api")
public class IdentiteInternetResource {

    private final Logger log = LoggerFactory.getLogger(IdentiteInternetResource.class);

    @Inject
    private IdentiteInternetRepository identiteInternetRepository;

    @Inject
    private IdentiteInternetSearchRepository identiteInternetSearchRepository;

    /**
     * GET  /identiteInternets/:id -> get the "id" identiteInternet.
     */
    @RequestMapping(value = "/identiteInternets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IdentiteInternet> getIdentiteInternet(@PathVariable Long id) {
        log.debug("REST request to get IdentiteInternet : {}", id);
        return Optional.ofNullable(identiteInternetRepository.findOne(id))
            .map(identiteInternet -> new ResponseEntity<>(
                identiteInternet,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
