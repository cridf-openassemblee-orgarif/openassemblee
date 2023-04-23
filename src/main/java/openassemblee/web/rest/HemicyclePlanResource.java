package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.validation.Valid;
import openassemblee.domain.HemicyclePlan;
import openassemblee.repository.HemicyclePlanRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.service.HemicyclePlanService;
import openassemblee.service.SessionMandatureService;
import openassemblee.web.rest.dto.HemicyclePlanAssociationsDTO;
import openassemblee.web.rest.dto.HemicyclePlanCreationDTO;
import openassemblee.web.rest.dto.HemicyclePlanUpdateDTO;
import openassemblee.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing HemicyclePlan.
 */
@RestController
@RequestMapping("/api")
public class HemicyclePlanResource {

    private final Logger log = LoggerFactory.getLogger(
        HemicyclePlanResource.class
    );

    public static final String hemicyclePlansAssociationsUrl =
        "hemicyclePlans-associations";

    @Inject
    private HemicyclePlanRepository hemicyclePlanRepository;

    @Inject
    private HemicyclePlanService hemicyclePlanService;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private SessionMandatureService sessionMandatureService;

    /**
     * POST  /hemicyclePlans -> Create a new hemicyclePlan.
     */
    @RequestMapping(
        value = "/hemicyclePlans",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<HemicyclePlan> createHemicyclePlan(
        @Valid @RequestBody HemicyclePlanCreationDTO hemicyclePlan
    ) throws URISyntaxException {
        log.debug("REST request to save HemicyclePlan : {}", hemicyclePlan);
        HemicyclePlan result = hemicyclePlanService.save(hemicyclePlan);
        auditTrailService.logCreation(result, result.getId(), hemicyclePlan);
        return ResponseEntity
            .created(new URI("/api/hemicyclePlans/" + result.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    "hemicyclePlan",
                    result.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * PUT  /hemicyclePlans -> Updates an existing hemicyclePlan.
     */
    @RequestMapping(
        value = "/hemicyclePlans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<HemicyclePlan> updateHemicyclePlan(
        @Valid @RequestBody HemicyclePlan hemicyclePlan
    ) throws URISyntaxException {
        log.debug("REST request to update HemicyclePlan : {}", hemicyclePlan);
        // FIXME audit
        if (hemicyclePlan.getId() == null) {
            throw new IllegalArgumentException();
        }
        HemicyclePlan result = hemicyclePlanRepository.save(hemicyclePlan);
        auditTrailService.logUpdate(hemicyclePlan, result.getId());
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    "hemicyclePlan",
                    hemicyclePlan.getId().toString()
                )
            )
            .body(result);
    }

    @RequestMapping(
        value = "/" + hemicyclePlansAssociationsUrl,
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<Void> updateHemicycleAssociations(
        @Valid @RequestBody HemicyclePlanUpdateDTO dto
    ) {
        hemicyclePlanService.update(dto);
        auditTrailService.logUpdate(dto, dto.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /hemicyclePlans -> get all the hemicyclePlans.
     */
    @RequestMapping(
        value = "/hemicyclePlans",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<HemicyclePlan> getAllHemicyclePlans() {
        log.debug("REST request to get all HemicyclePlans");
        return hemicyclePlanRepository.findByMandature(
            sessionMandatureService.getMandature()
        );
    }

    @RequestMapping(
        value = "/hemicyclePlans-projets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<HemicyclePlan> getAllHemicyclePlansProjets() {
        return hemicyclePlanRepository
            .findByMandature(sessionMandatureService.getMandature())
            .stream()
            .filter(p -> p.getSeance() == null)
            .collect(Collectors.toList());
    }

    /**
     * GET  /hemicyclePlans/:id -> get the "id" hemicyclePlan.
     */
    @RequestMapping(
        value = "/hemicyclePlans/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<HemicyclePlan> getHemicyclePlan(
        @PathVariable Long id
    ) {
        log.debug("REST request to get HemicyclePlan : {}", id);
        return Optional
            .ofNullable(hemicyclePlanRepository.findOne(id))
            .map(hemicyclePlan ->
                new ResponseEntity<>(hemicyclePlan, HttpStatus.OK)
            )
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(
        value = "/" + hemicyclePlansAssociationsUrl + "/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<HemicyclePlanAssociationsDTO> getHemicyclePlanAssociations(
        @PathVariable Long id
    ) {
        log.debug("REST request to get HemicyclePlan : {}", id);
        HemicyclePlanAssociationsDTO dto = hemicyclePlanService.get(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * DELETE  /hemicyclePlans/:id -> delete the "id" hemicyclePlan.
     */
    @RequestMapping(
        value = "/hemicyclePlans/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<Void> deleteHemicyclePlan(@PathVariable Long id) {
        log.debug("REST request to delete HemicyclePlan : {}", id);
        hemicyclePlanRepository.delete(id);
        auditTrailService.logDeletion(HemicyclePlan.class, id);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    "hemicyclePlan",
                    id.toString()
                )
            )
            .build();
    }
}
