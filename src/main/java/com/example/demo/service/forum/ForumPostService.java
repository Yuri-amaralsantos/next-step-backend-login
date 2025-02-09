package com.example.demo.forum.service;

import com.example.demo.domain.forum.Post;
import com.example.demo.repository.forum.ForumPostRepository;
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

    public List<Post> searchPosts(String query) {
    return forumPostRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
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
