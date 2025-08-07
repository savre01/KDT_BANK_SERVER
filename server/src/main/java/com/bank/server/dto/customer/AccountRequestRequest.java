package com.bank.server.dto.customer;

import com.bank.server.model.customer.AccountRequestEntity.RequestType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequestRequest {

    private Long customerIndex;
    private Long productsIndex;
    private Long accountIndex;
    private RequestType requestType;
    private String accountNum;
    private String accountPassword;
    private BigDecimal accountBalance;
    private Integer paymentDay;
}
