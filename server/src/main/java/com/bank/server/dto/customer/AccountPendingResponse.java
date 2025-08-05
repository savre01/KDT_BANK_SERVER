package com.bank.server.dto.customer;

import com.bank.server.model.customer.Account;
import lombok.Getter;

@Getter
public class AccountPendingResponse {
    private Long accountIndex;
    private String accountNum;
    private String customerName;
    private String customerBirth;
    private String productName;

    public AccountPendingResponse(Account a) {
        this.accountIndex = a.getAccountIndex();
        this.accountNum = a.getAccountNum();
        this.customerName = a.getCustomer().getCustomerName();
        this.customerBirth = a.getCustomer().getCustomerBirth();
        this.productName = a.getProduct() != null ? a.getProduct().getProductName() : null;
    }
}
