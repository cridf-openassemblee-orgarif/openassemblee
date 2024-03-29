package openassemblee.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.codahale.metrics.annotation.Timed;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import openassemblee.domain.*;
import openassemblee.repository.EluRepository;
import openassemblee.repository.MandatRepository;
import openassemblee.repository.search.EluSearchRepository;
import openassemblee.service.*;
import openassemblee.service.dto.EluDTO;
import openassemblee.service.dto.EluListDTO;
import openassemblee.service.util.SecurityUtil;
import openassemblee.web.rest.util.HeaderUtil;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Inject
    private ExcelExportService excelExportService;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private PdfExportService pdfExportService;

    @Inject
    private ShortUidService shortUidService;

    @Inject
    private MandatRepository mandatRepository;

    @Inject
    private SessionMandatureService sessionMandatureService;

    /**
     * POST  /elus -> Create a new elu.
     */
    @RequestMapping(
        value = "/elus",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Elu> createElu(@RequestBody Elu elu)
        throws URISyntaxException {
        log.debug("REST request to save Elu : {}", elu);
        if (elu.getId() != null) {
            return ResponseEntity
                .badRequest()
                .header("Failure", "A new elu cannot already have an ID")
                .body(null);
        }
        ShortUid uid = shortUidService.createShortUid();
        elu.setUid(uid.getUid());
        elu.setShortUid(uid.getShortUid());
        Elu result = eluRepository.save(elu);
        Mandat mandat = new Mandat();
        mandat.setMandature(sessionMandatureService.getMandature());
        mandat.setElu(result);
        mandatRepository.save(mandat);
        eluSearchRepository.save(result);
        // no log for mandat...
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity
            .created(new URI("/api/elus/" + elu.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    "elu",
                    elu.getId().toString()
                )
            )
            .body(elu);
    }

    @RequestMapping(
        value = "/elus/{eluId}/adressePostale",
        method = RequestMethod.POST
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> createAdressePostale(
        @PathVariable Long eluId,
        @RequestBody AdressePostale adressePostale
    ) throws URISyntaxException {
        eluService.saveAdressePostale(eluId, adressePostale);
        auditTrailService.logCreation(
            adressePostale,
            adressePostale.getId(),
            Elu.class,
            eluId
        );
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/adressePostale",
        method = RequestMethod.PUT
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> updateAdressePostale(
        @PathVariable Long eluId,
        @RequestBody AdressePostale adressePostale
    ) throws URISyntaxException {
        eluService.updateAdressePostale(adressePostale);
        auditTrailService.logUpdate(
            adressePostale,
            adressePostale.getId(),
            Elu.class,
            eluId
        );
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE  /adressePostales/:id -> delete the "id" adressePostale.
     */
    @RequestMapping(
        value = "/elus/{eluId}/adressePostale/{adressePostaleId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> deleteAdressePostale(
        @PathVariable Long eluId,
        @PathVariable Long adressePostaleId
    ) {
        log.debug(
            "REST request to delete AdressePostale : {} for elu {}",
            adressePostaleId,
            eluId
        );
        eluService.deleteAdressePostale(eluId, adressePostaleId);
        auditTrailService.logDeletion(
            AdressePostale.class,
            adressePostaleId,
            Elu.class,
            eluId
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    "adressePostale",
                    adressePostaleId.toString()
                )
            )
            .build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/adresseMail",
        method = RequestMethod.POST
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> createAdresseMail(
        @PathVariable Long eluId,
        @RequestBody AdresseMail adresseMail
    ) throws URISyntaxException {
        eluService.saveAdresseMail(eluId, adresseMail);
        auditTrailService.logCreation(
            adresseMail,
            adresseMail.getId(),
            Elu.class,
            eluId
        );
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/adresseMail",
        method = RequestMethod.PUT
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> updateAdresseMail(
        @PathVariable Long eluId,
        @RequestBody AdresseMail adresseMail
    ) throws URISyntaxException {
        eluService.updateAdresseMail(adresseMail);
        auditTrailService.logUpdate(
            adresseMail,
            adresseMail.getId(),
            Elu.class,
            eluId
        );
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/adresseMail/{adresseMailId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> deleteAdresseMail(
        @PathVariable Long eluId,
        @PathVariable Long adresseMailId
    ) {
        log.debug(
            "REST request to delete AdresseMail : {} for elu {}",
            adresseMailId,
            eluId
        );
        eluService.deleteAdresseMail(eluId, adresseMailId);
        auditTrailService.logDeletion(
            AdresseMail.class,
            adresseMailId,
            Elu.class,
            eluId
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    "adresseMail",
                    adresseMailId.toString()
                )
            )
            .build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/identiteInternet",
        method = RequestMethod.POST
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> createIdentiteInternet(
        @PathVariable Long eluId,
        @RequestBody IdentiteInternet identiteInternet
    ) throws URISyntaxException {
        eluService.saveIdentiteInternet(eluId, identiteInternet);
        auditTrailService.logCreation(
            identiteInternet,
            identiteInternet.getId(),
            Elu.class,
            eluId
        );
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/identiteInternet",
        method = RequestMethod.PUT
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> updateIdentiteInternet(
        @PathVariable Long eluId,
        @RequestBody IdentiteInternet identiteInternet
    ) throws URISyntaxException {
        eluService.updateIdentiteInternet(identiteInternet);
        auditTrailService.logUpdate(
            identiteInternet,
            identiteInternet.getId(),
            Elu.class,
            eluId
        );
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/identiteInternet/{identiteInternetId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> deleteIdentiteInternet(
        @PathVariable Long eluId,
        @PathVariable Long identiteInternetId
    ) {
        log.debug(
            "REST request to delete IdentiteInternet : {} for elu {}",
            identiteInternetId,
            eluId
        );
        eluService.deleteIdentiteInternet(eluId, identiteInternetId);
        auditTrailService.logDeletion(
            IdentiteInternet.class,
            identiteInternetId,
            Elu.class,
            eluId
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    "identiteInternet",
                    identiteInternetId.toString()
                )
            )
            .build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/numeroFax",
        method = RequestMethod.POST
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> createNumeroFax(
        @PathVariable Long eluId,
        @RequestBody NumeroFax numeroFax
    ) throws URISyntaxException {
        eluService.saveNumeroFax(eluId, numeroFax);
        auditTrailService.logCreation(
            numeroFax,
            numeroFax.getId(),
            Elu.class,
            eluId
        );
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/numeroFax",
        method = RequestMethod.PUT
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> updateNumeroFax(
        @PathVariable Long eluId,
        @RequestBody NumeroFax numeroFax
    ) throws URISyntaxException {
        eluService.updateNumeroFax(numeroFax);
        auditTrailService.logUpdate(
            numeroFax,
            numeroFax.getId(),
            Elu.class,
            eluId
        );
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/numeroFax/{numeroFaxId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> deleteNumeroFax(
        @PathVariable Long eluId,
        @PathVariable Long numeroFaxId
    ) {
        log.debug(
            "REST request to delete NumeroFax : {} for elu {}",
            numeroFaxId,
            eluId
        );
        eluService.deleteNumeroFax(eluId, numeroFaxId);
        auditTrailService.logDeletion(
            NumeroFax.class,
            numeroFaxId,
            Elu.class,
            eluId
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    "numeroFax",
                    numeroFaxId.toString()
                )
            )
            .build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/numeroTelephone",
        method = RequestMethod.POST
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> createNumeroTelephone(
        @PathVariable Long eluId,
        @RequestBody NumeroTelephone numeroTelephone
    ) throws URISyntaxException {
        eluService.saveNumeroTelephone(eluId, numeroTelephone);
        auditTrailService.logCreation(
            numeroTelephone,
            numeroTelephone.getId(),
            Elu.class,
            eluId
        );
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/numeroTelephone",
        method = RequestMethod.PUT
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> updateNumeroTelephone(
        @PathVariable Long eluId,
        @RequestBody NumeroTelephone numeroTelephone
    ) throws URISyntaxException {
        eluService.updateNumeroTelephone(numeroTelephone);
        auditTrailService.logUpdate(
            numeroTelephone,
            numeroTelephone.getId(),
            Elu.class,
            eluId
        );
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
        value = "/elus/{eluId}/numeroTelephone/{numeroTelephoneId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> deleteNumeroTelephone(
        @PathVariable Long eluId,
        @PathVariable Long numeroTelephoneId
    ) {
        log.debug(
            "REST request to delete NumeroTelephone : {} for elu {}",
            numeroTelephoneId,
            eluId
        );
        eluService.deleteNumeroTelephone(eluId, numeroTelephoneId);
        auditTrailService.logDeletion(
            NumeroTelephone.class,
            numeroTelephoneId,
            Elu.class,
            eluId
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    "numeroTelephone",
                    numeroTelephoneId.toString()
                )
            )
            .build();
    }

    /**
     * POST  /elus/:id/photo -> Upload une photo
     */
    @RequestMapping(
        value = "/elus/{id}/image",
        method = RequestMethod.POST,
        consumes = "multipart/form-data"
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> uploadImage(
        @PathVariable Long id,
        @RequestBody MultipartFile file
    ) throws URISyntaxException {
        log.debug("REST upload image");
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            imageService.saveImagePourElu(
                id,
                new Image(file.getContentType(), file.getBytes())
            );
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
    @RequestMapping(
        value = "/elus",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Elu> updateElu(@RequestBody Elu elu)
        throws URISyntaxException {
        log.debug("REST request to update Elu : {}", elu);
        if (elu.getId() == null) {
            return createElu(elu);
        }
        Elu result = eluService.saveElu(elu);
        auditTrailService.logUpdate(elu, elu.getId());
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    "elu",
                    elu.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * GET  /elus -> get all the elus.
     */
    @RequestMapping(
        value = "/elus",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<EluListDTO> getAllElus() {
        log.debug("REST request to get all Elus");
        return eluService.getAll(false, false, false);
    }

    /**
     * GET  /elus/:id -> get the "id" elu.
     */
    @RequestMapping(
        value = "/elus/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    // TODO tester jsonview annotations + doc
    @Timed
    public ResponseEntity<EluDTO> getElu(
        @PathVariable Long id,
        Authentication auth
    ) {
        log.debug("REST request to get Elu : {}", id);
        EluDTO eluDTO = eluService.get(id, !SecurityUtil.isAdmin(auth));
        if (eluDTO != null) {
            return new ResponseEntity<>(eluDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(
        value = "/elus/{id}/export",
        method = RequestMethod.GET,
        // TODO pas du tout, en fait =]
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public void getEluExport(
        @PathVariable Long id,
        HttpServletResponse response,
        Authentication auth
    ) {
        log.debug("REST request to get Elu : {}", id);
        EluDTO eluDTO = eluService.get(id, !SecurityUtil.isAdmin(auth));
        if (eluDTO != null) {
            Elu e = eluDTO.getElu();
            String civilite = e.getCiviliteLabel();
            String dateNaissance = e.getDateNaissance() != null
                ? e.getDateNaissance().format(DateTimeFormatter.ISO_LOCAL_DATE)
                : "Date de naissance inconnue";
            ExcelExportService.Entry entry1 = new ExcelExportService.Entry(
                "Élu",
                Arrays.asList(
                    Arrays.asList(
                        "Civilité",
                        "Prénom",
                        "Nom",
                        "Profession",
                        "Lieu de naissance",
                        "Date de naissance"
                    ),
                    Arrays.asList(
                        civilite,
                        e.getPrenom(),
                        e.getNom(),
                        e.getProfession(),
                        e.getLieuNaissance(),
                        dateNaissance
                    )
                )
            );
            List<List<String>> adressesPostales = new ArrayList<>();
            adressesPostales.add(
                Arrays.asList(
                    "Adresse",
                    "Nature pro/perso",
                    "Niveau de confidentialité",
                    "Adresse de correspondance",
                    "Publication dans l'annuaire"
                )
            );
            for (AdressePostale ap : e.getAdressesPostales()) {
                String nature = ap.getNatureProPerso() != null
                    ? ap.getNatureProPerso().name()
                    : "Inconnue";
                String niveauConfidentialite = ap.getNiveauConfidentialite() !=
                    null
                    ? ap.getNiveauConfidentialite().name()
                    : "Inconnu";
                adressesPostales.add(
                    Arrays.asList(
                        ap.getOneline(),
                        nature,
                        niveauConfidentialite,
                        ap.getAdresseDeCorrespondance() != null &&
                            ap.getAdresseDeCorrespondance()
                            ? "Oui"
                            : "Non",
                        ap.getPublicationAnnuaire() != null &&
                            ap.getPublicationAnnuaire()
                            ? "Oui"
                            : "Non"
                    )
                );
            }
            ExcelExportService.Entry entry2 = new ExcelExportService.Entry(
                "Adresses postales",
                adressesPostales
            );
            List<List<String>> groupesPolitiques = new ArrayList<>();
            groupesPolitiques.add(
                Arrays.asList(
                    "Groupe",
                    "Date de début",
                    "Date de fin",
                    "Motif de fin"
                )
            );
            for (AppartenanceGroupePolitique agp : e.getAppartenancesGroupePolitique()) {
                GroupePolitique gp = agp.getGroupePolitique();
                String dateDebut = agp.getDateDebut() != null
                    ? agp
                        .getDateDebut()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE)
                    : "Inconnue";
                String dateFin = agp.getDateFin() != null
                    ? agp.getDateFin().format(DateTimeFormatter.ISO_LOCAL_DATE)
                    : "";
                String motifFin = agp.getMotifFin() != null
                    ? agp.getMotifFin()
                    : "";
                groupesPolitiques.add(
                    Arrays.asList(gp.getNom(), dateDebut, dateFin, motifFin)
                );
            }
            ExcelExportService.Entry entry3 = new ExcelExportService.Entry(
                "Groupes politiques",
                groupesPolitiques
            );

            byte[] export = excelExportService.exportToExcel(
                entry1,
                entry2,
                entry3
            );

            response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );
            String filename = "siger-export-elu-" + id;
            response.setHeader(
                "Content-disposition",
                "attachment; filename=" + filename + ".xlsx"
            );
            try {
                Streams.copy(export, response.getOutputStream());
            } catch (IOException e1) {
                // TODO exception
                e1.printStackTrace();
            }
        }
    }

    @RequestMapping(
        value = "/elus/export-pdf",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public void exportPdf(HttpServletResponse response, Authentication auth)
        throws DocumentException {
        List<EluListDTO> elus = eluService.getAll(
            true,
            !SecurityUtil.isAdmin(auth),
            true
        );

        byte[] export = pdfExportService.exportElus(elus);

        response.setContentType("application/pdf");
        String filename = "siger-export-elus";
        response.setHeader(
            "Content-disposition",
            "attachment; filename=" + filename + ".pdf"
        );
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e1) {
            // TODO exception
            e1.printStackTrace();
        }
    }

    @RequestMapping(
        value = "/elus/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public void getAllElusExport(
        HttpServletResponse response,
        Authentication auth
    ) {
        log.debug("REST request to get elus export");
        ExcelExportService.Entry[] entries = eluService.getExportEntries(
            !SecurityUtil.isAdmin(auth)
        );
        byte[] export = excelExportService.exportToExcel(entries);

        response.setContentType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );
        String filename = "siger-export-elus";
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

    /**
     * DELETE  /elus/:id -> delete the "id" elu.
     */
    @RequestMapping(
        value = "/elus/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_USER")
    public ResponseEntity<Void> deleteElu(@PathVariable Long id) {
        log.debug("REST request to delete Elu : {}", id);
        eluRepository.delete(id);
        eluSearchRepository.delete(id);
        auditTrailService.logDeletion(Elu.class, id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityDeletionAlert("elu", id.toString()))
            .build();
    }

    /**
     * SEARCH  /_search/elus/:query -> search for the elu corresponding
     * to the query.
     */
    @RequestMapping(
        value = "/_search/elus/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<Elu> searchElus(@PathVariable String query) {
        return Lists.newArrayList(
            eluSearchRepository.search(queryStringQuery(query))
        );
    }
}
