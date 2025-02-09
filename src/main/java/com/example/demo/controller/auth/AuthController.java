package com.example.demo.controller.auth;

import com.example.demo.domain.user.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            String email = (String) request.get("email");
            String password = (String) request.get("password");
            List<String> categories = (List<String>) request.get("categories");

            User user = userService.registerUser(username, email, password, categories);
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Optional<User> user = userService.authenticateUser(username, password);
        if (user.isPresent()) {
            String token = jwtUtil.generateToken(username, user.get().getRole().name());
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}
