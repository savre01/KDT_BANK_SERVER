package com.bank.server.service.customer;

import com.bank.server.model.customer.Products;
import com.bank.server.repository.customer.ProductsRepository;
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
}