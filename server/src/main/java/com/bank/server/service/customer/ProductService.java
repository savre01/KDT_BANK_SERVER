package com.bank.server.service.customer;

import com.bank.server.model.customer.Products;
import com.bank.server.repository.customer.ProductsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductsRepository productsRepository;

    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }

    public Optional<Products> getProductByIdOptional(Long id) {
        return productsRepository.findById(id);
    }

    public Optional<List<Products>> getProductsByTypeOptional(String type) {
        List<Products> result = productsRepository.findByProductType(type);
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    public List<Products> getHighInterestProducts(Double rate) {
        return productsRepository.findByInterestRateGreaterThanEqual(rate);
    }

    public Products createProduct(Products product) {
        return productsRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long productIndex) {
        if (!productsRepository.existsById(productIndex)) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        }
        productsRepository.deleteById(productIndex);
    }
}