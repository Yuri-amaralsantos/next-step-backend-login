package com.example.demo.controller.forum;

import com.example.demo.domain.forum.Comment;
import com.example.demo.domain.forum.Post;
import com.example.demo.domain.user.User;
import com.example.demo.forum.service.ForumPostService;
import com.example.demo.service.forum.CommentService;


import com.example.demo.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/forum")
public class ForumPostController {

    private final ForumPostService forumPostService;
    private final CommentService commentService;
    private final UserService userService;

    public ForumPostController(ForumPostService forumPostService, CommentService commentService,
            UserService userService) {
        this.forumPostService = forumPostService;
        this.commentService = commentService;
        this.userService = userService;
    }
    
    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody Comment commentRequest, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = commentService.addComment(id, commentRequest.getContent(), user.getUsername());
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(id));
    }

    @GetMapping("/posts/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String query) {
        return ResponseEntity.ok(forumPostService.searchPosts(query));
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
