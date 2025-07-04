package com.shopverse.shopverse.service;

import java.util.List;
import java.util.Optional;

import com.shopverse.shopverse.security.TokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopverse.shopverse.dto.LoginRequest;
import com.shopverse.shopverse.dto.LoginResponse;
import com.shopverse.shopverse.dto.UserRequest;
import com.shopverse.shopverse.dto.UserResponse;
import com.shopverse.shopverse.entity.User;
import com.shopverse.shopverse.repository.UserRepository;
import com.shopverse.shopverse.security.JwtService;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;
    private final TokenStore tokenStore;

    @Value("${custom.username}")
    String username;

    @Value("${custom.password}")
    String password;

    @PostConstruct
    public void init() {
        System.out.println(this.username + "" + this.password);
    }

    public UserService(UserRepository userRepo, BCryptPasswordEncoder encoder, JwtService jwtService, TokenStore tokenStore) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.tokenStore=tokenStore;
    }

    public String registerUser(UserRequest req) {
        if (userRepo.findByEmail(req.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(req.name())
                .email(req.email())
                .password(encoder.encode(req.password())) // plain for now; will hash later
                .role(req.role())
                .build();

        userRepo.save(user);
        return "User registered successfully";
    }

    public Optional<UserResponse> getUserByEmail(String email) {
        return userRepo.findByEmail(email).map(user -> new UserResponse(user.getName(), user.getEmail()));
    }

    public LoginResponse loginUser(LoginRequest loginReq) {
        User user = userRepo.findByEmail(loginReq.email()).orElseThrow(() -> new RuntimeException("Invalid Email or Password"));
        String message;
        String token;
        String role;
        String refreshToken;
        if (!encoder.matches(loginReq.password(), user.getPassword())) {
            message = "Password does not match";
            token = "";
            role = "";
            refreshToken="";
        } else {
            message = "Logged in successfully";
            token = jwtService.generateToken(user.getEmail(), user.getRole());
            refreshToken = jwtService.generateRefreshToken(user.getEmail());
            tokenStore.storeRefreshToken(user.getEmail(), refreshToken);
            role = user.getRole();
        }

        return new LoginResponse(message, token, refreshToken, role);
    }

    public List<UserResponse> getAllUsers() {
        return userRepo.findAll().stream().map(user -> new UserResponse(user.getName(), user.getEmail())).toList();
    }
}
