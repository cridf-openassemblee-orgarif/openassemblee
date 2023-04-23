package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.util.List;
import openassemblee.service.SearchService;
import openassemblee.web.rest.dto.SearchResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchResource {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/{searchToken}", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<SearchResultDTO>> search(
        @PathVariable String searchToken
    ) {
        return ResponseEntity.ok(searchService.search(searchToken));
    }
}
