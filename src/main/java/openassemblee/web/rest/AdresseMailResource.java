package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.AdresseMail;
import openassemblee.repository.AdresseMailRepository;
import openassemblee.repository.search.AdresseMailSearchRepository;
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
 * REST controller for managing AdresseMail.
 */
@RestController
@RequestMapping("/api")
public class AdresseMailResource {

    private final Logger log = LoggerFactory.getLogger(AdresseMailResource.class);

    @Inject
    private AdresseMailRepository adresseMailRepository;

    @Inject
    private AdresseMailSearchRepository adresseMailSearchRepository;

    /**
     * GET  /adresseMails/:id -> get the "id" adresseMail.
     */
    @RequestMapping(value = "/adresseMails/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdresseMail> getAdresseMail(@PathVariable Long id) {
        log.debug("REST request to get AdresseMail : {}", id);
        return Optional.ofNullable(adresseMailRepository.findOne(id))
            .map(adresseMail -> new ResponseEntity<>(
                adresseMail,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
