package se.jensen.linea.onsocial_app.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "alias", unique = true, nullable = false)
    private String alias;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profilePicture")
    private String profilePicture;

    @Column(nullable = false)
    private String role = "USER";

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