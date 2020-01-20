package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import openassemblee.service.HemicycleService;
import openassemblee.web.rest.dto.HemicycleDTO;
import openassemblee.web.rest.dto.HemicycleDefinition;
import org.elasticsearch.common.io.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class HemicycleResource {

    @Autowired
    private HemicycleService hemicycleService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/hemicycle",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    // FIXMENOW ne pas tout recalculer
    public HemicycleDTO hemicycle() throws IOException {
        //    private double largeur = 24.0;
        //    private double prof = 14.0;
        //    private double largeur = 30.0;
        //    private double prof = 20.0;
        String json = new String(Streams.copyToByteArray(getClass().getClassLoader()
            .getResourceAsStream("hemicycle/hemicycle-disposition-2020.json")));
        HemicycleDefinition hd = objectMapper.readValue(json, HemicycleDefinition.class);
        return hemicycleService.hemicycle(hd);
    }
}
