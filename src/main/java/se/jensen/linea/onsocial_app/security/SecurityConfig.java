package se.jensen.linea.onsocial_app.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

/**
 * Klassen är säkerhetscentralen för applikationen.
 * Hanterar åtkomst baserat på behörigheter och autentisering.
 * Vissa delar är öppna för publiken och andra är stängda.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-26
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    public SecurityConfig() {

    }

    /**
     * Säkerhetsregler för HTTP-förfrågningar.
     * Bestämmer vilka endpoints som är publika och vilka som kräver autentisering.
     *
     * @param http Ett verktyg från Spring som vi använder för att bygga upp reglerna
     *             (t.ex. vilka URL:er som ska vara öppna).
     * @return Den färdiga säkerhetskedjan som Spring kommer använda för att kontrollera anrop.
     * @throws Exception kastas om något går fel under konfigurationen av säkerhetsreglerna.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {
                })
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/posts/findall").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/check-alias").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/check-email").permitAll()
                        .requestMatchers(HttpMethod.POST, "/upload/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/find_all").permitAll()

                        // Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Actuator endpoints for health check
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    /**
     * Bestämmer vilka webbadresser (origins) som får lov att kommunicera med vårt API.
     *
     * @return En konfiguration som tillåter specifika adresser som t.ex. localhost:5173.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5174", "http://localhost:5173", "http://jealous-charlotte-linealicia-21fed96d.koyeb.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        // configuration.setAllowCredentials(true); Only needed if we make JWT sessions in cookie header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Ett verktyg för att kryptera lösenord.
     * Vi sparar aldrig riktiga lösenord i databasen, bara krypterade versioner.
     *
     * @return En algoritm (BCrypt) som används för att hash-lagra lösenordet säkert.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Metoden sköter inloggningsprocessen.
     * Den kontrollerar om användarnamn och lösenord stämmer när någon loggar in.
     *
     * @param configuration Springs inbyggda regelbok som talar om hur säkerheten ska sättas upp.
     * @return Motorn som utför kontrollen av inloggningen.
     * @throws Exception Kastas om något går fel när Spring försöker bygga ihop inloggningsmotorn.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Hämtar och förbereder de hemliga nycklarna (RSA) som används för att
     * signera och verifiera våra JWT-token.
     *
     * @param privateKey Den hemliga text-strängen från vår inställningsfil (.env)
     *                   som används för att signera tokens.
     * @param publicKey  Den publika text-strängen som används för att verifiera tokens.
     * @return Ett KeyPair-objekt som håller i både den privata och publika nyckeln.
     * @throws Exception kastas om något fel inträffar vid formatering eller om RSA-algoritmen inte kan läsa dem.
     */
    @Bean
    public KeyPair keyPair(
            @Value("${jwt.private-key}") String privateKey,
            @Value("${jwt.public-key}") String publicKey
    ) throws Exception {
        byte[] privateBytes = Base64.getDecoder().decode(privateKey);
        byte[] publicBytes = Base64.getDecoder().decode(publicKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privKey = keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(privateBytes)
        );
        PublicKey pubKey = keyFactory.generatePublic(
                new X509EncodedKeySpec(publicBytes)
        );

        return new KeyPair(pubKey, privKey);
    }

    /**
     * Paketerar om våra RSA-nycklar till ett format som JWT-biblioteket förstår.
     *
     * @param keyPair De nycklar vi skapade i metoden ovan.
     * @return En källa för JSON Web Keys (JWK) som används vid kryptering.
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID("jwt-key-1")
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * Skapar verktyg som signerar och låser våra JWT-token (låser innehållet så att ingen kan ändra det).
     *
     * @param jwkSource Källan till nycklarna.
     * @return En encoder-instans som kan skapa och signera nya, säkra tokens till användaren.
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * Skapar verktyget som "läser"/verifierar och låser upp tokens.
     *
     * @param keyPair Objektet som innehåller våra nycklar (här används bara den publika nyckeln).
     * @return En decoder-instans som kan verifiera att tokens är äkta.
     */
    @Bean
    public JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder
                .withPublicKey((RSAPublicKey) keyPair.getPublic())
                .build();
    }

    /**
     * Hjälper Spring att förstå hur den ska läsa informationen inuti en token,
     * så att den vet vilka rättigheter (roles/scopes) användaren har.
     *
     * @return En översättare som gör om informationen i en JWT till rättigheter som Spring Security förstår.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return authenticationConverter;
    }
}
