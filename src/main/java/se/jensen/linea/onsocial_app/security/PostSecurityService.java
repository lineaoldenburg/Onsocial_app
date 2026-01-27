package se.jensen.linea.onsocial_app.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import se.jensen.linea.onsocial_app.model.Post;
import se.jensen.linea.onsocial_app.repository.PostRepository;

/**
 * Klassen hanterar säkerhetskontroller för inlägg.
 * <p>
 * Denna klass kontrollerar om en användare har rätt att ändra eller ta bort
 * sina egna inlägg.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-26
 */
@Service
public class PostSecurityService {
    private final PostRepository postRepository;

    public PostSecurityService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Kontrollerar om den inloggade användaren äger ett specifikt inlägg.
     * <p>
     * Metoden kollar om användaren är inloggad och sedan jämför användarens ID
     * med ID:t på den som skapade inlägget.
     *
     * @param postId         ID:t för inlägget som ska kontrolleras.
     * @param authentication information om den inloggade användaren.
     * @return true om användaren äger inlägget, annars false.
     */
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
