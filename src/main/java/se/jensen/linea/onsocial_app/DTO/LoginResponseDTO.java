package se.jensen.linea.onsocial_app.DTO;

/**
 * Data som skickas till klienten vid lyckad inloggning.
 *
 * @param token Den JWT-token som genereras vid lyckad inloggning.
 * @param user  Användarens information.
 * @author Simeon
 * Dokumenterad: 2026-01-22
 */
public record LoginResponseDTO(
        String token,
        UserResponseDTO user
) {
}