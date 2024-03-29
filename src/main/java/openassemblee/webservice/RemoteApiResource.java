package openassemblee.webservice;

import com.codahale.metrics.annotation.Timed;
import java.util.stream.Stream;
import openassemblee.config.data.TestDataInjector;
import openassemblee.domain.ShortUid;
import openassemblee.repository.*;
import openassemblee.service.SearchService;
import openassemblee.service.ShortUidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/remote")
public class RemoteApiResource {

    private final Logger log = LoggerFactory.getLogger(RemoteApiResource.class);

    @Value("${fakeData}")
    private boolean fakeData;

    @Autowired
    private TestDataInjector testDataInjector;

    @Autowired
    private SearchService searchService;

    @Autowired
    private OrganismeRepository organismeRepository;

    @Autowired
    private AppartenanceOrganismeRepository appartenanceOrganismeRepository;

    @Autowired
    private EluRepository eluRepository;

    @Autowired
    private ShortUidService shortUidService;

    private boolean migrationOk = false;

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

    @RequestMapping(value = "/set-uids", method = RequestMethod.GET)
    public ResponseEntity<String> setUids() {
        Stream<ShortUid> uuids = eluRepository
            .findAll()
            .stream()
            .filter(e -> e.getUid() == null)
            .map(e -> {
                ShortUid uid = shortUidService.createShortUid();
                e.setUid(uid.getUid());
                e.setShortUid(uid.getShortUid());
                eluRepository.save(e);
                return uid;
            });
        return ResponseEntity.ok("Changed " + uuids.count());
    }

    @RequestMapping(value = "/inject-organismes", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> injectOrganismes() {
        if (fakeData) {
            testDataInjector.injectOrganismes();
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/clean-organismes", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<String> cleanOrganismes() {
        appartenanceOrganismeRepository.deleteAll();
        organismeRepository.deleteAll();
        return ResponseEntity.ok("ok");
    }
}
