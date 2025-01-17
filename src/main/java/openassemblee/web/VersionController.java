package openassemblee.web;

import org.elasticsearch.common.io.Streams;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class VersionController {

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public ResponseEntity<String> version() throws IOException {
        String version = new String(
            Streams.copyToByteArray(
                getClass()
                    .getClassLoader()
                    .getResourceAsStream(
                        "dist/version.txt"
                    )
            )
        );
        return ResponseEntity.ok(version);
    }
}
