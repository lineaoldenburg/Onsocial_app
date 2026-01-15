package se.jensen.linea.onsocial_app.security;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.jensen.linea.onsocial_app.model.User;

import java.util.Collection;
import java.util.List;

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