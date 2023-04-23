package openassemblee.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.codahale.metrics.annotation.Timed;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import openassemblee.domain.*;
import openassemblee.repository.AppartenanceCommissionThematiqueRepository;
import openassemblee.repository.CommissionThematiqueRepository;
import openassemblee.repository.FonctionCommissionThematiqueRepository;
import openassemblee.repository.search.CommissionThematiqueSearchRepository;
import openassemblee.service.*;
import openassemblee.service.dto.*;
import openassemblee.service.util.SecurityUtil;
import openassemblee.web.rest.util.HeaderUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.common.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing CommissionThematique.
 */
@RestController
@RequestMapping("/api")
public class CommissionThematiqueResource {

    private final Logger log = LoggerFactory.getLogger(
        CommissionThematiqueResource.class
    );

    @Inject
    private CommissionThematiqueRepository commissionThematiqueRepository;

    @Inject
    private CommissionThematiqueSearchRepository commissionThematiqueSearchRepository;

    @Inject
    private CommissionThematiqueService commissionThematiqueService;

    @Inject
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;

    @Inject
    private FonctionCommissionThematiqueRepository fonctionCommissionThematiqueRepository;

    @Inject
    private ExcelExportService excelExportService;

    @Inject
    private PdfExportService pdfExportService;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private EluService eluService;

    @Autowired
    private SessionMandatureService sessionMandatureService;

