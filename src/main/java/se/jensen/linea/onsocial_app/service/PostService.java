package se.jensen.linea.onsocial_app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.jensen.linea.onsocial_app.DTO.PostRequestDTO;
import se.jensen.linea.onsocial_app.DTO.PostResponseDTO;
import se.jensen.linea.onsocial_app.mapper.PostMapper;
import se.jensen.linea.onsocial_app.model.Post;
import se.jensen.linea.onsocial_app.model.User;
import se.jensen.linea.onsocial_app.repository.PostRepository;
import se.jensen.linea.onsocial_app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * PostService innehåller alla metoder vi behöver anropa i PostController.
 * Den innehåller alltså affärslogiken mellan PostControllern och databasen.
 * Klassen annoteras med Service, vilket gör att Spring skapar en Singleton som kan injiceras i andra klasser.
 * <p>
 * Transactional gör att metoderna körs inom en databastransaktion.
 * Antingen lyckas alla metoder med annotering eller ingen av dem.
 * <p>
 * readOnly används för att signalera att metoderna inte ska uppdatera databasen.
 * Vilket gör programmet mer resurseffektivt och undviker fel.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-22
 */
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * Hämta alla inlägg i databasen.
     *
     * @return Lista av PostResponseDTO.
     */
    @Transactional(readOnly = true)
    public List<PostResponseDTO> findAll() {
        return postRepository.findAll()
                .stream()
                .map(PostMapper::toDTO)
                .toList();
    }


    /**
     * Hämta alla inlägg från en användare.
     *
     * @param userId Användarens id.
     * @return Lista av PostResponseDTO.
     */
    @Transactional(readOnly = true)
    public List<PostResponseDTO> findAllByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();

        for (Post post : posts) {
            PostResponseDTO dto = new PostResponseDTO(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getUser().getId(),
                    post.getUser().getAlias(),
                    post.getCreated()
            );
            postResponseDTOList.add(dto);
        }
        return postResponseDTOList;
    }

    /**
     * Hitta ett specifikt inlägg med ett ID.
     *
     * @param id Inläggets id.
     * @return En PostResponseDTO.
     * @throws RuntimeException on inlägget inte hittas.
     */
    @Transactional(readOnly = true)
    public PostResponseDTO findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getAlias(),
                post.getCreated()
        );
    }

    /**
     * Skapa ett nytt inlägg för en användare.
     *
     * @param userId  Användarens id.
     * @param postDto Vi hämtar data från klienten via PostRequestDTO.
     * @return En PostResponseDTO med information om postens innehåll.
     */
    @Transactional
    public PostResponseDTO createPost(Long userId, PostRequestDTO postDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Post post = new Post();
        post.setTitle(postDto.title());
        post.setContent(postDto.content());
        post.setUser(user);

        Post savedPost = postRepository.save(post);

        return new PostResponseDTO(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getUser().getId(),
                savedPost.getUser().getAlias(),
                savedPost.getCreated()
        );
    }

    /**
     * Uppdatera ett inlägg.
     * Söka upp ett inlägg med dess id och få uppdaterade uppgifterna från klienten.
     *
     * @param id             Inläggets ID.
     * @param postRequestDTO Vi hämtar data från klienten via PostRequestDTO.
     * @return En PostResponseDTO med information om postens innehåll.
     */
    @Transactional
    public PostResponseDTO updatePost(Long id, PostRequestDTO postRequestDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        post.setTitle(postRequestDTO.title());
        post.setContent(postRequestDTO.content());

        Post updatedPost = postRepository.save(post);

        return new PostResponseDTO(
                updatedPost.getId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                updatedPost.getUser().getId(),
                updatedPost.getUser().getAlias(),
                updatedPost.getCreated()
        );
    }

    /**
     * Radera inlägg.
     *
     * @param id Inläggets id.
     * @return true om posten har tagits bort, false annars.
     */
    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
}