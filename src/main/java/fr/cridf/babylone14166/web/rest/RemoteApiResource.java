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

@RestController
@RequestMapping("/remote-api")
public class RemoteApiResource {

    @Value("${fakeData}")
    private boolean fakeData;

    @Autowired
    private TestDataInjector testDataInjector;

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/index-reset", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> resetIndex() {
        searchService.resetIndex();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/inject-all", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> injectAll() {
        if (fakeData) {
            testDataInjector.injectTestData();
            testDataInjector.injectOrganismes();
            searchService.resetIndex();
        }
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
