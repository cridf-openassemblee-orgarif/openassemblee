package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.util.Optional;
import javax.inject.Inject;
import openassemblee.domain.NumeroFax;
import openassemblee.repository.NumeroFaxRepository;
import openassemblee.repository.search.NumeroFaxSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing NumeroFax.
 */
@RestController
@RequestMapping("/api")
public class NumeroFaxResource {

    private final Logger log = LoggerFactory.getLogger(NumeroFaxResource.class);

    @Inject
    private NumeroFaxRepository numeroFaxRepository;

    @Inject
    private NumeroFaxSearchRepository numeroFaxSearchRepository;

    /**
     * GET  /numeroFaxs/:id -> get the "id" numeroFax.
     */
    @RequestMapping(
        value = "/numeroFaxs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<NumeroFax> getNumeroFax(@PathVariable Long id) {
        log.debug("REST request to get NumeroFax : {}", id);
        return Optional
            .ofNullable(numeroFaxRepository.findOne(id))
            .map(numeroFax -> new ResponseEntity<>(numeroFax, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
