package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.NumeroTelephone;
import openassemblee.repository.NumeroTelephoneRepository;
import openassemblee.repository.search.NumeroTelephoneSearchRepository;
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
     * GET  /numeroTelephones/:id -> get the "id" numeroTelephone.
     */
    @RequestMapping(value = "/numeroTelephones/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NumeroTelephone> getNumeroTelephone(@PathVariable Long id) {
        log.debug("REST request to get NumeroTelephone : {}", id);
        return Optional.ofNullable(numeroTelephoneRepository.findOne(id))
            .map(numeroTelephone -> new ResponseEntity<>(
                numeroTelephone,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
