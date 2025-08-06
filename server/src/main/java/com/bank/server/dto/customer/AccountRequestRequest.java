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

    // 삭제 요청 시 필요한 계좌
    private Long accountIndex;

    private RequestType requestType;

    // 아래는 CREATE 요청에 필요하다면 유지
    private String accountNum;
    private String accountPassword;
    private BigDecimal accountBalance;
    private Integer paymentDay;
}
