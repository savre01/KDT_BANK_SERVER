package com.bank.server.controller.chat;

import com.bank.server.dto.chat.ChatMessagePayload;
import com.bank.server.dto.chat.ChatMessageResponse;
import com.bank.server.model.User;
import com.bank.server.model.chat.Chat;
import com.bank.server.model.chat.ChatMember;
import com.bank.server.model.chat.ChatMessage;
import com.bank.server.repository.UserRepository;
import com.bank.server.repository.chat.ChatMemberRepository;
import com.bank.server.repository.chat.ChatMessageRepository;
import com.bank.server.repository.chat.ChatRepository;
import com.bank.server.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void handleChatMessage(@Payload ChatMessagePayload payload) {

        Chat chat = chatRepository.findById(payload.getChatIndex())
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다"));

        User user = userRepository.findById(payload.getUserIndex())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));

        ChatMessage message = new ChatMessage();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(payload.getContent());
        message.setSentTime(new Date());

        ChatMessage saved = chatMessageRepository.save(message);

        ChatMessageResponse response = new ChatMessageResponse(
                saved.getMessageIndex(),
                saved.getContent(),
                saved.getSentTime(),
                user.getUserIndex(),
                user.getUserName()
        );

        messagingTemplate.convertAndSend("/topic/chat/" + payload.getChatIndex(), response);

        // 💬 알림 전송
        List<ChatMember> members = chatMemberRepository.findByChat(chat);
        for (ChatMember member : members) {
            Long memberId = member.getUser().getUserIndex();
            if (!memberId.equals(user.getUserIndex())) {
                notificationService.notifyChatMessage(chat.getChatIndex(), memberId);
            }
        }
    }
}