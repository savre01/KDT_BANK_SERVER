package com.bank.server.dto.customer;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;

@Getter
public class AccountRequest {
    private Long customerIndex;
    private Long productsIndex;
    private String accountNum;
    private BigDecimal accountBalance;
    private LocalDate accountCreateDate;
    private LocalDate accountExpirationDate;
    private Integer paymentDay;
    private String accountStatus;
}