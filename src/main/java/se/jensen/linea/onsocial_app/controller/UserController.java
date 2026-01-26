package se.jensen.linea.onsocial_app.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.jensen.linea.onsocial_app.DTO.UserRequestDTO;
import se.jensen.linea.onsocial_app.DTO.UserResponseDTO;
import se.jensen.linea.onsocial_app.service.UserService;

import java.util.List;

/**
 * * UserController hanterar klientens "requests" till /users.
 * * Klassen är ett REST API-lager och agerar som en mellanhand mellan klienten och affärslogiken (UserService).
 * * <p>
 * * Den annoteras med RestController för att indikera till Spring att den ska
 * * hantera HTTP-requests och returnera JSON till klienten.
 * *
 * * @author Simeon
 * * Dokumenterad: 2026-01-22
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Hämta alla användare.
     *
     * @return HTTP 200 (ok) och en lista av UserResponseDTO.
     */
    @GetMapping("/find_all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Hämta en användare med ett ID.
     *
     * @param id Användarens ID.
     * @return HTTP 200 (ok) och en UserResponseDTO.
     */
    @GetMapping("find_specific/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserByIdOrThrow(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Uppdatera en användare.
     *
     * @param id             Användarens ID.
     * @param userRequestDTO Informationen som ska uppdateras.
     * @return HTTP 200 (ok) och en UserResponseDTO.
     */
    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {

        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Radera en användare.
     *
     * @param id Användarens ID.
     * @return HTTP 204 (no content) eller HTTP 404 (not found).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}