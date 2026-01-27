package se.jensen.linea.onsocial_app.security;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.jensen.linea.onsocial_app.model.User;
import se.jensen.linea.onsocial_app.repository.UserRepository;

import java.util.Optional;

/**
 * Klassen används av Spring Security för att hitta och ladda användaren från databasen vid inloggning.
 * Det är alltså en brygga mellan Spring Security och databasen.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-26
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obligatoriskt kontraktet från UserDetailsService.
     * Metoden anropas automatiskt av Spring Security vid inloggning.
     * Den tar emot det användaren skriver in (alias eller email)
     * och letar upp användaren i databasen.
     * <p>
     * Vi skapar metoden loadUserByAliasOrEmail nedanför som gör själva jobbet.
     *
     * @param identifier användarens alias eller email.
     * @return ett UserDetails-objekt med användarens information.
     * @throws UsernameNotFoundException om användaren inte hittas i databasen.
     */
    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String identifier) throws UsernameNotFoundException {
        return loadUserByAliasOrEmail(identifier);
    }

    /**
     * Denna metod gör det möjligt för loadUserByUsername att identifiera användare med alias eller email.
     *
     * @param identifier användarens alias eller email.
     * @return Antingen en hämtad användare genom alias eller email, eller ett felmeddelande.
     * @throws UsernameNotFoundException om användaren inte hittas i databasen.
     */
    public @NonNull UserDetails loadUserByAliasOrEmail(@NonNull String identifier) throws UsernameNotFoundException {
        // Try first to find by alias
        Optional<User> userByAlias = userRepository.findByAlias(identifier);
        if (userByAlias.isPresent()) {
            return new CustomUserDetails(userByAlias.get());
        }

        // If not found try email
        Optional<User> userByEmail = userRepository.findByEmail(identifier);
        if (userByEmail.isPresent()) {
            return new CustomUserDetails(userByEmail.get());
        }

        // Not found
        throw new UsernameNotFoundException("User not found with alias or email: " + identifier);
    }

    /**
     * Metoden kollar om alias redan finns i databasen, validerar användarens input i realtid.
     * Detta sker i frontend.
     *
     * @param alias användarens alias.
     * @return true om alias finns, annars false.
     */
    public boolean aliasExists(String alias) {
        return userRepository.existsByAlias(alias);
    }

    /**
     * Metoden kollar om email redan finns i databasen, validerar användarens input i realtid.
     * Detta sker i frontend.
     *
     * @param email användarens email.
     * @return true om email finns, annars false.
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}