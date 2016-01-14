package fr.cridf.babylone14166.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.domain.GroupePolitique;
import fr.cridf.babylone14166.domain.Image;
import fr.cridf.babylone14166.repository.GroupePolitiqueRepository;
import fr.cridf.babylone14166.repository.search.GroupePolitiqueSearchRepository;
import fr.cridf.babylone14166.service.*;
import fr.cridf.babylone14166.service.dto.GroupePolitiqueDTO;
import fr.cridf.babylone14166.service.dto.GroupePolitiqueListDTO;
import fr.cridf.babylone14166.web.rest.util.HeaderUtil;

/**
 * REST controller for managing GroupePolitique.
 */
@RestController
@RequestMapping("/api")
public class GroupePolitiqueResource {

    private final Logger log = LoggerFactory.getLogger(GroupePolitiqueResource.class);

    @Inject
    private GroupePolitiqueRepository groupePolitiqueRepository;

    @Inject
    private GroupePolitiqueSearchRepository groupePolitiqueSearchRepository;

    @Inject
    private GroupePolitiqueService groupePolitiqueService;

    @Inject
    private ImageService imageService;

    @Inject
    private ExportService exportService;

    /**
     * POST  /groupePolitiques -> Create a new groupePolitique.
     */
    @RequestMapping(value = "/groupePolitiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupePolitique> createGroupePolitique(@RequestBody GroupePolitique groupePolitique) throws
        URISyntaxException {
        log.debug("REST request to save GroupePolitique : {}", groupePolitique);
        if (groupePolitique.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new groupePolitique cannot already have an ID")
                .body(null);
        }
        GroupePolitique result = groupePolitiqueService.save(groupePolitique);
        return ResponseEntity.created(new URI("/api/groupePolitiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("groupePolitique", result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /elus/:id/photo -> Upload une photo
     */
    @RequestMapping(value = "/groupePolitiques/{id}/image", method = RequestMethod.POST, consumes =
        "multipart/form-data")
    @Timed
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestBody MultipartFile file) throws
        URISyntaxException {
        log.debug("REST upload image");
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            imageService.saveImagePourGroupePolitique(id, new Image(file.getContentType(), file.getBytes()));
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
     * PUT  /groupePolitiques -> Updates an existing groupePolitique.
     */
    @RequestMapping(value = "/groupePolitiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupePolitique> updateGroupePolitique(@RequestBody GroupePolitique groupePolitique) throws
        URISyntaxException {
        log.debug("REST request to update GroupePolitique : {}", groupePolitique);
        if (groupePolitique.getId() == null) {
            return createGroupePolitique(groupePolitique);
        }
        GroupePolitique result = groupePolitiqueRepository.save(groupePolitique);
        groupePolitiqueSearchRepository.save(groupePolitique);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("groupePolitique", groupePolitique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groupePolitiques -> get all the groupePolitiques.
     */
    @RequestMapping(value = "/groupePolitiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GroupePolitique> getAllGroupePolitiques() {
        log.debug("REST request to get all GroupePolitiques");
        return groupePolitiqueRepository.findAll();
    }

    /**
     * GET  /groupePolitiques -> get all the groupePolitiques.
     */
    @RequestMapping(value = "/groupePolitiques-dtos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GroupePolitiqueListDTO> getAllGroupePolitiquesDtos() {
        log.debug("REST request to get all GroupePolitiques dtos");
        return groupePolitiqueService.getAll();
    }

    @RequestMapping(value = "/groupePolitiques/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getAllGroupePolitiquesExport(HttpServletResponse response) {
        log.debug("REST request to get all GroupePolitiques");
        try {
            List<GroupePolitiqueListDTO> gps = groupePolitiqueService.getAll();
            List<List<String>> lines = new ArrayList<>();
            for (GroupePolitiqueListDTO gpDto : gps) {
                GroupePolitique gp = gpDto.getGroupePolitique();
                String adresse = gp.getAdressePostale() != null ? gp.getAdressePostale().getOneline() : "Pas d'adresse";
                lines.add(Arrays.asList(gp.getNom(), gp.getNomCourt(), gpDto.getCount() + " membres", adresse));
            }
            byte[] export = exportService.exportToExcel(lines);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=groupes-politiques.xlsx");
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    /**
     * GET  /groupePolitiques/:id -> get the "id" groupePolitique.
     */
    @RequestMapping(value = "/groupePolitiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupePolitiqueDTO> getGroupePolitique(@PathVariable Long id) {
        log.debug("REST request to get GroupePolitique : {}", id);
        GroupePolitiqueDTO dto = groupePolitiqueService.get(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETE  /groupePolitiques/:id -> delete the "id" groupePolitique.
     */
    @RequestMapping(value = "/groupePolitiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGroupePolitique(@PathVariable Long id) {
        log.debug("REST request to delete GroupePolitique : {}", id);
        groupePolitiqueRepository.delete(id);
        groupePolitiqueSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("groupePolitique", id.toString()))
            .build();
    }

    /**
     * SEARCH  /_search/groupePolitiques/:query -> search for the groupePolitique corresponding to the query.
     */
    @RequestMapping(value = "/_search/groupePolitiques/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GroupePolitique> searchGroupePolitiques(@PathVariable String query) {
        return Lists.newArrayList(groupePolitiqueSearchRepository.search(queryStringQuery(query)));
    }
}
