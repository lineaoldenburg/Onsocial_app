package se.jensen.linea.onsocial_app.mapper;

import se.jensen.linea.onsocial_app.DTO.PostRequestDTO;
import se.jensen.linea.onsocial_app.DTO.PostResponseDTO;
import se.jensen.linea.onsocial_app.model.Post;
import se.jensen.linea.onsocial_app.model.User;

/**
 * Mapper-klass som omvandlar mellan Post-entity och Post-DTO:er.
 * Används för att hålla isär databasskiktet och det som skickas till/från klienten.
 */
public class PostMapper {

    /**
     * Skapar en Post-entity från en PostRequestDTO.
     * Från klienten -> till backend.
     * Används när klienten skickar in data för att skapa ett nytt inlägg.
     * Skapar eller uppdaterar nytt inlägg
     * (Request = klienten ber om något)
     *
     * @param dto  data från klienten (titel och innehåll)
     * @param user användaren som äger inlägget
     * @return en Post-entity redo att sparas i databasen
     */
    public static Post toEntity(PostRequestDTO dto, User user) {
        Post post = new Post();
        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setUser(user);
        return post;
    }

    /**
     * Skapar en PostResponseDTO från en Post-entity.
     * Från backend -> till klienten
     * Används när backend skickar tillbaka ett inlägg till klienten.
     * Response = backend svarar tillbacka
     *
     * @param post Post-entity som ska omvandlas
     * @return en DTO med den information klienten behöver
     */
    public static PostResponseDTO toDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getAlias(),
                post.getCreated()
        );
    }
}
