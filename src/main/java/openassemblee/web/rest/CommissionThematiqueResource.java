package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.CommissionThematique;
import openassemblee.repository.CommissionThematiqueRepository;
import openassemblee.repository.search.CommissionThematiqueSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.service.CommissionThematiqueService;
import openassemblee.service.ExportService;
import openassemblee.service.dto.AppartenanceCommissionThematiqueDTO;
import openassemblee.service.dto.CommissionThematiqueDTO;
import openassemblee.service.dto.CommissionThematiqueListDTO;
import openassemblee.web.rest.util.HeaderUtil;
import org.elasticsearch.common.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing CommissionThematique.
 */
@RestController
@RequestMapping("/api")
public class CommissionThematiqueResource {

    private final Logger log = LoggerFactory.getLogger(CommissionThematiqueResource.class);

    @Inject
    private CommissionThematiqueRepository commissionThematiqueRepository;

    @Inject
    private CommissionThematiqueSearchRepository commissionThematiqueSearchRepository;

    @Inject
    private CommissionThematiqueService commissionThematiqueService;

    @Inject
    private ExportService exportService;

    @Inject
    private AuditTrailService auditTrailService;

    /**
     * POST  /commissionThematiques -> Create a new commissionThematique.
     */
    @RequestMapping(value = "/commissionThematiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommissionThematique> createCommissionThematique(@RequestBody CommissionThematique commissionThematique) throws URISyntaxException {
        log.debug("REST request to save CommissionThematique : {}", commissionThematique);
        if (commissionThematique.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new commissionThematique cannot already have an ID").body(null);
        }
        CommissionThematique result = commissionThematiqueRepository.save(commissionThematique);
        commissionThematiqueSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/commissionThematiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("commissionThematique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commissionThematiques -> Updates an existing commissionThematique.
     */
    @RequestMapping(value = "/commissionThematiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommissionThematique> updateCommissionThematique(@RequestBody CommissionThematique commissionThematique) throws URISyntaxException {
        log.debug("REST request to update CommissionThematique : {}", commissionThematique);
        if (commissionThematique.getId() == null) {
            return createCommissionThematique(commissionThematique);
        }
        CommissionThematique result = commissionThematiqueRepository.save(commissionThematique);
        commissionThematiqueSearchRepository.save(commissionThematique);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("commissionThematique", commissionThematique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commissionThematiques -> get all the commissionThematiques.
     */
    @RequestMapping(value = "/commissionThematiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CommissionThematique> getAllCommissionThematiques() {
        log.debug("REST request to get all CommissionThematiques");
        return commissionThematiqueRepository.findAll();
    }

    /**
     * GET  /commissionThematiques -> get all the commissionThematiques.
     */
    @RequestMapping(value = "/commissionThematiques-dtos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CommissionThematiqueListDTO> getAllCommissionThematiquesDtos() {
        log.debug("REST request to get all CommissionThematiques dtos");
        return commissionThematiqueService.getAll();
    }

    @RequestMapping(value = "/commissionThematiques/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getAllCommissionThematiquesExport(HttpServletResponse response) {
        log.debug("REST request to get all GroupePolitiques");
        List<CommissionThematiqueListDTO> cts = commissionThematiqueService.getAll();
        List<List<String>> lines = new ArrayList<>();
        for (CommissionThematiqueListDTO ctDTO : cts) {
            CommissionThematique ct = ctDTO.getCommissionThematique();
            lines.add(Arrays.asList(ct.getNom(), ct.getNomCourt(), ctDTO.getCount() + " membres"));
        }
        byte[] export = exportService.exportToExcel("Commission thématiques", lines);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "siger-export-commissions-thematiques";
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xlsx");
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e) {
            // TODO exception
            e.printStackTrace();
        }
    }

    /**
     * GET  /commissionThematiques/:id -> get the "id" commissionThematique.
     */
    @RequestMapping(value = "/commissionThematiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommissionThematiqueDTO> getCommissionThematique(@PathVariable Long id) {
        log.debug("REST request to get CommissionThematique : {}", id);
        CommissionThematiqueDTO dto = commissionThematiqueService.get(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * GET  /commissionThematiques/:id -> get the "id" commissionThematique.
     */
    @RequestMapping(value = "/commissionThematiques/{id}/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getCommissionThematiqueExport(@PathVariable Long id, HttpServletResponse response) {
        CommissionThematiqueDTO dto = commissionThematiqueService.get(id);
        List<List<String>> lines = new ArrayList<>();
        CommissionThematique ct = dto.getCommissionThematique();
        lines.add(Arrays.asList(ct.getNom(), ct.getNomCourt()));
        lines.add(new ArrayList<>());
        // FIXME si la CT est fermée ça n'apparait pas là!
        // TODO autre sheet ?
        for (AppartenanceCommissionThematiqueDTO act : dto.getAppartenanceCommissionThematiqueDTOs()) {
            lines.add(Arrays.asList(act.getElu().getNom(), act.getElu().getPrenom()));
        }
        byte[] export = exportService.exportToExcel("Commission thématique", lines);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "siger-export-commission-thematique-" + id;
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xlsx");
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e) {
            // TODO exception
            e.printStackTrace();
        }
    }

    /**
     * DELETE  /commissionThematiques/:id -> delete the "id" commissionThematique.
     */
    @RequestMapping(value = "/commissionThematiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommissionThematique(@PathVariable Long id) {
        log.debug("REST request to delete CommissionThematique : {}", id);
        commissionThematiqueRepository.delete(id);
        commissionThematiqueSearchRepository.delete(id);
        auditTrailService.logDeletion(CommissionThematique.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("commissionThematique", id.toString())).build();
    }

    /**
     * SEARCH  /_search/commissionThematiques/:query -> search for the commissionThematique corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/commissionThematiques/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CommissionThematique> searchCommissionThematiques(@PathVariable String query) {
        return StreamSupport
            .stream(commissionThematiqueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
