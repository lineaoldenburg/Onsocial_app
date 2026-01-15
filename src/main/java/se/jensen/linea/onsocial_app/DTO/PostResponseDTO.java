package se.jensen.linea.onsocial_app.DTO;

import java.time.Instant;

public record PostResponseDTO(Long id, String title, String content, Long userId, String userAlias, Instant created) {
}
