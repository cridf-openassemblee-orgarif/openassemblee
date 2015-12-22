package fr.cridf.babylone14166.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.config.data.TestDataInjector;
import fr.cridf.babylone14166.service.IndexService;

@RestController
@RequestMapping("/api")
public class TestResource {

    @Autowired
    private TestDataInjector testDataInjector;

    @Autowired
    private IndexService indexService;

    @RequestMapping(value = "/index-reset", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> resetIndex() {
        indexService.resetIndex();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/data-inject", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<Void> search() {
        testDataInjector.init();
        return ResponseEntity.ok().build();
    }

}
