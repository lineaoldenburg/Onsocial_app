package se.jensen.linea.onsocial_app.security;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.jensen.linea.onsocial_app.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Klassen omvandlar ett User-objekt till ett format
 * som Spring Security förstår och kan arbeta med.
 * <p>
 * Klassen agerar som en brygga mellan User och UserDetails.
 * <p>
 * UserDetails innehåller metoder som getUsername, getPassword och getAuthorities.
 * Dessa metoder används vid autentisering.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-26
 */
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String alias;
    private final String email;
    private final String password;
    private final String role;

    // Constructor
    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.alias = user.getAlias();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    /**
     * Denna metod omvandlar användarens roll till ett format som Spring Security
     * förstår för behörighetskontroller.
     *
     * @return En lista med användarens roller.
     */
    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public @NonNull String getPassword() {
        return password;
    }

    @Override
    public @NonNull String getUsername() {
        return alias;
    }

    public @NonNull Long getId() {
        return id;
    }

    public @NonNull String getEmail() {
        return email;
    }
}