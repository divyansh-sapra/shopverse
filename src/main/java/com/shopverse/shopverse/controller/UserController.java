package com.shopverse.shopverse.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.shopverse.shopverse.dto.*;
import com.shopverse.shopverse.entity.User;
import com.shopverse.shopverse.repository.UserRepository;
import com.shopverse.shopverse.security.JwtService;
import com.shopverse.shopverse.security.TokenBlacklistService;
import com.shopverse.shopverse.security.TokenStore;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.shopverse.shopverse.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserRepository userRepository;
    private final TokenStore tokenStore;

    @Value("${custom.ADMIN_KEY}")
    String adminKey;

    public UserController(UserService service, JwtService jwtService, TokenBlacklistService tokenBlacklistService, UserRepository userRepository, TokenStore tokenStore) {
        this.service = service;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.userRepository = userRepository;
        this.tokenStore = tokenStore;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String key, @Valid @RequestBody UserRequest req) {
        if (req.role().equals("ADMIN") && !key.equals(adminKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Admin Key");
        }
        return ResponseEntity.ok(service.registerUser(req));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return service.getUserByEmail(email)
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

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer")) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("FAILURE", "No Token Provided", null));
        }

        String token = authorization.substring(7);
        long expiry = jwtService.getTokenExpiryMillis(token);
        tokenBlacklistService.blacklistToken(token, expiry);

        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "", "User Logged Out Successfully"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<String>> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken= body.get("refreshToken");
        if(refreshToken==null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("FAILED","Invalid Refresh Token",null));
        }

        String email = jwtService.extractEmail(refreshToken);
        if(!jwtService.isTokenValid(refreshToken, email) && !tokenStore.isRefreshTokenValid(email, refreshToken)){
            return ResponseEntity.badRequest().body(new ApiResponse<>("FAILED","Invalid Refresh Token",null));
        }

        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()){
            return ResponseEntity.badRequest().body(new ApiResponse<>("FAILED","Invalid Refresh Token",null));
        }
        String newToken = jwtService.generateToken(email, user.get().getRole());
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "", newToken));
    }

    @PatchMapping("/update-user")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@Valid @RequestBody UpdateRequest updateRequest) {
        UserResponse userResponse = service.updateUser(updateRequest.email(), updateRequest);
        return ResponseEntity.ok(new ApiResponse<>("success","", userResponse));
    }

    @DeleteMapping("/delete-user/{email}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String email) {
        service.deleteUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>("Success","", "User Deleted Successfully"));
    }
}
