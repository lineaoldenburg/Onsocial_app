package se.jensen.linea.onsocial_app.DTO;

public record UserResponseDTO(Long id, String alias, String email, String firstName, String lastName,
                              String profilePicture, String role) {
}
