package com.ntd.calculator.auth;

import com.ntd.calculator.auth.dto.UserRequest;
import com.ntd.calculator.auth.entity.User;
import com.ntd.calculator.auth.service.AuthService;

import jakarta.validation.Valid;

import com.ntd.calculator.auth.util.JwtUtil;
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
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    // Endpoint to register a new user
    @PostMapping("/users")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        try {
            authService.registerUser(request.getUsername(), request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
    
        // Validate input
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username and password are required"));
        }
    
        // Authenticate user using UserService
        Optional<User> user = authService.authenticate(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }
    
        // Generate JWT token
        String token = jwtUtil.generateToken(username);
    
        // Return token
        return ResponseEntity.ok(Map.of("token", token));
    }
    
}
