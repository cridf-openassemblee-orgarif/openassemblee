package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.Pouvoir;
import openassemblee.domain.Seance;
import openassemblee.repository.PouvoirRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.web.rest.dto.PouvoirCloseOrderDTO;
import openassemblee.web.rest.util.HeaderUtil;
import openassemblee.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

/**
 * REST controller for managing Pouvoir.
 */
@RestController
@RequestMapping("/api")
public class PouvoirResource {

    private final Logger log = LoggerFactory.getLogger(PouvoirResource.class);

    @Inject
    private PouvoirRepository pouvoirRepository;

    @Inject
    private AuditTrailService auditTrailService;

    /**
     * POST  /pouvoirs -> Create a new pouvoir.
     */
    @RequestMapping(value = "/pouvoirs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pouvoir> createPouvoir(@RequestBody Pouvoir pouvoir) throws URISyntaxException {
        log.debug("REST request to save Pouvoir : {}", pouvoir);
        if (pouvoir.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new pouvoir cannot already have an ID").body(null);
        }
        Pouvoir result = pouvoirRepository.save(pouvoir);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/pouvoirs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pouvoir", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pouvoirs -> Updates an existing pouvoir.
     */
    @RequestMapping(value = "/pouvoirs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pouvoir> updatePouvoir(@RequestBody Pouvoir pouvoir) throws URISyntaxException {
        log.debug("REST request to update Pouvoir : {}", pouvoir);
        if (pouvoir.getId() == null) {
            return createPouvoir(pouvoir);
        }
        Pouvoir result = pouvoirRepository.save(pouvoir);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pouvoir", pouvoir.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pouvoirs -> get all the pouvoirs.
     */
    @RequestMapping(value = "/pouvoirs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Pouvoir>> getAllPouvoirs(Pageable pageable)
        throws URISyntaxException {
        Page<Pouvoir> page = pouvoirRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pouvoirs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pouvoirs -> get all the pouvoirs sans date de fin.
     */
    @RequestMapping(value = "/pouvoirs/open/{seanceId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pouvoir> getAllOpenPouvoirs(@PathVariable Long seanceId) throws URISyntaxException {
        Seance s = new Seance();
        s.setId(seanceId);
        return pouvoirRepository.findAllBySeanceAndDateFinAndHeureFin(s, null, null);
    }

    /**
     * GET  /pouvoirs/:id -> get the "id" pouvoir.
     */
    @RequestMapping(value = "/pouvoirs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pouvoir> getPouvoir(@PathVariable Long id) {
        log.debug("REST request to get Pouvoir : {}", id);
        return Optional.ofNullable(pouvoirRepository.findOne(id))
            .map(pouvoir -> new ResponseEntity<>(
                pouvoir,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/pouvoirs/close-all",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void closeAll(@RequestBody PouvoirCloseOrderDTO dto) {
        Seance s = new Seance();
        s.setId(dto.getSeanceId());
        pouvoirRepository.findAllBySeanceAndDateFinAndHeureFin(s, null, null)
            .forEach(p -> {
                p.setDateFin(dto.getDateFin());
                p.setHeureFin(dto.getHeureFin());
                pouvoirRepository.save(p);
            });
    }

    /**
     * DELETE  /pouvoirs/:id -> delete the "id" pouvoir.
     */
    @RequestMapping(value = "/pouvoirs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePouvoir(@PathVariable Long id) {
        log.debug("REST request to delete Pouvoir : {}", id);
        pouvoirRepository.delete(id);
        auditTrailService.logDeletion(Pouvoir.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pouvoir", id.toString())).build();
    }

}
