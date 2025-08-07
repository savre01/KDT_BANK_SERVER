package com.bank.server.controller.customer;

import com.bank.server.dto.customer.ProductRequest;
import com.bank.server.dto.customer.ProductResponse;
import com.bank.server.model.User;
import com.bank.server.model.customer.Products;
import com.bank.server.service.UserService;
import com.bank.server.service.customer.ProductService;
import com.bank.server.service.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(
            productService.getAllProducts().stream()
                .map(ProductResponse::new)
                .toList()
        );
    }

    @GetMapping("/{productsIndex}")
    public ResponseEntity<?> getProductById(@PathVariable Long productsIndex) {
        return productService.getProductByIdOptional(productsIndex)
                .map(ProductResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request,
                                                         Authentication authentication) {
        // 1. 로그인한 관리자 정보 가져오기
        String userId = authentication.getName();
        User user = userService.getUserByUserId(userId);

        // 2. 상품 생성
        Products product = new Products();
        product.setProductType(request.getType());
        product.setInterestRate(request.getRate());
        product.setProductName(request.getProductName());
        product.setProductsDuration(request.getDuration());

        Products saved = productService.createProduct(product);

        // 3. 알림 전송
        notificationService.notifyProductCreated(saved.getProductsIndex(), user.getUserIndex());

        return ResponseEntity.ok(new ProductResponse(saved));
    }

    @DeleteMapping("/{productIndex}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productIndex) {
        try {
            productService.deleteProduct(productIndex);
            return ResponseEntity.ok("상품이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
