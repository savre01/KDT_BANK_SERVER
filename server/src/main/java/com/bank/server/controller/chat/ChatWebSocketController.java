package com.bank.server.controller.chat;

import com.bank.server.dto.chat.ChatMessagePayload;
import com.bank.server.model.User;
import com.bank.server.model.chat.Chat;
import com.bank.server.model.chat.ChatMessage;
import com.bank.server.repository.UserRepository;
import com.bank.server.repository.chat.ChatMessageRepository;
import com.bank.server.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chat/{chatIndex}")
    public ChatMessage handleChatMessage(@Payload ChatMessagePayload payload) {

        Chat chat = chatRepository.findById(payload.getChatIndex())
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다"));

        User user = userRepository.findById(payload.getUserIndex())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));

        ChatMessage message = new ChatMessage();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(payload.getContent());
        message.setSentTime(new Date());

        return chatMessageRepository.save(message);
    }
}
