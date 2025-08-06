package com.bank.server.dto.customer;

import com.bank.server.model.customer.Account;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {
    private Long customerIndex;
    private Long productsIndex;
    private String accountNum;
    private String accountPassword;
    private BigDecimal accountBalance;
    //private LocalDate accountExpirationDate; 자동 계산
    private Integer paymentDay;
    private Account.AccountStatus accountStatus; //기본값 pending(보류)
}