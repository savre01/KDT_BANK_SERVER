package com.bank.server.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String phoneNumber;
    private String position;
    private String department;
    private boolean admin;
}
