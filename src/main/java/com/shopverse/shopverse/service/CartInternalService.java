package com.shopverse.shopverse.service;

import com.shopverse.shopverse.RepoResponse.CartItemSummary;
import com.shopverse.shopverse.entity.Cart;
import com.shopverse.shopverse.entity.CartItem;
import com.shopverse.shopverse.entity.Product;
import com.shopverse.shopverse.entity.User;
import com.shopverse.shopverse.repository.CartItemRepository;
import com.shopverse.shopverse.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartInternalService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartInternalService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public boolean addUpdateItemToCart(User user, Product product, int quantity) {
        Cart userCart = cartRepository.findByUserId(user.getId()).orElseThrow();
        long productId = product.getId();
        BigDecimal totalAmount = BigDecimal.valueOf(product.getPrice().doubleValue() * quantity);
        Optional<CartItem> byProductId = cartItemRepository.findByProductIdAndCartId(productId, userCart.getId());
        if (byProductId.isEmpty()) {
            CartItem cartItem = CartItem.builder()
                    .cartId(userCart.getId())
                    .price(product.getPrice())
                    .productId(productId)
                    .quantity(quantity)
                    .subtotal(totalAmount)
                    .build();
            cartItemRepository.save(cartItem);
        } else {
            BigDecimal previousSubTotal = byProductId.get().getSubtotal();
            int previousQuantity = byProductId.get().getQuantity();
            if (previousQuantity + quantity > product.getQuantity())
                return false;
            CartItem cItem = byProductId.orElseThrow();
            cItem.setQuantity(previousQuantity + quantity);
            cItem.setSubtotal(BigDecimal.valueOf(previousSubTotal.doubleValue() + totalAmount.doubleValue()));
            cartItemRepository.save(cItem);
        }
        //select cart items call
        CartItemSummary cartItemSummary = cartItemRepository.cartItemSummary(userCart.getId());
        //update cart call
        cartRepository.updateCart(userCart.getId(), cartItemSummary.getTotalAmount(), cartItemSummary.getTotalQuantities(), cartItemSummary.getProductIds());
        return true;
    }
}
