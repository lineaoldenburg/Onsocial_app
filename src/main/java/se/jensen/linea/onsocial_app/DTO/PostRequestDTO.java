package se.jensen.linea.onsocial_app.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequestDTO(
        @NotBlank(message = "Titel is not allowed to be empty")
        @Size(min = 3, max = 100, message = "Title must be between 3 & 100 characters")
        String title,

        @NotBlank(message = "Content is not allowed to be empty")
        @Size(min = 10, max = 5000, message = "Content must be between 10 & 5000 characters long")
        String content
) {
}