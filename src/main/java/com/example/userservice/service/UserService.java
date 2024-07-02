package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.controller.RegisterRequest; // проверьте этот импорт
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest registerRequest) throws Exception {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) { // используем userRepository
            throw new Exception("Email is already taken");
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setLogin(registerRequest.getLogin());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());
        user.setName(registerRequest.getName());
        return userRepository.save(user);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login); // используем userRepository
    }
}
