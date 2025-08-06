package com.bank.server.dto.customer;

import com.bank.server.model.customer.Account;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class AccountResponse {
    private Long accountIndex;
    private String accountNum;
    private BigDecimal accountBalance;
    private LocalDate accountCreateDate;
    private LocalDate accountExpirationDate;
    private String customerName;
    private String productName;

    public AccountResponse(Account a) {
        this.accountIndex = a.getAccountIndex();
        this.accountNum = a.getAccountNum();
        this.accountBalance = a.getAccountBalance();
        this.accountCreateDate = a.getAccountCreateDate();
        this.accountExpirationDate = a.getAccountExpirationDate();
        this.customerName = a.getCustomer().getCustomerName();
        this.productName = a.getProduct() != null ? a.getProduct().getProductName() : null;
    }
}