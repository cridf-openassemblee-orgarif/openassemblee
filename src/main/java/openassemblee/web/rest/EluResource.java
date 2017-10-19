package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.*;
import openassemblee.repository.EluRepository;
import openassemblee.repository.search.EluSearchRepository;
import openassemblee.service.AuditTrailService;
import openassemblee.service.EluService;
import openassemblee.service.ExportService;
import openassemblee.service.ImageService;
import openassemblee.service.dto.EluDTO;
import openassemblee.service.dto.EluListDTO;
import openassemblee.web.rest.util.HeaderUtil;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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
    private ExportService exportService;

    @Inject
    private AuditTrailService auditTrailService;

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
        Elu result = eluRepository.save(elu);
        eluSearchRepository.save(result);
        auditTrailService.logCreation(result, result.getId());
        return ResponseEntity.created(new URI("/api/elus/" + elu.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("elu", elu.getId().toString()))
            .body(elu);
    }

    @RequestMapping(value = "/elus/{eluId}/adressePostale", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> createAdressePostale(@PathVariable Long eluId, @RequestBody AdressePostale adressePostale)
        throws URISyntaxException {
        eluService.saveAdressePostale(eluId, adressePostale);
        auditTrailService.logCreation(adressePostale, adressePostale.getId(), Elu.class, eluId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{eluId}/adressePostale", method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<Void> updateAdressePostale(@PathVariable Long eluId, @RequestBody AdressePostale adressePostale)
        throws URISyntaxException {
        eluService.updateAdressePostale(adressePostale);
        auditTrailService.logUpdate(adressePostale, adressePostale.getId(), Elu.class, eluId);
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE  /adressePostales/:id -> delete the "id" adressePostale.
     */
    @RequestMapping(value = "/elus/{eluId}/adressePostale/{adressePostaleId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdressePostale(@PathVariable Long eluId, @PathVariable Long adressePostaleId) {
        log.debug("REST request to delete AdressePostale : {} for elu {}", adressePostaleId, eluId);
        eluService.deleteAdressePostale(eluId, adressePostaleId);
        auditTrailService.logDeletion(AdressePostale.class, adressePostaleId, Elu.class, eluId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("adressePostale", adressePostaleId.toString())).build();
    }

    @RequestMapping(value = "/elus/{eluId}/adresseMail", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> createAdresseMail(@PathVariable Long eluId, @RequestBody AdresseMail adresseMail)
        throws URISyntaxException {
        eluService.saveAdresseMail(eluId, adresseMail);
        auditTrailService.logCreation(adresseMail, adresseMail.getId(), Elu.class, eluId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{eluId}/adresseMail", method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<Void> updateAdresseMail(@PathVariable Long eluId, @RequestBody AdresseMail adresseMail)
        throws URISyntaxException {
        eluService.updateAdresseMail(adresseMail);
        auditTrailService.logUpdate(adresseMail, adresseMail.getId(), Elu.class, eluId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{eluId}/identiteInternet", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> createIdentiteInternet(@PathVariable Long eluId, @RequestBody IdentiteInternet
        identiteInternet)
        throws URISyntaxException {
        eluService.saveIdentiteInternet(eluId, identiteInternet);
        auditTrailService.logCreation(identiteInternet, identiteInternet.getId(), Elu.class, eluId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{eluId}/identiteInternet", method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<Void> updateIdentiteInternet(@PathVariable Long eluId, @RequestBody IdentiteInternet identiteInternet)
        throws URISyntaxException {
        eluService.updateIdentiteInternet(identiteInternet);
        auditTrailService.logUpdate(identiteInternet, identiteInternet.getId(), Elu.class, eluId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{eluId}/numeroFax", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> createNumeroFax(@PathVariable Long eluId, @RequestBody NumeroFax numeroFax)
        throws URISyntaxException {
        eluService.saveNumeroFax(eluId, numeroFax);
        auditTrailService.logCreation(numeroFax, numeroFax.getId(), Elu.class, eluId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{eluId}/numeroFax", method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<Void> updateNumeroFax(@PathVariable Long eluId, @RequestBody NumeroFax numeroFax)
        throws URISyntaxException {
        eluService.updateNumeroFax(numeroFax);
        auditTrailService.logUpdate(numeroFax, numeroFax.getId(), Elu.class, eluId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{eluId}/numeroTelephone", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> createNumeroTelephone(@PathVariable Long eluId, @RequestBody NumeroTelephone
        numeroTelephone)
        throws URISyntaxException {
        eluService.saveNumeroTelephone(eluId, numeroTelephone);
        auditTrailService.logCreation(numeroTelephone, numeroTelephone.getId(), Elu.class, eluId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/elus/{eluId}/numeroTelephone", method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<Void> updateNumeroTelephone(@PathVariable Long eluId, @RequestBody NumeroTelephone numeroTelephone)
        throws URISyntaxException {
        eluService.updateNumeroTelephone(numeroTelephone);
        auditTrailService.logUpdate(numeroTelephone, numeroTelephone.getId(), Elu.class, eluId);
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
        Elu result = eluService.saveElu(elu);
        auditTrailService.logUpdate(elu, elu.getId());
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

    @RequestMapping(value = "/elus/{id}/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getEluExport(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Elu : {}", id);
        EluDTO eluDTO = eluService.get(id);
        if (eluDTO != null) {
            Elu e = eluDTO.getElu();
            String civilite = e.getCivilite() != null ? e.getCivilite().label() : "Civilité non connue";
            String dateNaissance = e.getDateNaissance() != null ?
                e.getDateNaissance().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de naissance inconnue";
            ExportService.Entry entry1 = new ExportService.Entry("Élu", Arrays.asList(
                Arrays.asList("Civilité", "Prénom", "Nom", "Profession", "Lieu de naissance", "Date de naissance"),
                Arrays.asList(civilite, e.getPrenom(), e.getNom(), e.getProfession(), e.getLieuNaissance(),
                    dateNaissance)));
            List<List<String>> adressesPostales = new ArrayList<>();
            adressesPostales.add(Arrays.asList("Adresse", "Nature pro/perso", "Niveau de confidentialité",
                "Adresse de correspondance", "Publication dans l'annuaire"));
            for (AdressePostale ap : e.getAdressesPostales()) {
                String nature = ap.getNatureProPerso() != null ? ap.getNatureProPerso().name() :
                    "Inconnue";
                String niveauConfidentialite = ap.getNiveauConfidentialite() != null ?
                    ap.getNiveauConfidentialite().name() : "Inconnu";
                adressesPostales.add(Arrays.asList(ap.getOneline(), nature, niveauConfidentialite,
                    ap.getAdresseDeCorrespondance() != null && ap.getAdresseDeCorrespondance() ? "Oui" : "Non",
                    ap.getPublicationAnnuaire() != null && ap.getPublicationAnnuaire() ? "Oui" : "Non"));
            }
            ExportService.Entry entry2 = new ExportService.Entry("Adresses postales", adressesPostales);
            List<List<String>> groupesPolitiques = new ArrayList<>();
            groupesPolitiques.add(Arrays.asList("Groupe", "Date de début", "Date de fin", "Motif de fin"));
            for (AppartenanceGroupePolitique agp : e.getAppartenancesGroupePolitique()) {
                GroupePolitique gp = agp.getGroupePolitique();
                String dateDebut = agp.getDateDebut() != null ?
                    agp.getDateDebut().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Inconnue";
                String dateFin = agp.getDateFin() != null ?
                    agp.getDateFin().format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
                String motifFin = agp.getMotifFin() != null ? agp.getMotifFin() : "";
                groupesPolitiques.add(Arrays.asList(gp.getNom(), dateDebut, dateFin, motifFin));
            }
            ExportService.Entry entry3 = new ExportService.Entry("Groupes politiques", groupesPolitiques);

            byte[] export = exportService.exportToExcel(entry1, entry2, entry3);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=elu-" + id + ".xlsx");
            try {
                Streams.copy(export, response.getOutputStream());
            } catch (IOException e1) {
                // TODO exception
                e1.printStackTrace();
            }
        }
    }


    @RequestMapping(value = "/elus/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getAllElusExport(HttpServletResponse response) {
        log.debug("REST request to get all GroupePolitiques");
        List<EluListDTO> dtos = eluService.getAll();
        List<List<String>> lines = new ArrayList<>();
        lines.add(Arrays.asList("Civilité", "Prénom", "Nom", "Groupe politique", "Profession", "Lieu de naissance",
            "Date de naissance"));
        for (EluListDTO dto : dtos) {
            Elu e = dto.getElu();
            String civilite = e.getCivilite() != null ? e.getCivilite().label() : "Civilité non connue";
            String groupePolitique = dto.getGroupePolitique() != null ? dto.getGroupePolitique().getNom() :
                "Aucun groupe politique";
            String dateNaissance = e.getDateNaissance() != null ?
                e.getDateNaissance().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de naissance inconnue";
            lines.add(Arrays.asList(civilite, e.getPrenom(), e.getNom(), groupePolitique, e.getProfession(),
                e.getLieuNaissance(), dateNaissance));
        }
        byte[] export = exportService.exportToExcel("Élus", lines);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment; filename=elus.xlsx");
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
    @RequestMapping(value = "/elus/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteElu(@PathVariable Long id) {
        log.debug("REST request to delete Elu : {}", id);
        eluRepository.delete(id);
        eluSearchRepository.delete(id);
        auditTrailService.logDeletion(Elu.class, id);
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
