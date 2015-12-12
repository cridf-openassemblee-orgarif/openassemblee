package fr.cridf.babylone14166.web.rest;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.service.EluService;
import fr.cridf.babylone14166.service.dto.EluCompletDTO;

@RestController
@RequestMapping("/api")
public class EluCompletResource {

    private final Logger log = LoggerFactory.getLogger(EluCompletResource.class);

    @Inject
    private EluService eluService;

    /**
     * GET  /elus/:id -> get the "id" elu.
     */
    @RequestMapping(value = "/elu-complet/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    // TODO tester jsonview annotations + doc
    @Timed
    public ResponseEntity<EluCompletDTO> getElu(@PathVariable Long id) {
        log.debug("REST request to get Elu complet : {}", id);
        EluCompletDTO eluCompletDTO = eluService.getEluComplet(id);
        if (eluCompletDTO != null) {
            return new ResponseEntity<>(eluCompletDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
