package com.example.demo.service.user;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.Category;
import com.example.demo.domain.user.Role;
import com.example.demo.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
    }

    public User registerUser(String username, String email, String password, List<String> categories) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Convert list of strings to a set of Category enums
        Set<Category> categorySet = categories.stream()
                .map(String::trim)
                .map(Category::valueOf)
                .collect(Collectors.toSet());

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        user.setCategories(categorySet);

        return userRepository.save(user);
    }

    public Optional<User> authenticateUser(String username, String password) {
    Optional<User> userOpt = userRepository.findByUsername(username);
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        return matches ? Optional.of(user) : Optional.empty();
    }
    return Optional.empty();
}
}
