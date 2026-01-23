package se.jensen.linea.onsocial_app.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/")
    public String rootHealth() {
        return "OK"; // HTTP 200
    }
}
