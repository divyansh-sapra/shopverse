package com.shopverse.shopverse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "cart_items")
@SQLDelete(sql = "UPDATE cart_items SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class CartItem  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cart_id")
    private long cartId;

    @Column(name = "product_id")
    private long productId;

    private BigDecimal price;

    private int quantity;

    private BigDecimal subtotal;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private LocalDateTime deleted_at;

    @PrePersist
    public void onCreate() {
        created_at = updated_at = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updated_at = LocalDateTime.now();
    }
}
