package com.bank.server.dto.customer;
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
    private LocalDate accountCreateDate;
    //private LocalDate accountExpirationDate; 자동 계산
    private Integer paymentDay;
    //private String accountStatus; 기본값 pending(보류)
}