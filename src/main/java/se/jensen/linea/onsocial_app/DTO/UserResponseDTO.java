package se.jensen.linea.onsocial_app.DTO;

/**
 * DTO för att returnera användarinformation till klienten.
 * UserResponseDTO innehåller inte mer information än nödvändigt, skyddar känslig data.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-21
 */
public record UserResponseDTO(Long id, String alias, String email, String firstName, String lastName,
                              String profilePicture, String role) {


}
