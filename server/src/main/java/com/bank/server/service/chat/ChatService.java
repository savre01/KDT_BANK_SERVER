package com.bank.server.service.chat;

import com.bank.server.model.User;
import com.bank.server.dto.chat.ChatResponse;
import com.bank.server.dto.chat.ChatSummaryResponse;
import com.bank.server.dto.chat.ChatDetailResponse;
import com.bank.server.dto.chat.ChatMemberResponse;
import com.bank.server.model.chat.Chat;
import com.bank.server.model.chat.ChatMember;
import com.bank.server.repository.chat.ChatRepository;
import com.bank.server.repository.chat.ChatMemberRepository;
import com.bank.server.repository.UserRepository;
import com.bank.server.repository.FriendRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
        
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    // 채팅방 생성
    public Chat createChat(String name, Long requesterIndex, List<Long> memberIndexes) {
    // 멤버가 없으면 채팅방을 만들 수 없도록 처리
        if (memberIndexes == null || memberIndexes.isEmpty()) {
                throw new IllegalArgumentException("채팅방에 최소한 하나의 멤버가 있어야 합니다.");
        }

        // 요청자 정보 가져오기
        User requester = userRepository.findById(requesterIndex)
                .orElseThrow(() -> new IllegalArgumentException("요청자 없음"));

        // 멤버들에 대한 친구 여부 검증 (친구가 아니면 방을 만들지 않음)
        for (Long memberIndex : memberIndexes) {
                User user = userRepository.findById(memberIndex)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

                // 친구가 아닌 사람은 채팅방에 추가 불가
                if (!requester.getUserIndex().equals(user.getUserIndex()) && 
                !friendRepository.existsByUserAndFriend(requester, user)) {
                throw new IllegalArgumentException("친구만 채팅방에 추가할 수 있습니다: ");
                }
        }

        // 친구 관계 검증 후 채팅방 생성
        Chat chat = new Chat();
        chat.setChatName(name);
        chat.setChatCreated(new Date());
        Chat savedChat = chatRepository.save(chat);

        // 본인도 채팅방에 추가
        if (!memberIndexes.contains(requesterIndex)) {
                ChatMember creator = new ChatMember();
                creator.setChat(savedChat);
                creator.setUser(requester);
                creator.setJoined(new Date());
                chatMemberRepository.save(creator);
        }

        // 멤버들 추가
        for (Long memberIndex : memberIndexes) {
                User user = userRepository.findById(memberIndex)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

                // 이미 채팅방에 멤버가 있는지 확인
                if (chatMemberRepository.existsByChatAndUser(savedChat, user)) {
                continue; // 이미 멤버라면 추가하지 않음
                }

                // 새로운 채팅방 멤버 추가
                ChatMember member = new ChatMember();
                member.setChat(savedChat);
                member.setUser(user);
                member.setJoined(new Date());
                chatMemberRepository.save(member);
        }

        return savedChat;
        }

    // 채팅방 삭제
    public Optional<Chat> deleteChat(Long chatIndex) {
        return chatRepository.findById(chatIndex).map(chat -> {
                chatRepository.delete(chat);
                return chat;
        });
        }

    // 전체 채팅방 리스트 DTO 반환
    public List<ChatResponse> getAllChatResponses() {
        return chatRepository.findAll().stream()
                .map(chat -> {
                    List<ChatMemberResponse> memberDTOs = chat.getMembers().stream()
                            .map(member -> {
                                User user = member.getUser();
                                return new ChatMemberResponse(
                                        user.getUserIndex(),
                                        user.getUserId(),
                                        user.getUserName()
                                );
                            })
                            .collect(Collectors.toList());

                    return new ChatResponse(
                            chat.getChatIndex(),
                            chat.getChatName(),
                            chat.getChatCreated(),
                            memberDTOs
                    );
                })
                .collect(Collectors.toList());
    }
    //채팅방 상세 조회
    public Optional<ChatDetailResponse> getChatDetailOptional(Long chatIndex) {
        return chatRepository.findById(chatIndex)
                .map(chat -> {
                List<ChatMemberResponse> members = chatMemberRepository.findByChat(chat).stream()
                        .map(member -> new ChatMemberResponse(
                                member.getUser().getUserIndex(),
                                member.getUser().getUserId(),
                                member.getUser().getUserName()
                        ))
                        .collect(Collectors.toList());

                return new ChatDetailResponse(
                        chat.getChatIndex(),
                        chat.getChatName(),
                        members
                );
                });
        }
    //내 채팅방 조회
    public List<ChatSummaryResponse> getChatsByUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return chatMemberRepository.findByUser(user).stream()
                .map(member -> {
                    Chat chat = member.getChat();
                    return new ChatSummaryResponse(
                            chat.getChatIndex(),
                            chat.getChatName()

                    );
                })
                .collect(Collectors.toList());
    }
}

