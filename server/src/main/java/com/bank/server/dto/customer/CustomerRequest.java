package com.bank.server.dto.customer;

import lombok.Getter;

@Getter
public class CustomerRequest {
    private String customerId;
    private String customerPassword;
    private String customerName;
    private String customerPhone;
    private String customerBirth;
}