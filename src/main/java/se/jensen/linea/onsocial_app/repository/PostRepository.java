package se.jensen.linea.onsocial_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.linea.onsocial_app.model.Post;

import java.util.List;

/**
 * PostRepository ärver metoder från Spring Data JPA.
 * Med "extends JpaRepository" undviker vi boilerplate-kod och ärver:
 * save(), findAll(), findById() osv.
 * Vi får alltså färdiga metoder för att kommunicera med databasen.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-22
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
}
