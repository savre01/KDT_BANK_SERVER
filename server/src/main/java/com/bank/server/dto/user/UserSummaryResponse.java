package com.bank.server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSummaryResponse {
    private Long userIndex;
    private String userName;
    private String department;
    private String position;
}
