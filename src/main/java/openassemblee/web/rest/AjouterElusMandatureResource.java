package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.net.URISyntaxException;
import java.util.List;
import javax.inject.Inject;
import javax.validation.Valid;
import openassemblee.domain.Mandature;
import openassemblee.service.AjouterElusMandatureService;
import openassemblee.service.AuditTrailService;
import openassemblee.service.dto.CandidatCorrespondanceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AjouterElusMandatureResource {

    private final Logger log = LoggerFactory.getLogger(
        AjouterElusMandatureResource.class
    );

    @Inject
    private AjouterElusMandatureService ajouterElusMandatureService;

    @Inject
    private AuditTrailService auditTrailService;

    @RequestMapping(
        value = "/ajouter-elus-mandature/prepareImport",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<List<CandidatCorrespondanceDTO>> prepareImport(
        @Valid @RequestBody String csv
    ) throws URISyntaxException {
        return ResponseEntity.ok(
            ajouterElusMandatureService.prepareImport(csv)
        );
    }

    @RequestMapping(
        value = "/ajouter-elus-mandature/ajouter/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> create(
        @PathVariable Long id,
        @Valid @RequestBody List<CandidatCorrespondanceDTO> candidats
    ) throws URISyntaxException {
        auditTrailService.logUpdate(candidats.toArray(), id);
        ajouterElusMandatureService.ajouterElus(id, candidats);
        return ResponseEntity.ok().build();
    }
}
