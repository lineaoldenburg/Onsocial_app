package se.jensen.linea.onsocial_app.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Klassen representerar en användare.
 * Den innehåller de attribut som speglar kolumner i databasen.
 * En user kan ha flera posts.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-21
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Unikt ID för användaren, genereras automatiskt av databasen.
     * Referens till databasens namn "user_id".
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * Användaren förnamn.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * Användarens efternamn.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * Användarens e-postadress.
     * Måste vara unik (två användare kan inte ha samma e-postadress).
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Användarens användarnamn.
     * Måste vara unik (två användare kan inte ha samma användarnamn).
     */
    @Column(unique = true, nullable = false)
    private String alias;

    /**
     * Användarens lösenord.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Användarens profilbild.
     */
    private String profilePicture;

    /**
     * Användarens roll.
     */
    @Column(nullable = false)
    private String role = "USER";

    /**
     * Lista över alla poster användaren har skapat.
     * En användare kan ha flera poster (One-to-Many relation).
     * Om användaren tas bort, raderas alla poster automatiskt (CascadeType.REMOVE).
     */
    @OneToMany(mappedBy = "user"
            , cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Post> posts;

    //Constructors
    public User() {

    }

    public User(long id, String firstName, String lastName, String email, String alias, String password, String roll, String profilePicture) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.alias = alias;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    //getters, setters

    public Long getId() {
        return id;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}