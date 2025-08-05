package com.bank.server.dto.customer;

import com.bank.server.model.customer.Products;
import lombok.Getter;

@Getter
public class ProductResponse {
    private Long productsIndex;
    private String type;
    private Double rate;
    private String productName;
    private Integer duration;

    public ProductResponse(Products p) {
        this.productsIndex = p.getProductsIndex();
        this.type = p.getProductType();
        this.rate = p.getInterestRate();
        this.productName = p.getProductName();
        this.duration = p.getProductsDuration();
    }
}
