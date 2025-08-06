package com.bank.server.dto.customer;

import com.bank.server.model.customer.AccountRequestEntity;
import lombok.Getter;

@Getter
public class AccountRequestResponse {
    private Long requestIndex;

    private String customerName;
    private String customerBirth;
    private String productName;
    private String accountNum; // 삭제 요청일 경우에만
    private AccountRequestEntity.RequestType requestType;


    public AccountRequestResponse(AccountRequestEntity entity) {
        this.requestIndex = entity.getRequestIndex();
        this.customerName = entity.getCustomer().getCustomerName();
        this.customerBirth = entity.getCustomer().getCustomerBirth();
        this.productName = entity.getProducts() != null ? entity.getProducts().getProductName() : null;
        this.accountNum = entity.getAccount() != null ? entity.getAccount().getAccountNum() : null;
        this.requestType = entity.getType();
    }
}