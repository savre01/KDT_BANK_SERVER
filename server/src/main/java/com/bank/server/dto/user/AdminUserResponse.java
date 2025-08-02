package com.bank.server.dto.user;

import com.bank.server.model.User;

public class AdminUserResponse extends UserResponse {
    private boolean admin;

    public AdminUserResponse(User user) {
        super(user);
        this.admin = user.isAdmin();
    }

    public boolean isAdmin() {
        return admin;
    }
}
