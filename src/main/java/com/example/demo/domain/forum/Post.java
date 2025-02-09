package com.example.demo.domain.forum;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    public Post() {
    }

    public Post(Long id, String title, String content, String username, Date createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getUsername() { return username; }
    public Date getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setUsername(String username) { this.username = username; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
