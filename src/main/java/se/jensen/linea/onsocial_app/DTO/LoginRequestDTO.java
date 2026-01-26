package se.jensen.linea.onsocial_app.DTO;

/**
 * Inloggningsuppgifter som skickas från klienten till servern.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-22
 */
public record LoginRequestDTO(String alias, String email, String password) {
}
