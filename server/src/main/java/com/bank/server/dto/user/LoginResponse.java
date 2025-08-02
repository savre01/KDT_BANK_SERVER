// dto/LoginResponse.java
package com.bank.server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long userIndex; 
    private String userName;
    private boolean admin;
}
