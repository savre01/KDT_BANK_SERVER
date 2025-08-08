package com.bank.server.controller.chat;

import com.bank.server.dto.chat.ChatRequest;
import com.bank.server.dto.chat.ChatResponse;
import com.bank.server.dto.chat.ChatSummaryResponse;
import com.bank.server.model.chat.Chat;
import com.bank.server.service.UserService;
import com.bank.server.service.chat.ChatService;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody ChatRequest request, Authentication auth) {
        String userId = auth.getName();
        Long requesterIndex = userService.getUserByUserId(userId).getUserIndex();
        Chat chat = chatService.createChat(request.getName(), requesterIndex, request.getMember());
        return ResponseEntity.ok(chat);
    }

    // 채팅방 목록 (DTO 반환)
    @GetMapping
    public ResponseEntity<List<ChatResponse>> getChats() {
        return ResponseEntity.ok(chatService.getAllChatResponses());
    }

    // 채팅방 삭제
    @DeleteMapping("/{chatIndex}")
    public ResponseEntity<String> deleteChat(@PathVariable Long chatIndex) {
        return chatService.deleteChat(chatIndex)
                .map(chat -> ResponseEntity.ok("삭제 완료"))
                .orElse(ResponseEntity.notFound().build());
    }
    //채팅방 상세 조회
    @GetMapping("/{chatIndex}")
    public ResponseEntity<?> getChatDetail(@PathVariable Long chatIndex) {
        return chatService.getChatDetailOptional(chatIndex)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //내 채팅방 확인
    @GetMapping("/me")
    public ResponseEntity<List<ChatSummaryResponse>> getMyChats(Principal principal) {
        String userId = principal.getName();
        return ResponseEntity.ok(chatService.getChatsByUser(userId));
    }

    @PostMapping("/{chatIndex}/enter")
    public ResponseEntity<String> enterChat(@PathVariable Long chatIndex, Authentication authentication) {
        String userId = authentication.getName();
        Long userIndex = userService.getUserByUserId(userId).getUserIndex();

        chatService.enterChat(chatIndex, userIndex);  // 접속 상태 등록
        return ResponseEntity.ok("채팅방 입장 처리 완료");
    }

    // 채팅방 퇴장
    @PostMapping("/{chatIndex}/leave")
    public ResponseEntity<String> leaveChat(@PathVariable Long chatIndex, Authentication authentication) {
        String userId = authentication.getName();
        Long userIndex = userService.getUserByUserId(userId).getUserIndex();

        chatService.leaveChat(chatIndex, userIndex);  // 접속 상태 제거
        return ResponseEntity.ok("채팅방 퇴장 처리 완료");
    }
}
