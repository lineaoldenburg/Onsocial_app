package se.jensen.linea.onsocial_app.security;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.jensen.linea.onsocial_app.model.User;
import se.jensen.linea.onsocial_app.repository.UserRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String identifier) throws UsernameNotFoundException {
        return loadUserByAliasOrEmail(identifier);
    }

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

    // Check if alias exists (for live validation)
    public boolean aliasExists(String alias) {
        return userRepository.existsByAlias(alias);
    }

    // Check if email exists (for live validation)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}