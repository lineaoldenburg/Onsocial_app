package se.jensen.linea.onsocial_app.security;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Klassen förbereder Cloudinary för bilduppladdning.
 * Hämtar inloggningsuppgifter från application.properties och
 * skapar en Cloudinary-instans som andra klasser kan använda.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-26
 */
@Configuration
public class CloudinaryConfig {

    /**
     * Cloudinary-instansens namn, hämtad från konfigurationsfilen (application.properties).
     */
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    /**
     * Cloudinary API-nyckel hämtat från konfigurationsfilen (application.properties).
     */
    @Value("${cloudinary.api-key}")
    private String apiKey;

    /**
     * Cloudinary API-secret hämtat från konfigurationsfilen (application.properties).
     */
    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    /**
     * Metoden läser in cloud name, api key och api secret
     * och använder dem för att skapa en fungerande Cloudinary-koppling.
     *
     * @return ett Cloudinary-objekt klart att användas för bilduppladdning.
     */
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }
}
