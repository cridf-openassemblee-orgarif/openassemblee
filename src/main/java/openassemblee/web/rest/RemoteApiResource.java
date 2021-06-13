package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.config.data.TestDataInjector;
import openassemblee.domain.*;
import openassemblee.repository.*;
import openassemblee.service.SearchService;
import openassemblee.service.ShortUidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private MandatRepository mandatRepository;

    @Autowired
    private MandatureRepository mandatureRepository;

    @Autowired
    private CommissionThematiqueRepository commissionThematiqueRepository;

    @Autowired
    private AppartenanceCommissionPermanenteRepository appartenanceCommissionPermanenteRepository;

    @Autowired
    private FonctionExecutiveRepository fonctionExecutiveRepository;

    @Autowired
    private FonctionCommissionPermanenteRepository fonctionCommissionPermanenteRepository;

    @Autowired
    private GroupePolitiqueRepository groupePolitiqueRepository;

    @Autowired
    private ListeElectoraleRepository listeElectoraleRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private ReunionCaoRepository reunionCaoRepository;

    @Autowired
    private ReunionCommissionThematiqueRepository reunionCommissionThematiqueRepository;

    @Autowired
    private AutreMandatRepository autreMandatRepository;

    @Autowired
    private HemicyclePlanRepository hemicyclePlanRepository;

//    @PostConstruct
//    public void init() {
//        System.out.println("--------");
//        eluRepository.findAll().stream().map(e -> e.getListeCourt() + " <=> " + e.getListeElectorale()).distinct()
//            .sorted()
//            .collect(Collectors.toList())
//            .forEach(l -> System.out.println(l));
//        System.out.println("--------");
//    }

    private boolean migrationOk = false;

    @RequestMapping(value = "/migrate-mandature", method = RequestMethod.GET)
    public ResponseEntity<String> migrateMandature() {
        log.info("Migration mandature");

        // mandatureRepository.findAll().isEmpty() alone can be slow
        if (migrationOk || !mandatureRepository.findAll().isEmpty()) {
            return ResponseEntity.ok("Migration déjà faite");
        }

        migrationOk = true;

//        insert into mandature (id, annee, date_debut, current) values (1, 2015, '2015-04-15', true);
//        update appartenance_commission_permanente set mandature_id = 1;
//        update commission_thematique set mandature_id = 1;
//        update fonction_executive set mandature_id = 1;
//        update fonction_commission_permanente set mandature_id = 1;
//        update groupe_politique set mandature_id = 1;

        Mandature mandatureDraft = new Mandature();
        mandatureDraft.setAnneeDebut(2015);
        mandatureDraft.setAnneeFin(2021);
        mandatureDraft.setDateDebut(LocalDate.of(2015, 12, 18));
        mandatureDraft.setCurrent(true);
        Mandature mandature = mandatureRepository.save(mandatureDraft);

        handle(appartenanceCommissionPermanenteRepository, mandature);
        handle(autreMandatRepository, mandature);
        handle(commissionThematiqueRepository, mandature);
        handle(fonctionCommissionPermanenteRepository, mandature);
        handle(fonctionExecutiveRepository, mandature);
        handle(groupePolitiqueRepository, mandature);
        handle(hemicyclePlanRepository, mandature);
        handle(reunionCaoRepository, mandature);
        handle(reunionCommissionThematiqueRepository, mandature);
        handle(seanceRepository, mandature);

        Map<String, ListeElectorale> listeElectoraleMap = new HashMap<>();
        eluRepository.findAll().forEach(e -> {
            String listeElectoralKey = e.getListeCourt() + "///" + e.getListeElectorale();
            ListeElectorale listeElectorale = listeElectoraleMap.get(listeElectoralKey);
            if (listeElectorale == null && e.getListeElectorale() != null) {
                ListeElectorale listeElectoraleDraft = new ListeElectorale();
                listeElectoraleDraft.setNom(e.getListeElectorale());
                listeElectoraleDraft.setNomCourt(e.getListeCourt());
                listeElectoraleDraft.setMandature(mandature);
                listeElectorale = listeElectoraleRepository.save(listeElectoraleDraft);
                listeElectoraleMap.put(listeElectoralKey, listeElectorale);
            }
            Mandat mandat = new Mandat();
            mandat.setElu(e);
            mandat.setMandature(mandature);
            mandat.setDateDemission(e.getDateDemission());
            mandat.setMotifDemission(e.getMotifDemission());
            mandat.setListeElectorale(listeElectorale);
            mandat.setDateDebut(null); // on sait pas...
            mandat.setCodeDepartement(e.getCodeDepartement());
            mandat.setDepartement(e.getDepartement());
            mandatRepository.save(mandat);
        });
        log.info("Migration mandature OK");
        return ResponseEntity.ok("ok");
    }

    private <T> void handle(JpaRepository<T, Long> repo, Mandature mandature) {
        repo.findAll().forEach(i -> {
            try {
                Method m = i.getClass().getMethod("setMandature", Mandature.class);
                m.invoke(i, mandature);
                repo.save(i);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

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
        Stream<ShortUid> uuids = eluRepository.findAll()
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
