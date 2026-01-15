package se.jensen.linea.onsocial_app.DTO;

public record LoginResponseDTO(
        String token,
        UserResponseDTO user
) {
}