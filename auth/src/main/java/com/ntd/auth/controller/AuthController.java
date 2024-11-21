package com.ntd.auth.controller;

import com.ntd.auth.dto.UserRequest;
import com.ntd.auth.entity.User;
import com.ntd.auth.service.AuthService;

import jakarta.validation.Valid;

import com.ntd.shared.util.JwtUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
    
        // Authenticate user using UserService
        Optional<User> user = authService.authenticate(username, password);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }
    
        // Generate JWT token
        String token = jwtUtil.generateToken(user.get().getUsername());
    
        // Return token
        return ResponseEntity.ok(Map.of("token", token));
    }
    
}
