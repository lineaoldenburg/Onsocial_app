package se.jensen.linea.onsocial_app.model;

import jakarta.persistence.*;

import java.time.Instant;


/**
 * Klassen representerar en användares post.
 * Den innehåller de attribut som speglar kolumner i databasen.
 * En post tillhör en user.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-21
 */
@Entity
@Table(name = "posts")
public class Post {

    /**
     * Unikt ID för posten, genereras automatiskt av databasen.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Postens titel, får inte vara tom.
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Tidpunkt när posten skapades, referens till databasens namn "created_at", får inte vara tom.
     */
    @Column(name = "created_at", nullable = false)
    private Instant created;

    /**
     * Postens innehåll, referens till databasens namn "post_content", får inte vara tom.
     */
    @Column(name = "post_content", nullable = false)
    private String content;

    /**
     * Användaren som skapade posten. Många poster kan tillhöra samma användare (Many-to-One relation).
     * Referens till databasens namn "user_id".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors
    public Post() {
    }

    public Post(Long id, String title, String content, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Auto-set creation timestamp
    @PrePersist
    protected void onCreate() {
        this.created = Instant.now();
    }

    // Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getCreated() {
        return created;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}