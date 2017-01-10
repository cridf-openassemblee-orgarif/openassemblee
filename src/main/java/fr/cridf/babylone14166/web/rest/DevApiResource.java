package fr.cridf.babylone14166.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cridf.babylone14166.config.data.TestDataInjector;
import fr.cridf.babylone14166.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/dev-api")
// FIXME remote
// un inject all
public class DevApiResource {

    @Value("${fakeData}")
    private boolean fakeData;

    @Value("${autoResetIndex}")
    private boolean autoResetIndex;

    @Autowired
    private TestDataInjector testDataInjector;

    @Autowired
    private SearchService searchService;

    @PostConstruct
    public void init() {
        if (autoResetIndex) {
            searchService.resetIndex();
        }
    }

    @RequestMapping(value = "/index-reset", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> resetIndex() {
        searchService.resetIndex();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/inject-test-data", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> injectTestData() {
        if (fakeData) {
            testDataInjector.injectTestData();
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/inject-organismes", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> injectOrganismes() {
        if (fakeData) {
            testDataInjector.injectOrganismes();
        }
        return ResponseEntity.ok().build();
    }

}
