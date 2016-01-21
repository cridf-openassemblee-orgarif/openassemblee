package fr.cridf.babylone14166.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.repository.EluRepository;
import fr.cridf.babylone14166.repository.search.EluSearchRepository;
import fr.cridf.babylone14166.service.EluService;
import fr.cridf.babylone14166.service.ImageService;
import fr.cridf.babylone14166.service.dto.EluDTO;
import fr.cridf.babylone14166.service.dto.EluListDTO;
import fr.cridf.babylone14166.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Elu.
 */
@RestController
@RequestMapping("/api")
public class EluResource {

    private final Logger log = LoggerFactory.getLogger(EluResource.class);

    @Inject
    private EluRepository eluRepository;

    @Inject
    private EluService eluService;

    @Inject
    private EluSearchRepository eluSearchRepository;

    @Inject
    private ImageService imageService;

    /**
     * POST  /elus -> Create a new elu.
     */
    @RequestMapping(value = "/elus",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Elu> createElu(@RequestBody Elu elu) throws URISyntaxException {
        log.debug("REST request to save Elu : {}", elu);
        if (elu.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new elu cannot already have an ID").body(null);
        }
        eluRepository.save(elu);
        eluSearchRepository.save(elu);
        return ResponseEntity.created(new URI("/api/elus/" + elu.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("elu", elu.getId().toString()))
            .body(elu);
    }

    @RequestMapping(value = "/elus/{id}/adressePostale", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> createAdressePostale(@PathVariable Long id, @RequestBody AdressePostale adressePostale)
        throws URISyntaxException {
        eluService.saveAdressePostale(id, adressePostale);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{id}/adresseMail", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> createAdresseMail(@PathVariable Long id, @RequestBody AdresseMail adresseMail)
        throws URISyntaxException {
        eluService.saveAdresseMail(id, adresseMail);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{id}/identiteInternet", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> createIdentiteInternet(@PathVariable Long id, @RequestBody IdentiteInternet
        identiteInternet)
        throws URISyntaxException {
        eluService.saveIdentiteInternet(id, identiteInternet);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{id}/numeroFax", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> createNumeroFax(@PathVariable Long id, @RequestBody NumeroFax numeroFax)
        throws URISyntaxException {
        eluService.saveNumeroFax(id, numeroFax);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{id}/numeroTelephone", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> createNumeroTelephone(@PathVariable Long id, @RequestBody NumeroTelephone
        numeroTelephone)
        throws URISyntaxException {
        eluService.saveNumeroTelephone(id, numeroTelephone);
        return ResponseEntity.ok().build();
    }

    /**
     * POST  /elus/:id/photo -> Upload une photo
     */
    @RequestMapping(value = "/elus/{id}/image", method = RequestMethod.POST, consumes = "multipart/form-data")
    @Timed
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestBody MultipartFile file) throws
        URISyntaxException {
        log.debug("REST upload image");
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            imageService.saveImagePourElu(id, new Image(file.getContentType(), file.getBytes()));
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            log.error("Unable to read uploaded image", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {
            log.error("Unable to write uploaded image", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * PUT  /elus -> Updates an existing elu.
     */
    // TODO if adresse n'a pas d'id...
    @RequestMapping(value = "/elus",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Elu> updateElu(@RequestBody Elu elu) throws URISyntaxException {
        log.debug("REST request to update Elu : {}", elu);
        if (elu.getId() == null) {
            return createElu(elu);
        }
        Elu result = eluRepository.save(elu);
        eluSearchRepository.save(elu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("elu", elu.getId().toString()))
            .body(result);
    }

    /**
     * GET  /elus -> get all the elus.
     */
    @RequestMapping(value = "/elus",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EluListDTO> getAllElus() {
        log.debug("REST request to get all Elus");
        return eluService.getAll();
    }

    /**
     * GET  /elus/:id -> get the "id" elu.
     */
    @RequestMapping(value = "/elus/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    // TODO tester jsonview annotations + doc
    @Timed
    public ResponseEntity<EluDTO> getElu(@PathVariable Long id) {
        log.debug("REST request to get Elu : {}", id);
        EluDTO eluDTO = eluService.get(id);
        if (eluDTO != null) {
            return new ResponseEntity<>(eluDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETE  /elus/:id -> delete the "id" elu.
     */
    @RequestMapping(value = "/elus/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteElu(@PathVariable Long id) {
        log.debug("REST request to delete Elu : {}", id);
        eluRepository.delete(id);
        eluSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("elu", id.toString())).build();
    }

    /**
     * SEARCH  /_search/elus/:query -> search for the elu corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/elus/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Elu> searchElus(@PathVariable String query) {
        return Lists.newArrayList(eluSearchRepository.search(queryStringQuery(query)));
    }
}
