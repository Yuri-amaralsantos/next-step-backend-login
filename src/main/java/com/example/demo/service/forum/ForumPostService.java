package com.example.demo.service;

import com.example.demo.domain.Post;
import com.example.demo.repository.ForumPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class ForumPostService {

    private final ForumPostRepository forumPostRepository;

    public ForumPostService(ForumPostRepository forumPostRepository) {
        this.forumPostRepository = forumPostRepository;
    }

    public Post createPost(String title, String content, String username) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUsername(username);
        return forumPostRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return forumPostRepository.findAllByOrderByCreatedAtDesc();  // Return posts in reverse order
    }

    public Optional<Post> getPostById(Long id) {
        return forumPostRepository.findById(id);
    }
}
