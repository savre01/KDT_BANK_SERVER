package com.bank.server.repository.chat;

import com.bank.server.model.chat.ChatMessage;
import com.bank.server.model.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChat(Chat chat); 

    List<ChatMessage> findByChat_ChatIndex(Long chatIndex);
}
