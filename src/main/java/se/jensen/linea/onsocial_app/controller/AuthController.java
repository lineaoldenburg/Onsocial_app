package se.jensen.linea.onsocial_app.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import se.jensen.linea.onsocial_app.DTO.*;
import se.jensen.linea.onsocial_app.security.CustomUserDetails;
import se.jensen.linea.onsocial_app.security.CustomUserDetailsService;
import se.jensen.linea.onsocial_app.service.TokenService;
import se.jensen.linea.onsocial_app.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          CustomUserDetailsService customUserDetailsService,
                          TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO user = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        String identifier = loginRequest.alias() != null ? loginRequest.alias() : loginRequest.email();

        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("Please enter alias or valid email");
        }

        // Authenticate user
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identifier, loginRequest.password())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Wrong password, please try again");
        } catch (UsernameNotFoundException e) {
            String message = identifier.contains("@")
                    ? "No user found with email " + identifier
                    : "No user found with alias " + identifier;
            throw new UsernameNotFoundException(message);
        }

        // Generate JWT token
        String jwt = tokenService.generateToken(auth);

        // Get user details from Authentication object
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        UserResponseDTO userResponse = userService.getUserByIdOrThrow(customUserDetails.getId());

        // Return token and user info (frontend will save to localStorage)
        return ResponseEntity.ok(new LoginResponseDTO(jwt, userResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Frontend handles token-removal from localStorage
        return ResponseEntity.noContent().build();
    }


    //Boolean returns so I can check quick in frontend // Linea
    @GetMapping("/check-alias")
    public ResponseEntity<AvailabilityResponseDTO> checkAliasAvailable(@RequestParam String alias) {
        boolean exists = customUserDetailsService.aliasExists(alias);
        return ResponseEntity.ok(new AvailabilityResponseDTO(!exists));
    }

    @GetMapping("/check-email")
    public ResponseEntity<AvailabilityResponseDTO> checkEmailAvailable(@RequestParam String email) {
        boolean exists = customUserDetailsService.emailExists(email);
        return ResponseEntity.ok(new AvailabilityResponseDTO(!exists));
    }
}