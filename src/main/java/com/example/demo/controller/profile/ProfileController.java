package com.example.demo.controller;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.Category;
import com.example.demo.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class ProfileController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error", "User is not authenticated"));
        }

        Optional<User> userOpt = userService.findByUsername(principal.getName());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            
            List<String> categories = Optional.ofNullable(user.getCategories())
                                              .orElse(Set.of())
                                              .stream()
                                              .map(cat -> cat != null ? cat.name() : "Unknown")
                                              .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("categories", categories);

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Map.of("error", "User not found"));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        List<String> categories = Arrays.stream(Category.values()) // FIX: Ensure correct import
                                        .map(Enum::name)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody Map<String, Object> updates) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User is not authenticated"));
        }

        Optional<User> userOpt = userService.findByUsername(principal.getName());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            updates.forEach((key, value) -> {
                switch (key) {
                    case "username":
                        user.setUsername((String) value);
                        break;
                    case "email":
                        user.setEmail((String) value);
                        break;
                    case "password":
                        if (value != null && !((String) value).isEmpty()) {
                            user.setPassword(passwordEncoder.encode((String) value)); // FIX: Ensure passwordEncoder is injected
                        }
                        break;
                    case "categories":
                        if (value instanceof List<?>) {
                            List<String> categoryNames = (List<String>) value;
                            Set<Category> updatedCategories = categoryNames.stream()
                                .map(Category::valueOf)
                                .collect(Collectors.toSet());
                            user.setCategories(updatedCategories);
                        }
                        break;
                }
            });

            userService.save(user);
            return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
    }
}
