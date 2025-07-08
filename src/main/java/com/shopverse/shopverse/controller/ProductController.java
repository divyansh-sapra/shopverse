package com.shopverse.shopverse.controller;

import com.shopverse.shopverse.dto.ApiResponse;
import com.shopverse.shopverse.dto.GetAllProductResponse;
import com.shopverse.shopverse.dto.ProductRequest;
import com.shopverse.shopverse.dto.ProductResponse;
import com.shopverse.shopverse.entity.Product;
import com.shopverse.shopverse.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add-product")
    public ResponseEntity<ApiResponse<Optional<Product>>> appProduct(@Valid @RequestBody ProductRequest request) {
        Optional<Product> product = productService.addProduct(request);
        if (product.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>("FAILED", "Product cannot be added", null));
        }
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Product added successfully", product));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<GetAllProductResponse>>> getAllProduct() {
        List<GetAllProductResponse> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "", allProducts));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductByName(@PathVariable String name) {
        Optional<Product> productByName = productService.getProductByName(name);
        if (!productByName.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>("FAILED", "No Data Found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "", new ProductResponse(productByName.get().getName(), productByName.get().getDescription(), productByName.get().getPrice(), productByName.get().getQuantity())));
    }

    @PutMapping("/update-product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        Optional<Product> productByName = productService.updateProduct(id, productRequest);
        if (!productByName.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>("FAILED", "No Data Found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "", new ProductResponse(productByName.get().getName(), productByName.get().getDescription(), productByName.get().getPrice(), productByName.get().getQuantity())));

    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id){
        boolean b = productService.deleteProduct(id);
        return b?ResponseEntity.ok(new ApiResponse<>("SUCCESS","","User Deleted Successfully")):ResponseEntity.ok(new ApiResponse<>("FAILED","Product Cannot be Deleted",null));
    }

//    DELETE /api/product/delete/{id} – Soft delete product
}
