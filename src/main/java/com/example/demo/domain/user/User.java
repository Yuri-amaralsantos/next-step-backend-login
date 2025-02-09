package com.example.demo.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
@NoArgsConstructor  // Added for JPA
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_categories", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "category")
    private Set<Category> categories = new HashSet<>();

    // Getters and setters (or you can use Lombok's @Getter and @Setter annotations)
    public String getUsername() {
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Set<Category> getCategories() {
        if (categories == null) {
            categories = new HashSet<>();
        }
        return categories;
    }
    public void setCategories(Set<Category> categories) {
        if (categories != null) {
            this.categories = categories;
        } else {
            this.categories = new HashSet<>();
        }
    }
    public void addCategory(Category category) {
        if (category != null) {
            getCategories().add(category);
        }
    }
    public void removeCategory(Category category) {
        if (category != null) {
            getCategories().remove(category);
        }
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
