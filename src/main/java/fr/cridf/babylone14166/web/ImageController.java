package fr.cridf.babylone14166.web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import fr.cridf.babylone14166.repository.ImageRepository;

@Controller
@RequestMapping("/images")
public class ImageController {

    private final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageRepository imageRepository;

    @RequestMapping(value = "/:id", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<byte[]> createElu(@RequestParam Long id) throws URISyntaxException {
        try {
            return new HttpEntity<>(imageRepository.getImage(id));
        } catch (SQLException | IOException e) {
            log.error("Unable to get image", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
