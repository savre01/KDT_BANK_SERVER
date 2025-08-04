package com.bank.server.dto.user;

import com.bank.server.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponse {
    private Long userIndex;
    private String userId;
    private String userName;
    private String userPhone;
    private String department;
    private String position;

    public UserResponse(User user) {
        this.userIndex = user.getUserIndex();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userPhone = user.getUserPhone();
        this.department = user.getDepartment();
        this.position = user.getPosition();
    }
}