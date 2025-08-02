package com.bank.server.service;

import com.bank.server.dto.user.UserSummaryResponse;
import com.bank.server.model.Friend;
import com.bank.server.model.User;
import com.bank.server.repository.FriendRepository;
import com.bank.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public void addFriend(Long userIndex, Long friendIndex) {
        User user = userRepository.findById(userIndex)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
        User friend = userRepository.findById(friendIndex)
                .orElseThrow(() -> new UsernameNotFoundException("친구 없음"));

        if (friendRepository.existsByUserAndFriend(user, friend)) {
            throw new IllegalStateException("이미 친구입니다");
        }

        friendRepository.save(new Friend(null, user, friend));
    }

    public List<UserSummaryResponse> getFriends(Long userIndex) {
        User user = userRepository.findById(userIndex)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        return friendRepository.findByUser(user).stream()
                .map(f -> new UserSummaryResponse(
                        f.getFriend().getUserIndex(),
                        f.getFriend().getUserName(),
                        f.getFriend().getDepartment(),
                        f.getFriend().getPosition()
                        ))
                .collect(Collectors.toList());
    }

     public void deleteFriend(Long userIndex, Long friendIndex) {
        User user = userRepository.findById(userIndex)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
        User friend = userRepository.findById(friendIndex)
                .orElseThrow(() -> new UsernameNotFoundException("친구 없음"));

        // 친구 관계가 존재하는지 확인
        if (!friendRepository.existsByUserAndFriend(user, friend)) {
            throw new IllegalArgumentException("친구 관계가 존재하지 않습니다");
        }
        // 친구 관계 삭제
        Friend friendRelationship = friendRepository.findByUserAndFriend(user, friend)
                .orElseThrow(() -> new IllegalArgumentException("친구 관계를 찾을 수 없습니다"));
        
        friendRepository.delete(friendRelationship);
    }
}
