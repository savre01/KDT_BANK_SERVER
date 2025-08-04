package com.bank.server.controller.customer;

import com.bank.server.dto.customer.ProductRequest;
import com.bank.server.dto.customer.ProductResponse;
import com.bank.server.model.customer.Products;
import com.bank.server.service.customer.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(
            productService.getAllProducts().stream()
                .map(ProductResponse::new)
                .toList()
        );
    }


    @PostMapping("/type")
    public ResponseEntity<?> getProductsByType(@RequestBody ProductRequest request) {
        return productService.getProductsByTypeOptional(request.getType())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/high-interest")
    public ResponseEntity<List<Products>> getHighInterestProducts(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.getHighInterestProducts(request.getRate()));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        Products product = new Products();
        product.setProductType(request.getType());
        product.setInterestRate(request.getRate());
        product.setProductName(request.getProductName());
        product.setProductsDuration(request.getDuration());
        product.setProductDescription(request.getProductDescription());
        return ResponseEntity.ok(new ProductResponse(productService.createProduct(product)));
    }
}