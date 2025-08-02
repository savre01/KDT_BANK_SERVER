// dto/LoginRequest.java
package com.bank.server.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    private String userId;
    private String userPassword;
}
