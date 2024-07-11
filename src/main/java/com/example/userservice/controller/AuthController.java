package com.example.userservice.controller;

import com.example.userservice.service.AuthService;
import com.example.userservice.service.UserService;
import com.example.userservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        logger.info("Login page requested");
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for email: {}", loginRequest.getEmail());
        System.out.println("Login attempt for email: " + loginRequest.getEmail());
        try {
            String token = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            logger.info("Login successful for email: {}", loginRequest.getEmail());
            System.out.println("Login successful for email: " + loginRequest.getEmail());
            return ResponseEntity.ok(new AuthResponse("token: " + token + " - from Java backend"));
        } catch (Exception e) {
            logger.error("Login failed for email: {}", loginRequest.getEmail());
            System.out.println("Login failed for email: " + loginRequest.getEmail());
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid email or password. - from Java backend"));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        logger.info("Test endpoint reached");
        return ResponseEntity.ok("Test endpoint works");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        logger.info("Register attempt for email: {}", registerRequest.getEmail());
        try {
            User newUser = userService.registerUser(registerRequest);
            logger.info("Register successful for email: {}", registerRequest.getEmail());
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            logger.error("Register failed for email: {}", registerRequest.getEmail());
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid input data provided."));
        }
    }
}
