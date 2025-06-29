package com.shopverse.shopverse.service;

import com.shopverse.shopverse.entity.User;
import com.shopverse.shopverse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCacheService {
    @Autowired
    private UserRepository userRepository;

    @Cacheable(value = "users", key = "#email")
    public Optional<User> getUserInfo(String email) {
        System.out.println("Fetching from DB");
        return userRepository.findByEmail(email);
    }
}
