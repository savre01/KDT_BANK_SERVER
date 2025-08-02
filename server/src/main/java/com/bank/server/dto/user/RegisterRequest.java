package com.bank.server.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    private String userId;
    private String userPassword;
    private String userName;
    private String userPhone;
    private String userBirth;
    private String department;
    private String position;
}
