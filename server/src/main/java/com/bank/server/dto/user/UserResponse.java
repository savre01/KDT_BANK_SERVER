package com.bank.server.dto.user;

import com.bank.server.model.User;

public class UserResponse {
    private Long userIndex;
    private String userId;
    private String userName;
    private String userPhone;
    //private String userBirth;
    private String department;
    private String position;

    public UserResponse(User user) {
        this.userIndex = user.getUserIndex();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userPhone = user.getUserPhone();
        //this.userBirth = user.getUserBirth();
        this.department = user.getDepartment();
        this.position = user.getPosition();
    }

    // Getters
    public Long getUserIndex() {
        return userIndex;
    }
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    //public String getUserBirth() {
    //    return userBirth;
    //}

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }
}
