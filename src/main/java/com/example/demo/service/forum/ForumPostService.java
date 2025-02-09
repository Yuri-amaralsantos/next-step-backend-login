package com.example.demo.forum.service;

import com.example.demo.domain.forum.Post;
import com.example.demo.repository.forum.ForumPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumPostService {

    private final ForumPostRepository forumPostRepository;

    public ForumPostService(ForumPostRepository forumPostRepository) {
        this.forumPostRepository = forumPostRepository;
    }

    public List<Post> getAllPosts() {
        return forumPostRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Post> getPostById(Long id) {
        return forumPostRepository.findById(id);
    }
}
