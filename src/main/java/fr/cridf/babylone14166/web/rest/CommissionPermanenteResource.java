package fr.cridf.babylone14166.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.service.CommissionPermanenteService;
import fr.cridf.babylone14166.service.dto.CommissionPermanenteDTO;

@RestController
@RequestMapping("/api")
// TODO test
public class CommissionPermanenteResource {

    @Autowired
    private CommissionPermanenteService commissionPermanenteService;

    @RequestMapping(value = "/commission-permanente",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommissionPermanenteDTO> getCommissionPermanente() {
        return new ResponseEntity<>(commissionPermanenteService.getCommissionPermanente(), HttpStatus.OK);
    }

}
