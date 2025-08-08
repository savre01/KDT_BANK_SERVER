package com.bank.server.service.chat;

import com.bank.server.dto.chat.ChatMemberResponse;

import com.bank.server.model.User;
import com.bank.server.model.chat.Chat;
import com.bank.server.model.chat.ChatMember;
import com.bank.server.repository.UserRepository;
import com.bank.server.repository.FriendRepository;
import com.bank.server.repository.chat.ChatMemberRepository;
import com.bank.server.repository.chat.ChatRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatMemberService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final ChatMemberRepository chatMemberRepository;

    public List<ChatMember> addMembers(Long chatIndex, Long requesterIndex, List<Long> memberIndexes) {
        Chat chat = chatRepository.findById(chatIndex)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        User requester = userRepository.findById(requesterIndex)
                .orElseThrow(() -> new IllegalArgumentException("요청자 정보 없음"));

        return memberIndexes.stream().map(userIndex -> {
                User user = userRepository.findById(userIndex)
                        .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

                // 친구 여부 확인
                if (!friendRepository.existsByUserAndFriend(requester, user)) {
                throw new IllegalArgumentException("친구만 채팅방에 추가할 수 있습니다: " + user.getUserName());
                }

                // 중복 방지
                if (chatMemberRepository.existsByChatAndUser(chat, user)) {
                return null; 
                }

                ChatMember member = new ChatMember();
                member.setChat(chat);
                member.setUser(user);
                member.setJoined(new Date());

                return chatMemberRepository.save(member);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
    public List<ChatMember> getMembersByChat(Long chatIndex) {
        Chat chat = chatRepository.findById(chatIndex)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        return chatMemberRepository.findByChat(chat);
    }

    public List<ChatMemberResponse> getMemberResponses(Long chatIndex) {
    Chat chat = chatRepository.findById(chatIndex)
            .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다"));

    return chatMemberRepository.findByChat(chat).stream()
            .map(member -> {
                User user = member.getUser();
                return new ChatMemberResponse(
                        user.getUserName(),
                        user.getDepartment(),
                        user.getPosition()
                );
            })
            .collect(Collectors.toList());
    }
     @Transactional
     public boolean deleteMember(Long chatIndex, Long userIndex) {
        Chat chat = chatRepository.findById(chatIndex)
                .orElse(null);
        User user = userRepository.findById(userIndex)
                .orElse(null);

        if (chat == null || user == null) {
                return false;
        }

        // 채팅방에서 사용자 제거
        chatMemberRepository.deleteByUserAndChat(user, chat);

        // 남은 멤버 수 확인 → 채팅방 제거 조건
        int remaining = chatMemberRepository.countByChat_ChatIndex(chatIndex);
        if (remaining == 0) {
                chatRepository.delete(chat);
        }
        return true;
        }
}
