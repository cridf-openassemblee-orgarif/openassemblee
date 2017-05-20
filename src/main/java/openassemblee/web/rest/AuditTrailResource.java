package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.AuditTrail;
import openassemblee.repository.AuditTrailRepository;
import openassemblee.repository.search.AuditTrailSearchRepository;
import openassemblee.web.rest.dto.AuditTrailDTO;
import openassemblee.web.rest.mapper.AuditTrailMapper;
import openassemblee.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.LinkedList;
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
    private AuditTrailMapper auditTrailMapper;

    @Inject
    private AuditTrailSearchRepository auditTrailSearchRepository;

    /**
     * GET  /auditTrails -> get all the auditTrails.
     */
    @RequestMapping(value = "/auditTrails",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AuditTrailDTO>> getAllAuditTrails(
        @SortDefault(sort = {"date"}, direction = Sort.Direction.DESC) Pageable pageable)
        throws URISyntaxException {
        Page<AuditTrail> page = auditTrailRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/auditTrails");
        return new ResponseEntity<>(page.getContent().stream()
            .map(auditTrailMapper::entityToDto)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /auditTrails/:id -> get the "id" auditTrail.
     */
    @RequestMapping(value = "/auditTrails/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuditTrailDTO> getAuditTrail(@PathVariable Long id) {
        log.debug("REST request to get AuditTrail : {}", id);
        return Optional.ofNullable(auditTrailRepository.findOne(id))
            .map(auditTrailMapper::entityToDto)
            .map(auditTrailDTO -> new ResponseEntity<>(
                auditTrailDTO,
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
    public List<AuditTrailDTO> searchAuditTrails(@PathVariable String query) {
        return StreamSupport
            .stream(auditTrailSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(auditTrailMapper::entityToDto)
            .collect(Collectors.toList());
    }
}
