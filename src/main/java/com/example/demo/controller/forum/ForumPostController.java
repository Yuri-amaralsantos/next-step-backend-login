package com.example.demo.controller;

import com.example.demo.domain.Post;
import com.example.demo.service.ForumPostService;
import com.example.demo.service.user.UserService;
import com.example.demo.domain.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/forum")
public class ForumPostController {

    private final ForumPostService forumPostService;
    private final UserService userService;

    public ForumPostController(ForumPostService forumPostService, UserService userService) {
        this.forumPostService = forumPostService;
        this.userService = userService;
    }

    // Endpoint to create a post
    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody Post postRequest, Authentication authentication) {
        // Retrieve user from authentication
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create post with the retrieved username
        Post post = forumPostService.createPost(postRequest.getTitle(), postRequest.getContent(), user.getUsername());

        return ResponseEntity.ok(post);
    }

    // Endpoint to get all posts (only authenticated users)
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // Unauthorized if no authentication
        }

        List<Post> posts = forumPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // Endpoint to get a single post by ID (only authenticated users)
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // Unauthorized if no authentication
        }

        return forumPostService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
