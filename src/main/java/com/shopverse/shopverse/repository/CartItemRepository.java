package com.shopverse.shopverse.repository;

import com.shopverse.shopverse.RepoResponse.CartItemSummary;
import com.shopverse.shopverse.entity.CartItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CartItem> findByProductIdAndCartId(Long productId, Long cartId);

    @Query(value = """
            select coalesce(sum(subtotal), 0) as totalAmount,
                   count(product_id)          as productIds,
                   coalesce(sum(quantity), 0) as totalQuantities
            from cart_items
            where cart_id = :cartId
              and deleted_at is null;
            """, nativeQuery = true)
    CartItemSummary cartItemSummary(@Param("cartId") Long cartId);
}
