package com.bank.server.repository.chat;

import com.bank.server.model.chat.Chat;
import com.bank.server.model.User;
import com.bank.server.model.chat.ChatMember;
import com.bank.server.model.chat.ChatMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, ChatMemberId> {
    List<ChatMember> findByUser(User user);
    List<ChatMember> findByChat(Chat chat);
    boolean existsByChatAndUser(Chat chat, User user);
}
