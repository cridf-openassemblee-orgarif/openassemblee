package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.domain.HemicycleConfiguration;
import openassemblee.repository.HemicycleConfigurationRepository;
import openassemblee.repository.search.HemicycleConfigurationSearchRepository;
import openassemblee.service.HemicycleConfigurationRendererService;
import openassemblee.service.HemicycleConfigurationService;
import openassemblee.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing HemicycleConfiguration.
 */
@RestController
@RequestMapping("/api")
public class HemicycleConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(HemicycleConfigurationResource.class);

    @Inject
    private HemicycleConfigurationRepository hemicycleConfigurationRepository;

    @Inject
    private HemicycleConfigurationSearchRepository hemicycleConfigurationSearchRepository;

    @Inject
    private HemicycleConfigurationService hemicycleConfigurationService;

    /**
     * POST  /hemicycleConfigurations -> Create a new hemicycleConfiguration.
     */
    @RequestMapping(value = "/hemicycleConfigurations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HemicycleConfiguration> createHemicycleConfiguration(@Valid @RequestBody HemicycleConfiguration hemicycleConfiguration) throws URISyntaxException {
        log.debug("REST request to save HemicycleConfiguration : {}", hemicycleConfiguration);
        if (hemicycleConfiguration.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hemicycleConfiguration cannot already have an ID").body(null);
        }
        HemicycleConfiguration result = hemicycleConfigurationRepository.save(hemicycleConfiguration);
        hemicycleConfigurationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hemicycleConfigurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hemicycleConfiguration", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hemicycleConfigurations -> Updates an existing hemicycleConfiguration.
     */
    @RequestMapping(value = "/hemicycleConfigurations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HemicycleConfiguration> updateHemicycleConfiguration(@Valid @RequestBody HemicycleConfiguration hemicycleConfiguration) throws URISyntaxException {
        log.debug("REST request to update HemicycleConfiguration : {}", hemicycleConfiguration);
        if (hemicycleConfiguration.getId() == null) {
            return createHemicycleConfiguration(hemicycleConfiguration);
        }
        HemicycleConfiguration result = hemicycleConfigurationRepository.save(hemicycleConfiguration);
        hemicycleConfigurationSearchRepository.save(hemicycleConfiguration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hemicycleConfiguration", hemicycleConfiguration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hemicycleConfigurations -> get all the hemicycleConfigurations.
     */
    @RequestMapping(value = "/hemicycleConfigurations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HemicycleConfiguration> getAllHemicycleConfigurations() {
        log.debug("REST request to get all HemicycleConfigurations");
        return hemicycleConfigurationService.getAllSorted();
    }

    /**
     * GET  /hemicycleConfigurations/:id -> get the "id" hemicycleConfiguration.
     */
    @RequestMapping(value = "/hemicycleConfigurations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HemicycleConfiguration> getHemicycleConfiguration(@PathVariable Long id) {
        log.debug("REST request to get HemicycleConfiguration : {}", id);
        return Optional.ofNullable(hemicycleConfigurationRepository.findOne(id))
            .map(hemicycleConfiguration -> new ResponseEntity<>(
                hemicycleConfiguration,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hemicycleConfigurations/:id -> delete the "id" hemicycleConfiguration.
     */
    @RequestMapping(value = "/hemicycleConfigurations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHemicycleConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete HemicycleConfiguration : {}", id);
        hemicycleConfigurationRepository.delete(id);
        hemicycleConfigurationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hemicycleConfiguration", id.toString())).build();
    }

    /**
     * SEARCH  /_search/hemicycleConfigurations/:query -> search for the hemicycleConfiguration corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/hemicycleConfigurations/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HemicycleConfiguration> searchHemicycleConfigurations(@PathVariable String query) {
        return StreamSupport
            .stream(hemicycleConfigurationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
