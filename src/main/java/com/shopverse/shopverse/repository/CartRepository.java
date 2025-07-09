package com.shopverse.shopverse.repository;

import com.shopverse.shopverse.entity.Cart;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Cart> findByUserId(Long user_id);

    @Modifying
    @Query(value = """
                UPDATE cart
                 set total_amount   = :totalAmount,
                     total_items    = :totalItems,
                     total_products = :totalProducts
                 WHERE id = :cartId 
            """, nativeQuery = true)
    void updateCart(@Param("cartId") Long cartId, @Param("totalAmount") BigDecimal totalAmount, @Param("totalItems") int totalItems, @Param("totalProducts") int totalProducts);
}
