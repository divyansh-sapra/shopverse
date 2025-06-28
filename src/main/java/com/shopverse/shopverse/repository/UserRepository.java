package com.shopverse.shopverse.repository;

import com.shopverse.shopverse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = """
            with allUsers as (
                select * from users where id=:id
            )
            select * from allUsers
            """, nativeQuery = true)
    User findUserByIdValue(@Param("id") Long id);
    
}