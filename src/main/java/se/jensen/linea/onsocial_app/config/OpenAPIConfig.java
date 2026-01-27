package se.jensen.linea.onsocial_app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Den här klassen talar om för Swagger/OpenAPI-dokumentation att
 * programmet använder JWT-tokens för autentisering och för att
 * komma åt skyddade endpoints.
 * I swagger-ui visas detta med "🔓 Authorize".
 */
@OpenAPIDefinition(
        info = @Info(title = "OnSocial API med JWT", version = "1.0"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenAPIConfig {

}