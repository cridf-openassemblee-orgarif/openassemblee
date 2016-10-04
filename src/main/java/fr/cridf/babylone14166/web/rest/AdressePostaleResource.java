package fr.cridf.babylone14166.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cridf.babylone14166.domain.AdressePostale;
import fr.cridf.babylone14166.repository.AdressePostaleRepository;
import fr.cridf.babylone14166.repository.search.AdressePostaleSearchRepository;
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
     * GET  /adressePostales/:id -> get the "id" adressePostale.
     */
    @RequestMapping(value = "/adressePostales/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdressePostale> getAdressePostale(@PathVariable Long id) {
        log.debug("REST request to get AdressePostale : {}", id);
        return Optional.ofNullable(adressePostaleRepository.findOne(id))
            .map(adressePostale -> new ResponseEntity<>(
                adressePostale,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
