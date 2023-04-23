package openassemblee.web;

import com.codahale.metrics.annotation.Timed;
import java.util.List;
import openassemblee.service.inconsistency.InconsistencyService;
import openassemblee.web.rest.dto.InconsistenciesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class InconsistencyResource {

    @Autowired
    private InconsistencyService inconsistencyService;

    @RequestMapping(
        value = "/inconsistency",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<List<InconsistenciesDTO>> getInconsistencies() {
        return new ResponseEntity<>(
            inconsistencyService.getInconsistencies(),
            HttpStatus.OK
        );
    }
}
