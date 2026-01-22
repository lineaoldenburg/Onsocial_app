package se.jensen.linea.onsocial_app.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO för att ta emot användarinformation av klienten.
 * UserRequestDTO efterfrågar inte mer info av klienten än nödvändigt.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-21
 */
public record UserRequestDTO(

        /**Alias får inte vara tom. Måste vara mellan 3-50 tecken långt.*/
        @NotBlank(message = "Alias is required")
        @Size(min = 3, max = 50, message = "Alias must be between 3 and 50 characters")
        String alias,

        /**Email får inte vara tom.*/
        @NotBlank(message = "Email address is required")
        @Email(message = "Please provide a valid email address")
        String email,

        /**Förnamn får inte vara tom. Måste innehålla mellan 2-50 tecken.*/
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 3 and 50 characters")
        String firstName,

        /**Efternamn får inte vara tom. Måste innehålla mellan 2-50 tecken.*/
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 3 and 50 characters")
        String lastName,

        /**Lösenord får inte vara tom. Måste innehålla mellan 8-22 tecken.*/
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 22, message = "Password must be between 8 and 22 characters")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,22}$",
                message = "Password must contain at least one uppercase letter and one number"
        )
        String password,

        /**Inget krav på profilbild.*/
        String profilePicture
) {
}
