package com.shopverse.shopverse.service;

import com.shopverse.shopverse.dto.ApiResponse;
import com.shopverse.shopverse.dto.GetAllProductResponse;
import com.shopverse.shopverse.dto.ProductRequest;
import com.shopverse.shopverse.entity.Product;
import com.shopverse.shopverse.entity.User;
import com.shopverse.shopverse.repository.ProductRepository;
import com.shopverse.shopverse.utility.HelperMethods;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private HelperMethods helperMethods;
    private UserService userService;

    public ProductService(ProductRepository productRepository, HelperMethods helperMethods, UserService userService) {
        this.productRepository = productRepository;
        this.helperMethods = helperMethods;
        this.userService = userService;
    }

    public Optional<Product> addProduct(ProductRequest request) {
        Optional<User> user = helperMethods.getUserByToken();
        if (!user.get().getRole().toUpperCase().equals(new String("ADMIN"))) {
            return Optional.empty();
        }
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .quantity(request.quantity())
                .created_by(user.get().getId())
                .build();
        productRepository.save(product);
        return Optional.ofNullable(product);
    }

    public List<GetAllProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map((product -> new GetAllProductResponse(product.getName(), product.getPrice(), product.getQuantity()))).toList();
    }

    public Optional<Product> getProductByName(String productName) {
        return productRepository.findByName(productName);
    }

    public Optional<Product> updateProduct(Long productId, ProductRequest productRequest) {
        User user = helperMethods.getUserByToken().orElseThrow();
        Long id = user.getId();
        Product product = productRepository.findById(productId).orElseThrow();
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setQuantity(productRequest.quantity());
        product.setUpdated_by(id);
        productRepository.save(product);
        return Optional.of(product);
    }

    public boolean deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
            return true;
        } else
            return false;
    }
}
