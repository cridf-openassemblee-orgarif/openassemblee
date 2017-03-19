package fr.cridf.babylone14166.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthCheck {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String ok() {
        return "OK";
    }
}
