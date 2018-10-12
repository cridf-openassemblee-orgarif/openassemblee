package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itextpdf.text.DocumentException;
import openassemblee.domain.*;
import openassemblee.repository.SeanceRepository;
import openassemblee.repository.search.SeanceSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.service.ExportService;
import openassemblee.service.PdfService;
import openassemblee.service.SeanceService;
import openassemblee.service.dto.SeanceDTO;
import openassemblee.web.rest.util.HeaderUtil;
import openassemblee.web.rest.util.PaginationUtil;
import org.elasticsearch.common.io.Streams;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Seance.
 */
@RestController
@RequestMapping("/api")
public class SeanceResource {

    private final Logger log = LoggerFactory.getLogger(SeanceResource.class);

    @Inject
    private SeanceRepository seanceRepository;

    @Inject
    private SeanceService seanceService;

    @Inject
    private SeanceSearchRepository seanceSearchRepository;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private ExportService exportService;

    @Inject
    private PdfService pdfService;

    /**
     * POST  /seances -> Create a new seance.
     */
    @RequestMapping(value = "/seances",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Seance> createSeance(@RequestBody Seance seance) throws URISyntaxException {
        log.debug("REST request to save Seance : {}", seance);
        if (seance.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new seance cannot already have an ID").body(null);
        }
        Seance result = seanceService.create(seance);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/seances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("seance", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /seances -> Updates an existing seance.
     */
    @RequestMapping(value = "/seances",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Seance> updateSeance(@RequestBody Seance seance) throws URISyntaxException {
        log.debug("REST request to update Seance : {}", seance);
        if (seance.getId() == null) {
            return createSeance(seance);
        }
        Seance result = seanceService.update(seance);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("seance", seance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /seances -> get all the seances.
     */
    @RequestMapping(value = "/seances",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Seance>> getAllSeances(Pageable pageable)
        throws URISyntaxException {
        Page<Seance> page = seanceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/seances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /seances/:id -> get the "id" seance.
     */
    @RequestMapping(value = "/seances/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Seance> getSeance(@PathVariable Long id) {
        log.debug("REST request to get Seance : {}", id);
        return Optional.ofNullable(seanceRepository.findOne(id))
            .map(seance -> new ResponseEntity<>(
                seance,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/seances/{id}/export-pouvoirs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getPouvoirsExport(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Elu : {}", id);
        List<Pouvoir> pouvoirs = seanceService.getPouvoirsFromSeanceId(id);
        List<List<String>> result = new ArrayList<>();
        result.add(Arrays.asList(
            "Civilité élu cédeur",
            "Prénom élu cédeur",
            "Nom élu cédeur",
            "Civilité élu bénéficiaire",
            "Nom élu bénéficiaire",
            "Prénom élu bénéficiaire",
            "Date de début",
            "Heure de début",
            "Date de fin",
            "Heure de fin"
        ));
        for (Pouvoir pv : pouvoirs) {
            String dateDebut = pv.getDateDebut() != null ?
                pv.getDateDebut().format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
            String dateFin = pv.getDateFin() != null ?
                pv.getDateFin().format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
            result.add(Arrays.asList(
                pv.getEluCedeur().getCiviliteLabel(),
                pv.getEluCedeur().getPrenom(),
                pv.getEluCedeur().getNom(),
                pv.getEluBeneficiaire().getCiviliteLabel(),
                pv.getEluBeneficiaire().getNom(),
                pv.getEluBeneficiaire().getPrenom(),
                dateDebut,
                pv.getHeureDebut(),
                dateFin,
                pv.getHeureFin()
            ));
        }
        ExportService.Entry entry = new ExportService.Entry("Pouvoirs", result);

        byte[] export = exportService.exportToExcel(entry);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "siger-export-pouvoirs-seance-" + id;
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xlsx");
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e1) {
            // TODO exception
            e1.printStackTrace();
        }
    }

    @RequestMapping(value = "/seances/{id}/export-signatures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getSignaturesExport(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Elu : {}", id);
        Seance seance = seanceService.get(id);
        List<PresenceElu> presenceElus = seance != null ?
            seance.getPresenceElus().stream()
                .sorted(Comparator.comparing(p -> p.getElu().getNom()))
                .collect(Collectors.toList())
            : Collections.emptyList();
        List<List<String>> result = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        headers.addAll(Arrays.asList(
            "Civilité élu",
            "Nom élu",
            "Prénom élu"
        ));
        for (int i = 1; i <= (seance != null ? seance.getNombreSignatures() : 0); i++) {
            headers.add("Signature " + i);
        }
        result.add(headers);
        for (PresenceElu pe : presenceElus) {
            List<String> line = new ArrayList<>();
            line.addAll(Arrays.asList(
                pe.getElu().getCiviliteLabel(),
                pe.getElu().getPrenom(),
                pe.getElu().getNom()
            ));
            for (int i = 1; i <= seance.getNombreSignatures(); i++) {
                Integer position = i;
                Optional<Signature> s = pe.getSignatures().stream().filter(it -> it.getPosition() == position).findFirst();
                line.add(s.isPresent() ? s.get().getStatut().name() : "");
            }
            result.add(line);
        }
        ExportService.Entry entry = new ExportService.Entry("Pouvoirs", result);

        byte[] export = exportService.exportToExcel(entry);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "siger-export-signatures-seance-" + id;
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xlsx");
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e1) {
            // TODO exception
            e1.printStackTrace();
        }
    }

    @RequestMapping(value = "/seances/{id}/feuille-emargement",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getFeuilleEmargement(@PathVariable Long id, HttpServletResponse response) throws DocumentException {
        Seance seance = seanceService.get(id);
        List<Elu> elus = seance != null ?
            seance.getPresenceElus().stream()
                .map(p -> p.getElu())
                .sorted(Comparator.comparing(Elu::getNom))
                .collect(Collectors.toList())
            : Collections.emptyList();

        byte[] export = pdfService.export(elus, seance.getNombreSignatures());

        response.setContentType("application/pdf");
        String filename = "siger-feuille-emargement-seance-" + id;
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".pdf");
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e1) {
            // TODO exception
            e1.printStackTrace();
        }
    }

    /**
     * GET  /seances/:id -> get the "id" seance.
     */
    @RequestMapping(value = "/seances/{id}/dto",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SeanceDTO> getSeanceDTO(@PathVariable Long id) {
        log.debug("REST request to get Seance : {}", id);
        return Optional.ofNullable(seanceService.getDto(id))
            .map(seance -> new ResponseEntity<>(
                seance,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /seances/:id -> delete the "id" seance.
     */
    @RequestMapping(value = "/seances/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) {
        log.debug("REST request to delete Seance : {}", id);
        seanceService.delete(id);
        auditTrailService.logDeletion(Seance.class, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("seance", id.toString())).build();
    }

    /**
     * SEARCH  /_search/seances/:query -> search for the seance corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/seances/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Seance> searchSeances(@PathVariable String query) {
        return StreamSupport
            .stream(seanceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