    /**
     * POST  /commissionThematiques -> Create a new commissionThematique.
     */
    @RequestMapping(
        value = "/commissionThematiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<CommissionThematique> createCommissionThematique(
        @RequestBody CommissionThematique commissionThematique
    ) throws URISyntaxException {
        log.debug(
            "REST request to save CommissionThematique : {}",
            commissionThematique
        );
        if (commissionThematique.getId() != null) {
            return ResponseEntity
                .badRequest()
                .header(
                    "Failure",
                    "A new commissionThematique cannot already have an ID"
                )
                .body(null);
        }
        CommissionThematique result = commissionThematiqueRepository.save(
            commissionThematique
        );
        commissionThematiqueSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity
            .created(new URI("/api/commissionThematiques/" + result.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    "commissionThematique",
                    result.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * PUT  /commissionThematiques -> Updates an existing commissionThematique.
     */
    @RequestMapping(
        value = "/commissionThematiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<CommissionThematique> updateCommissionThematique(
        @RequestBody CommissionThematique commissionThematique
    ) throws URISyntaxException {
        log.debug(
            "REST request to update CommissionThematique : {}",
            commissionThematique
        );
        if (commissionThematique.getId() == null) {
            return createCommissionThematique(commissionThematique);
        }
        CommissionThematique result = commissionThematiqueRepository.save(
            commissionThematique
        );
        commissionThematiqueSearchRepository.save(commissionThematique);
        auditTrailService.logUpdate(result, result.getId());
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    "commissionThematique",
                    commissionThematique.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * GET  /commissionThematiques -> get all the commissionThematiques.
     */
    @RequestMapping(
        value = "/commissionThematiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<CommissionThematique> getAllCommissionThematiques() {
        log.debug("REST request to get all CommissionThematiques");
        return commissionThematiqueRepository.findByMandature(
            sessionMandatureService.getMandature()
        );
    }

    /**
     * GET  /commissionThematiques -> get all the commissionThematiques.
     */
    @RequestMapping(
        value = "/commissionThematiques-dtos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<CommissionThematiqueListDTO> getAllCommissionThematiquesDtos() {
        log.debug("REST request to get all CommissionThematiques dtos");
        return commissionThematiqueService.getAllDtos();
    }

    @RequestMapping(
        value = "/commissionThematiques/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public void getAllCommissionThematiquesExport(
        HttpServletResponse response,
        Authentication auth
    ) {
        log.debug("REST request to get all GroupePolitiques");

        byte[] export = excelExportService.exportToExcel(
            commissionThematiqueService.getExportEntries(
                !SecurityUtil.isAdmin(auth)
            )
        );

        response.setContentType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );
        String filename = "siger-export-commissions-thematiques";
        response.setHeader(
            "Content-disposition",
            "attachment; filename=" + filename + ".xlsx"
        );
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e) {
            // TODO exception
            e.printStackTrace();
        }
    }

    @RequestMapping(
        value = "/commissionThematiques/export-pdf",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Transactional(readOnly = true)
    @Secured("ROLE_USER")
    public void getAllCommissionThematiquesExportPdf(
        HttpServletResponse response
    ) throws DocumentException {
        log.debug("REST request to get all GroupePolitiques");
        List<CommissionThematique> cts = commissionThematiqueService.getAll();
        List<AppartenanceCommissionThematique> as =
            appartenanceCommissionThematiqueRepository.findAll();
        List<FonctionCommissionThematique> fs =
            fonctionCommissionThematiqueRepository.findAll();
        Map<Long, List<EluEnFonctionDTO>> appartenancesByCt = cts
            .stream()
            .map(ct -> {
                List<EluEnFonctionDTO> dtos = as
                    .stream()
                    .filter(a -> a.getCommissionThematique() != null)
                    .filter(a ->
                        a.getCommissionThematique().getId().equals(ct.getId())
                    )
                    .filter(a -> a.getDateFin() == null)
                    .map(a -> {
                        EluListDTO dto = eluService.eluToEluListDTO(
                            a.getElu(),
                            false,
                            false
                        );
                        return new EluEnFonctionDTO(
                            a.getElu(),
                            dto.getGroupePolitique(),
                            null
                        );
                    })
                    .collect(Collectors.toList());
                return ImmutablePair.of(ct.getId(), dtos);
            })
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
        Map<Long, List<EluEnFonctionDTO>> fonctionByCt = cts
            .stream()
            .map(ct -> {
                List<EluEnFonctionDTO> dtos = fs
                    .stream()
                    // should not happen but happens
                    .filter(f -> f.getCommissionThematique() != null)
                    .filter(f ->
                        f.getCommissionThematique().getId().equals(ct.getId())
                    )
                    .filter(f -> f.getDateFin() == null)
                    .map(f -> {
                        EluListDTO dto = eluService.eluToEluListDTO(
                            f.getElu(),
                            false,
                            false
                        );
                        return new EluEnFonctionDTO(
                            f.getElu(),
                            dto.getGroupePolitique(),
                            f.getFonction()
                        );
                    })
                    .collect(Collectors.toList());
                return ImmutablePair.of(ct.getId(), dtos);
            })
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
        byte[] export = pdfExportService.exportCommissionsThematiques(
            cts,
            appartenancesByCt,
            fonctionByCt
        );

        response.setContentType("application/pdf");
        String filename = "siger-export-commissions-thematiques";
        response.setHeader(
            "Content-disposition",
            "attachment; filename=" + filename + ".pdf"
        );
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
    @RequestMapping(
        value = "/commissionThematiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<CommissionThematiqueDTO> getCommissionThematique(
        @PathVariable Long id
    ) {
        log.debug("REST request to get CommissionThematique : {}", id);
        CommissionThematiqueDTO dto = commissionThematiqueService.get(
            id,
            false
        );
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * GET  /commissionThematiques/:id -> get the "id" commissionThematique.
     */
    @RequestMapping(
        value = "/commissionThematiques/{id}/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public void getCommissionThematiqueExport(
        @PathVariable Long id,
        HttpServletResponse response
    ) {
        CommissionThematiqueDTO dto = commissionThematiqueService.get(id, true);
        List<List<String>> lines = new ArrayList<>();
        CommissionThematique ct = dto.getCommissionThematique();
        lines.add(Arrays.asList(ct.getNom()));
        lines.add(new ArrayList<>());
        // FIXME si la CT est fermée ça n'apparait pas là!
        // TODO autre sheet ?
        List<AppartenanceCommissionThematiqueDTO> appartenances = dto
            .getAppartenanceCommissionThematiqueDTOs()
            .stream()
            .filter(a ->
                a.getAppartenanceCommissionThematique().getDateFin() == null
            )
            .collect(Collectors.toList());
        for (AppartenanceCommissionThematiqueDTO act : appartenances) {
            Optional<GroupePolitique> groupePolitiqueCedeur = act
                .getElu()
                .getAppartenancesGroupePolitique()
                .stream()
                .filter(GroupePolitiqueService::isAppartenanceCourante)
                .map(AppartenanceGroupePolitique::getGroupePolitique)
                .findFirst();
            lines.add(
                Arrays.asList(
                    act.getElu().getNom(),
                    act.getElu().getPrenom(),
                    groupePolitiqueCedeur
                        .map(GroupePolitique::getNom)
                        .orElse("Aucun groupe politique")
                )
            );
        }
        // FIXMENOW pas les fonctions ???
        ExcelExportService.Entry appartenancesEntry =
            new ExcelExportService.Entry("Appartenances", lines);
        List<FonctionCommissionThematiqueDTO> fonctions = dto
            .getFonctionCommissionThematiqueDTOs()
            .stream()
            .filter(f ->
                f.getFonctionCommissionThematique().getDateFin() == null
            )
            .collect(Collectors.toList());
        List<List<String>> fonctionsLines = new ArrayList<>();
        for (FonctionCommissionThematiqueDTO f : fonctions) {
            Optional<GroupePolitique> groupePolitiqueCedeur = f
                .getElu()
                .getAppartenancesGroupePolitique()
                .stream()
                .filter(GroupePolitiqueService::isAppartenanceCourante)
                .map(AppartenanceGroupePolitique::getGroupePolitique)
                .findFirst();
            fonctionsLines.add(
                Arrays.asList(
                    f.getElu().getNom(),
                    f.getElu().getPrenom(),
                    groupePolitiqueCedeur
                        .map(GroupePolitique::getNom)
                        .orElse("Aucun groupe politique")
                )
            );
        }
        ExcelExportService.Entry fonctionsEntry = new ExcelExportService.Entry(
            "Fonctions",
            fonctionsLines
        );
        byte[] export = excelExportService.exportToExcel(
            appartenancesEntry,
            fonctionsEntry
        );

        response.setContentType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );
        String filename = "siger-export-commission-thematique-" + id;
        response.setHeader(
            "Content-disposition",
            "attachment; filename=" + filename + ".xlsx"
        );
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e) {
            // TODO exception
            e.printStackTrace();
        }
    }

    @RequestMapping(
        value = "/commissionThematiques/{id}/export-pdf",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Transactional(readOnly = true)
    public void getCommissionThematiqueExportPdf(
        @PathVariable Long id,
        HttpServletResponse response
    ) throws DocumentException {
        log.debug("REST request to get all GroupePolitiques");
        CommissionThematiqueDTO ct = commissionThematiqueService.get(id, false);
        List<EluEnFonctionDTO> as = ct
            .getAppartenanceCommissionThematiqueDTOs()
            .stream()
            .filter(a ->
                a.getAppartenanceCommissionThematique().getDateFin() == null
            )
            .map(a -> {
                EluListDTO dto = eluService.eluToEluListDTO(
                    a.getElu(),
                    false,
                    false
                );
                return new EluEnFonctionDTO(
                    a.getElu(),
                    dto.getGroupePolitique(),
                    null
                );
            })
            .collect(Collectors.toList());
        List<EluEnFonctionDTO> fs = ct
            .getFonctionCommissionThematiqueDTOs()
            .stream()
            .filter(f ->
                f.getFonctionCommissionThematique().getDateFin() == null
            )
            .map(a -> {
                EluListDTO dto = eluService.eluToEluListDTO(
                    a.getElu(),
                    false,
                    false
                );
                return new EluEnFonctionDTO(
                    a.getElu(),
                    dto.getGroupePolitique(),
                    a.getFonctionCommissionThematique().getFonction()
                );
            })
            .collect(Collectors.toList());
        byte[] export = pdfExportService.exportCommissionsThematique(
            ct.getCommissionThematique(),
            as,
            fs
        );

        response.setContentType("application/pdf");
        String filename =
            "siger-export-commission-thematique-" +
            ct.getCommissionThematique().getId();
        response.setHeader(
            "Content-disposition",
            "attachment; filename=" + filename + ".pdf"
        );
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
    @RequestMapping(
        value = "/commissionThematiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> deleteCommissionThematique(
        @PathVariable Long id
    ) {
        log.debug("REST request to delete CommissionThematique : {}", id);
        commissionThematiqueRepository.delete(id);
        commissionThematiqueSearchRepository.delete(id);
        auditTrailService.logDeletion(CommissionThematique.class, id);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    "commissionThematique",
                    id.toString()
                )
            )
            .build();
    }

    /**
     * SEARCH  /_search/commissionThematiques/:query -> search for the commissionThematique corresponding
     * to the query.
     */
    @RequestMapping(
        value = "/_search/commissionThematiques/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<CommissionThematique> searchCommissionThematiques(
        @PathVariable String query
    ) {
        return StreamSupport
            .stream(
                commissionThematiqueSearchRepository
                    .search(queryStringQuery(query))
                    .spliterator(),
                false
            )
            .collect(Collectors.toList());
    }
}
