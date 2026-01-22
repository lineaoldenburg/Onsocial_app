package se.jensen.linea.onsocial_app.model;

import jakarta.persistence.*;

import java.time.Instant;


@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_at", nullable = false)
    private Instant created;

    @Column(name = "post_content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors
    public Post() {
    }

    public Post(Long id, String title, Instant created, String content, User user) {
        this.id = id;
        this.title = title;
        this.created = created;
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