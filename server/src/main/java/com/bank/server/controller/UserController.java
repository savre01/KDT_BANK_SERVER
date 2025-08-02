package com.bank.server.controller;
import com.bank.server.dto.user.UserResponse;
import com.bank.server.dto.user.UserSummaryResponse;
import com.bank.server.dto.user.AdminUserResponse;
import org.springframework.security.core.Authentication;

import com.bank.server.model.User;
import com.bank.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    //사원 전체 조회
    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUserSummaries());
    }

    // 특정 사원 상세 조회
    @GetMapping("/{userIndex}")
    public ResponseEntity<AdminUserResponse> getUserByIndex(@PathVariable Long userIndex) {
        return ResponseEntity.ok(userService.getAdminUserResponseByUserIndex(userIndex));
    }
    //사원 개인 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(Authentication authentication) {
        String userId = authentication.getName();
        User user = userService.getUserByUserId(userId);

        if (user.isAdmin()) {
            return ResponseEntity.ok(new AdminUserResponse(user));
        } else {
            return ResponseEntity.ok(new UserResponse(user));
        }
    }

    //사원 삭제
    @DeleteMapping("/{userIndex}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userIndex) {
        userService.deleteUser(userIndex);
        return ResponseEntity.noContent().build(); 
    }
}