package se.jensen.linea.onsocial_app.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.jensen.linea.onsocial_app.DTO.PostRequestDTO;
import se.jensen.linea.onsocial_app.DTO.PostResponseDTO;
import se.jensen.linea.onsocial_app.security.CustomUserDetails;
import se.jensen.linea.onsocial_app.security.CustomUserDetailsService;
import se.jensen.linea.onsocial_app.service.PostService;

import java.util.List;

/**
 * PostController hanterar klientens "requests" till /posts.
 * Klassen är ett REST API-lager och agerar som en mellanhand mellan klienten och affärslogiken (PostService).
 * <p>
 * Den annoteras med RestController för att indikera till Spring att den ska
 * hantera HTTP-requests och returnera JSON till klienten.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-22
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CustomUserDetailsService customUserDetailsService;

    public PostController(PostService postService, CustomUserDetailsService customUserDetailsService) {
        this.postService = postService;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Hämta alla inlägg.
     *
     * @return HTTP 200 (ok) och en lista av PostResponseDTO.
     */
    @GetMapping("/findall")
    public ResponseEntity<List<PostResponseDTO>> findAll() {
        List<PostResponseDTO> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    /**
     * Hämta alla inlägg av en specifik användare.
     *
     * @param user_id Användarens unika ID.
     * @return HTTP 200 (ok) och en lista av PostResponseDTO eller HTTP 404 (not found).
     */
    @GetMapping("/byuser/{user_id}")
    public ResponseEntity<List<PostResponseDTO>> findByUser(@PathVariable Long user_id) {
        List<PostResponseDTO> posts = postService.findAllByUserId(user_id);

        if (posts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(posts);
    }

    /**
     * Hitta ett specifikt inlägg med ett ID.
     *
     * @param post_id Inläggets unika ID.
     * @return HTTP 200 (ok) och en PostResponseDTO.
     */
    @GetMapping("/specific/{post_id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long post_id) {
        PostResponseDTO post = postService.findById(post_id);
        return ResponseEntity.ok(post);
    }

    /**
     * Skapa ett nytt inlägg.
     *
     * @param postDto        Vi hämtar data från klienten via PostRequestDTO.
     * @param authentication Innehåller information om den inloggade användaren, tack vare SecurityConfig.
     * @return HTTP 201 (created) och en PostResponseDTO.
     */
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostResponseDTO> addPost(
            @Valid @RequestBody PostRequestDTO postDto,
            Authentication authentication) {

        // Get username from JWT token
        String username = authentication.getName();

        // Load full user details
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

        Long userId = userDetails.getId();
        PostResponseDTO createdPost = postService.createPost(userId, postDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    /**
     * Uppdaterar ett inlägg.
     *
     * @param id             Inläggets ID.
     * @param postRequestDTO Vi hämtar data från klienten via PostRequestDTO.
     * @return HTTP 200 (ok) och en PostResponseDTO.
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("@postSecurityService.isPostOwner(#id, authentication) or hasRole('ADMIN')")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDTO postRequestDTO) {

        PostResponseDTO updatedPost = postService.updatePost(id, postRequestDTO);
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * Radera ett inlägg.
     *
     * @param id Inläggets ID.
     * @return HTTP 204 (no content) eller HTTP 404 (not found)
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@postSecurityService.isPostOwner(#id, authentication) or hasRole('ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}