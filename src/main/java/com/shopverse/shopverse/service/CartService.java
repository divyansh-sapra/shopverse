package com.shopverse.shopverse.service;

import com.shopverse.shopverse.dto.CartRequest;
import com.shopverse.shopverse.entity.Product;
import com.shopverse.shopverse.entity.User;
import com.shopverse.shopverse.repository.CartItemRepository;
import com.shopverse.shopverse.repository.CartRepository;
import com.shopverse.shopverse.repository.ProductRepository;
import com.shopverse.shopverse.repository.UserRepository;
import com.shopverse.shopverse.utility.HelperMethods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartService {

    private final HelperMethods helperMethods;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartInternalService cartInternalService;


    public CartService(HelperMethods helperMethods, UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, CartInternalService cartInternalService) {
        this.helperMethods = helperMethods;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartInternalService = cartInternalService;
    }

    public boolean addToCart(CartRequest cartRequest) {
        try{
            User user = helperMethods.getUserByToken().orElseThrow();
            Product product = productRepository.findById(cartRequest.productId()).orElseThrow();
            if(product.getQuantity() < cartRequest.quantity()) {
                return false;
            }
            return cartInternalService.addUpdateItemToCart(user, product, cartRequest.quantity());
        } catch (Exception e) {
            log.error("e: ", e);
            return false;
        }
    }
}
