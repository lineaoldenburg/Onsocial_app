package se.jensen.linea.onsocial_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.linea.onsocial_app.model.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
}
