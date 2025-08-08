package com.bank.server.service.chat;

import com.bank.server.model.chat.*;
import com.bank.server.dto.chat.ChatMessageResponse;
import com.bank.server.model.User;
import com.bank.server.repository.chat.ChatMessageRepository;
import com.bank.server.repository.chat.ChatRepository;
import com.bank.server.repository.chat.ChatMemberRepository;
import com.bank.server.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMemberRepository chatMemberRepository;

    // 메시지 저장
    public ChatMessage saveMessage(Long chatIndex, Long userIndex, String content) {
        Chat chat = chatRepository.findById(chatIndex)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        User user = userRepository.findById(userIndex)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if (!chatMemberRepository.existsByChatAndUser(chat, user)) {
            throw new AccessDeniedException("채팅방에 참여하지 않은 사용자는 메시지를 보낼 수 없습니다.");
        }
        ChatMessage message = new ChatMessage();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(content);
        message.setSentTime(new Date());

        return chatMessageRepository.save(message);
    }

    // 채팅방 내 메시지 전체 조회
    public List<ChatMessageResponse> getMessageResponsesByChat(Long chatIndex) {
        List<ChatMessage> messages = chatMessageRepository.findByChat_ChatIndex(chatIndex);

        return messages.stream()
                .map(msg -> new ChatMessageResponse(
                        msg.getMessageIndex(),
                        msg.getContent(),
                        msg.getSentTime(),
                        msg.getUser().getUserIndex(),
                        msg.getUser().getUserName() // sender
                ))
                .collect(Collectors.toList());
    }
    // 채팅방 내 메시지 전체 삭제
    public void deleteAllMessagesInChat(Long chatIndex) {
        Chat chat = new Chat();
        chat.setChatIndex(chatIndex);
        List<ChatMessage> messages = chatMessageRepository.findByChat(chat);
        chatMessageRepository.deleteAll(messages);
    }
}
