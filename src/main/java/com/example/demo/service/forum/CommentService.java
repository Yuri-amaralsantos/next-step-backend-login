package com.example.demo.service.forum;  // Ensure this package matches your folder structure

import com.example.demo.domain.forum.Comment;  // Ensure Comment model exists
import com.example.demo.domain.forum.Post;  // Ensure Post model exists
import com.example.demo.forum.repository.CommentRepository;
import com.example.demo.forum.service.ForumPostService;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ForumPostService forumPostService;

    public CommentService(CommentRepository commentRepository, ForumPostService forumPostService) {
        this.commentRepository = commentRepository;
        this.forumPostService = forumPostService;
    }

    public Comment addComment(Long postId, String content, String username) {
        Post post = forumPostService.getPostById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUsername(username);
        comment.setContent(content);

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }
}
