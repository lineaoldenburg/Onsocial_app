package se.jensen.linea.onsocial_app.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.jensen.linea.onsocial_app.DTO.PostRequestDTO;
import se.jensen.linea.onsocial_app.DTO.PostResponseDTO;
import se.jensen.linea.onsocial_app.security.CustomUserDetails;
import se.jensen.linea.onsocial_app.security.CustomUserDetailsService;
import se.jensen.linea.onsocial_app.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CustomUserDetailsService customUserDetailsService;

    public PostController(PostService postService, CustomUserDetailsService customUserDetailsService) {
        this.postService = postService;
        this.customUserDetailsService = customUserDetailsService;
    }

    // Get all posts
    @GetMapping("/findall")
    public ResponseEntity<List<PostResponseDTO>> findAll() {
        List<PostResponseDTO> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    // Get all posts by user
    @GetMapping("/byuser/{user_id}")
    public ResponseEntity<List<PostResponseDTO>> findByUser(@PathVariable Long user_id) {
        List<PostResponseDTO> posts = postService.findAllByUserId(user_id);

        if (posts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(posts);
    }

    // Get one specific post
    @GetMapping("/specific/{post_id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long post_id) {
        PostResponseDTO post = postService.findById(post_id);
        return ResponseEntity.ok(post);
    }

    // Create new post
    @PostMapping("/add")
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

    // Update post
    @PutMapping("/update/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDTO postRequestDTO) {

        PostResponseDTO updatedPost = postService.updatePost(id, postRequestDTO);
        return ResponseEntity.ok(updatedPost);
    }

    // Delete post
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}