package se.jensen.linea.onsocial_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.linea.onsocial_app.model.User;

import java.util.Optional;

/**
 * UserRepository ärver metoder från Spring Data JPA.
 * Med "extends JpaRepository" undviker vi boilerplate-kod och ärver:
 * save(), findAll(), findById() osv.
 * Vi får alltså färdiga metoder för att kommunicera med databasen.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-22
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * @return User-objekt med alias som matchar parametern eller null.
     */
    Optional<User> findByAlias(String alias);

    /**
     * @return User-objekt med e-postadress som matchar parametern eller null.
     */
    Optional<User> findByEmail(String email);

    /**
     * Boolean för live-validation av alias i Frontend.
     *
     * @return "True" eller "false", om en användare med alias finns eller inte.
     */
    boolean existsByAlias(String alias);

    /**
     * Boolean för live-validation av e-postadress i Frontend.
     *
     * @return "True" eller "false", om en användare med e-postadress finns eller inte.
     */
    boolean existsByEmail(String email);
}
