package se.jensen.linea.onsocial_app.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(

        @NotBlank(message = "Alias is required")
        @Size(min = 3, max = 50, message = "Alias must be between 3 and 50 characters")
        String alias,

        @NotBlank(message = "Email address is required")
        @Email(message = "Please provide a valid email address")
        String email,

        @NotBlank(message = "First name is required")
        @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
        String lastName,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 22, message = "Password must be between 8 and 22 characters")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,22}$",
                message = "Password must contain at least one uppercase letter and one number"
        )
        String password,

        String profilePicture
) {
}
