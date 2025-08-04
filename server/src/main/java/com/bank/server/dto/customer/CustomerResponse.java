package com.bank.server.dto.customer;

import com.bank.server.model.customer.Customer;
import lombok.Getter;

@Getter
public class CustomerResponse {
    private Long customerIndex;
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String customerBirth;

    public CustomerResponse(Customer c) {
        this.customerIndex = c.getCustomerIndex();
        this.customerId = c.getCustomerId();
        this.customerName = c.getCustomerName();
        this.customerPhone = c.getCustomerPhone();
        this.customerBirth = c.getCustomerBirth();
    }
}