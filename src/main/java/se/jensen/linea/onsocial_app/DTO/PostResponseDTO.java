package se.jensen.linea.onsocial_app.DTO;

import java.time.Instant;

/**
 * DTO för att returnera postinformation till klienten.
 * PostResponseDTO innehåller inte mer information än nödvändigt, skyddar känslig data.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-21
 */
public record PostResponseDTO(Long id, String title, String content, Long userId, String userAlias, Instant created) {
}
