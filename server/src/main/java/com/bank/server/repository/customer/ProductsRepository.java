package com.bank.server.repository.customer;

import com.bank.server.model.customer.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findByProductType(String type);
    List<Products> findByInterestRateGreaterThanEqual(Double rate);
}
