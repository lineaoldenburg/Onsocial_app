package se.jensen.linea.onsocial_app.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Enkel health check-controller som används av Koyeb
 * för att verifiera att applikationen är igång och svarar.
 * <p>
 * Endpunkten returnerar "OK" med HTTP-status 200.
 *
 * @author Linea
 */

@RestController
public class HealthCheckController {
    @GetMapping("/")
    public String rootHealth() {
        return "OK"; // HTTP 200
    }
}
