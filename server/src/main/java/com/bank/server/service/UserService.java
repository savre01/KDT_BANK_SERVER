package com.bank.server.service;

import com.bank.server.dto.user.AdminUserResponse;
import com.bank.server.dto.user.UserSummaryResponse;
import com.bank.server.model.User;
import com.bank.server.repository.UserRepository;
import com.bank.server.repository.FriendRepository;
import com.bank.server.repository.chat.ChatMemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final ChatMemberRepository chatMemberRepository;

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public User updateUser(Long userIndex, String phoneNumber, String position, String department, boolean isAdmin) {
        // 사용자가 존재하는지 확인
        User user = userRepository.findById(userIndex)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 수정할 내용 설정
        user.setUserPhone(phoneNumber);
        user.setPosition(position);
        user.setDepartment(department);
        user.setAdmin(isAdmin);

        return userRepository.save(user);
    }
    @Transactional
    public Optional<User> deleteUser(Long userIndex) {
        return userRepository.findById(userIndex).map(user -> {
            friendRepository.deleteByUser(user);
            friendRepository.deleteByFriend(user);
            chatMemberRepository.deleteByUser(user);
            userRepository.delete(user);
            return user;
        });
    }
}