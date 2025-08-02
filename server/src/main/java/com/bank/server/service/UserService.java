package com.bank.server.service;

import com.bank.server.dto.user.AdminUserResponse;
import com.bank.server.dto.user.UserSummaryResponse;
import com.bank.server.model.User;
import com.bank.server.repository.UserRepository;
import com.bank.server.repository.FriendRepository;
import com.bank.server.repository.chat.ChatMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @Transactional
    public void deleteUser(Long userIndex) {
        User user = userRepository.findById(userIndex)
            .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        // 1. 친구 관계 삭제
        friendRepository.deleteByUser(user);
        friendRepository.deleteByFriend(user);

        // 2. 채팅방에서 사용자 삭제
        chatMemberRepository.deleteByUser(user);

        // 3. 사용자 삭제
        userRepository.delete(user);
    }
}