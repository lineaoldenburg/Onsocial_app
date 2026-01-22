package se.jensen.linea.onsocial_app.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO för att ta emot postinformation från klienten.
 * PostRequestDTO efterfrågar inte mer info av klienten än nödvändigt.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-21
 */
public record PostRequestDTO(
        /**
         * Titeln får inte vara tom. Måste vara mellan 3-100 tecken långt.
         */
        @NotBlank(message = "Titel is not allowed to be empty")
        @Size(min = 3, max = 100, message = "Title must be between 3 & 100 characters")
        String title,

        /**
         * Innehållet får inte vara tomt. Måste vara mellan 10-5000 tecken långt.
         */
        @NotBlank(message = "Content is not allowed to be empty")
        @Size(min = 10, max = 5000, message = "Content must be between 10 & 5000 characters long")
        String content
) {
}