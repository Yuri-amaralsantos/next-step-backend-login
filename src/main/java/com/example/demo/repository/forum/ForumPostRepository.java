package com.example.demo.repository;

import com.example.demo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumPostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();  // To get posts in reverse chronological order
}
