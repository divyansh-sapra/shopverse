package com.shopverse.shopverse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.shopverse.shopverse.dto.GetUserEmailRequest;
import com.shopverse.shopverse.dto.LoginRequest;
import com.shopverse.shopverse.dto.LoginResponse;
import com.shopverse.shopverse.dto.UserRequest;
import com.shopverse.shopverse.dto.UserResponse;
import com.shopverse.shopverse.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;

import com.shopverse.shopverse.dto.ApiResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    @Value("${custom.ADMIN_KEY}")
    String adminKey;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String key, @Valid @RequestBody UserRequest req) {
        if (req.role().equals("ADMIN") && !key.equals(adminKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Admin Key");
        }
        return ResponseEntity.ok(service.registerUser(req));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@Valid @PathVariable GetUserEmailRequest email) {
        return service.getUserByEmail(email.email())
                .map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginReq) {
        return ResponseEntity.ok(service.loginUser(loginReq));
    }

    @GetMapping("/get-all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = service.getAllUsers();
        if (users.isEmpty()) {
            ApiResponse<List<UserResponse>> response = new ApiResponse<>("ERROR", "NO DATA FOUND", null);
            return ResponseEntity.status(404).body(response);
        }
        ApiResponse<List<UserResponse>> response = new ApiResponse<>("SUCCESS", "", users);
        return ResponseEntity.ok(response);
    }

}
