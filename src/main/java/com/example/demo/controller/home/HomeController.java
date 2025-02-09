package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import com.example.demo.domain.user.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/home")
    public Map<String, String> home() {
        return Map.of("message", "Welcome to the public home page");
    }
}
