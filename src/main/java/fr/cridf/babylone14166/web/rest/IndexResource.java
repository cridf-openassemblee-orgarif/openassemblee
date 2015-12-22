package fr.cridf.babylone14166.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import fr.cridf.babylone14166.service.IndexService;

@RestController
@RequestMapping("/api")
public class IndexResource {

    @Autowired
    private IndexService indexService;

    @RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<Object>> search(@PathVariable String query) {
        return ResponseEntity.ok(indexService.search(query));
    }

}
