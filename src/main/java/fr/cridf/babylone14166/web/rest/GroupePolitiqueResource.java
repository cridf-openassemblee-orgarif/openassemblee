package fr.cridf.babylone14166.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.domain.GroupePolitique;
import fr.cridf.babylone14166.repository.GroupePolitiqueRepository;
import fr.cridf.babylone14166.repository.search.GroupePolitiqueSearchRepository;
import fr.cridf.babylone14166.service.GroupePolitiqueService;
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
     * GET  /groupePolitiques/:id -> get the "id" groupePolitique.
     */
    @RequestMapping(value = "/groupePolitiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupePolitique> getGroupePolitique(@PathVariable Long id) {
        log.debug("REST request to get GroupePolitique : {}", id);
        GroupePolitique groupePolitique = groupePolitiqueService.get(id);
        if (groupePolitique != null) {
            return new ResponseEntity<>(groupePolitique, HttpStatus.OK);
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
