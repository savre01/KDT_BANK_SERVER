package com.bank.server.service;

import com.bank.server.dto.user.AdminUserResponse;
import com.bank.server.dto.user.UserSummaryResponse;
import com.bank.server.model.User;
import com.bank.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserSummaryResponse> getAllUserSummaries() {
        return userRepository.findAll().stream()
            .map(user -> new UserSummaryResponse(
                user.getUserIndex(),
                user.getUserName(),
                user.getDepartment(),
                user.getPosition()
            ))
            .collect(Collectors.toList());
    }

    public UserSummaryResponse getUserSummaryByIndex(Long userIndex) {
        User user = userRepository.findById(userIndex)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new UserSummaryResponse(
            user.getUserIndex(),
            user.getUserName(),
            user.getDepartment(),
            user.getPosition()
        );
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }
    public AdminUserResponse getAdminUserResponseByUserIndex(Long userIndex) {
    User user = userRepository.findById(userIndex)
            .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));

        return new AdminUserResponse(user);
    }
}