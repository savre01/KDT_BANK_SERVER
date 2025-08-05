package com.bank.server.controller.chat;

import com.bank.server.dto.chat.ChatMessageResponse;
import com.bank.server.model.chat.ChatMessage;
import com.bank.server.service.chat.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // 메시지 저장
    @PostMapping
    public ResponseEntity<ChatMessage> sendMessage(@RequestParam Long chatIndex,
                                                   @RequestParam Long userIndex,
                                                   @RequestParam String content) {
        ChatMessage saved = chatMessageService.saveMessage(chatIndex, userIndex, content);
        return ResponseEntity.ok(saved);
    }

    // 채팅방 내 모든 메시지 조회
    @GetMapping("/{chatIndex}")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(@PathVariable Long chatIndex) {
        return ResponseEntity.ok(chatMessageService.getMessageResponsesByChat(chatIndex));
    }
}
