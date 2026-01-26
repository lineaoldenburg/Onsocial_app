package se.jensen.linea.onsocial_app.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import se.jensen.linea.onsocial_app.model.Post;
import se.jensen.linea.onsocial_app.repository.PostRepository;

@Service
public class PostSecurityService {
    private final PostRepository postRepository;

    public PostSecurityService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    //Kanske lägga till throws exception?
    public boolean isPostOwner(Long postId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        Post post = postRepository.findById(postId).orElse(null);

        return post != null && post.getUser().getId().equals(userId);
    }
}
