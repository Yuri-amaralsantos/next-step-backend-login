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

    public ForumPostController(ForumPostService forumPostService, CommentService commentService, UserService userService) {
        this.forumPostService = forumPostService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(forumPostService.getAllPosts());
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        return forumPostService.getPostById(postId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody Comment comment, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        Comment savedComment = commentService.addComment(postId, comment.getContent(), username);
        return ResponseEntity.ok(savedComment);
    }
}
