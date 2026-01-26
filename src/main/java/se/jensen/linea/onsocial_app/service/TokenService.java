package se.jensen.linea.onsocial_app.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

/**
 * TokenService skapar säkra JWT-tokens som används för autentisering i appen.
 * Token skickas istället för användarnamn och lösenord för varje request.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-22
 */
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Genererar JWT-token för att autentisera användaren.
     * Token innehåller användarens identitet och roller och är giltig i 1 timme.
     * String scope samlar användarens roll/roller vilket sen kan användas om användaren försöker använda en CRUD.
     * Matchar användarens roll med CRUD-metodens säkerhetsregler, då får användaren åtkomst till den metoden.
     *
     * @param authentication innehåller användarens identitet och roller.
     * @return En signerad JWT-token.
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}