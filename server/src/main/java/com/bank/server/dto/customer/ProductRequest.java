package com.bank.server.dto.customer;

import lombok.Getter;

@Getter
public class ProductRequest {
    private String type;
    private Double rate;
    private String productName;
    private Integer duration;
}
