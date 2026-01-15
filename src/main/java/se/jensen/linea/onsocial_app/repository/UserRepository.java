package se.jensen.linea.onsocial_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.linea.onsocial_app.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAlias(String alias);

    Optional<User> findByEmail(String email);

    // Booleans for live validation in frontend
    boolean existsByAlias(String alias);

    boolean existsByEmail(String email);
}
