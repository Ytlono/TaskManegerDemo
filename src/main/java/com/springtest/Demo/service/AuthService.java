package com.springtest.Demo.service;

import com.springtest.Demo.auth.dto.RegisterDTO;
import com.springtest.Demo.repository.Role;
import com.springtest.Demo.repository.User;
import com.springtest.Demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterDTO registerRequest) {
        System.out.println("Starting registration for user: " + registerRequest.getUsername());

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            System.out.println("User with email already exists: " + registerRequest.getEmail());
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            System.out.println("User with username already exists: " + registerRequest.getUsername());
            throw new RuntimeException("Пользователь с таким username уже существует");
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        System.out.println("Password encoded successfully");

        User user = User.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        System.out.println("User registered successfully: " + savedUser.getUsername());
        
        return savedUser;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }
}
