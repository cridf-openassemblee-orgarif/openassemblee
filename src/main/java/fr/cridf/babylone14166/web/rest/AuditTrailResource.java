package fr.cridf.babylone14166.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cridf.babylone14166.domain.AuditTrail;
import fr.cridf.babylone14166.repository.AuditTrailRepository;
import fr.cridf.babylone14166.repository.search.AuditTrailSearchRepository;
<<<<<<< HEAD
import fr.cridf.babylone14166.web.rest.util.HeaderUtil;
=======
import fr.cridf.babylone14166.web.rest.dto.AuditTrailDTO;
import fr.cridf.babylone14166.web.rest.mapper.AuditTrailMapper;
>>>>>>> 63865dd... AuditTrail cleaning backend
import fr.cridf.babylone14166.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing AuditTrail.
 */
@RestController
@RequestMapping("/api")
public class AuditTrailResource {

    private final Logger log = LoggerFactory.getLogger(AuditTrailResource.class);

    @Inject
    private AuditTrailRepository auditTrailRepository;

    @Inject
    private AuditTrailSearchRepository auditTrailSearchRepository;

    /**
     * GET  /auditTrails -> get all the auditTrails.
     */
    @RequestMapping(value = "/auditTrails",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AuditTrail>> getAllAuditTrails(Pageable pageable)
        throws URISyntaxException {
        Page<AuditTrail> page = auditTrailRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/auditTrails");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /auditTrails/:id -> get the "id" auditTrail.
     */
    @RequestMapping(value = "/auditTrails/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditTrail> getAuditTrail(@PathVariable Long id) {
        log.debug("REST request to get AuditTrail : {}", id);
        return Optional.ofNullable(auditTrailRepository.findOne(id))
            .map(auditTrail -> new ResponseEntity<>(
                auditTrail,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * SEARCH  /_search/auditTrails/:query -> search for the auditTrail corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/auditTrails/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AuditTrail> searchAuditTrails(@PathVariable String query) {
        return StreamSupport
            .stream(auditTrailSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}