package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Optional;
import java.util.Date;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Генерация безопасного ключа

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticate(String email, String password) throws Exception {
        logger.info("Attempting to authenticate user with email: {}", email);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("User found in database: {}", user.getEmail());

            if (passwordEncoder.matches(password, user.getPassword())) {
                logger.info("Password match for user: {}", user.getEmail());
                String token = generateToken(user);
                logger.info("Generated token for user: {}", email);
                return token;
            } else {
                logger.error("Password mismatch for user: {}", user.getEmail());
                throw new Exception("Invalid email or password.");
            }
        } else {
            logger.error("User not found in database with email: {}", email);
            throw new Exception("Invalid email or password.");
        }
    }

    private String generateToken(User user) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .claim("role", user.getRole())
                    .claim("userId", user.getId())
                    .setIssuedAt(new Date(currentTimeMillis))
                    .setExpiration(new Date(currentTimeMillis + 3600000)) // 1 hour
                    .signWith(SECRET_KEY)
                    .compact();

            logger.info("Generated token for user {}: {}", user.getEmail(), token);
            return token;
        } catch (Exception e) {
            logger.error("Error generating token for user {}: {}", user.getEmail(), e.getMessage());
            throw e;
        }
    }
}
