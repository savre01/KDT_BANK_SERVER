package com.bank.server.controller;

import com.bank.server.dto.chat.ChatMemberResponse;
import com.bank.server.dto.chat.ChatMemberRequest;
import com.bank.server.model.chat.ChatMember;
import com.bank.server.service.UserService;
import com.bank.server.service.chat.ChatMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/chat/member")
@RequiredArgsConstructor
public class ChatMemberController {

    private final ChatMemberService chatMemberService;
    private final UserService userService;

     @PostMapping
    public ResponseEntity<List<ChatMember>> addMembers(@RequestBody ChatMemberRequest request, Authentication auth) {
        String userId = auth.getName();
        Long requesterIndex = userService.getUserByUserId(userId).getUserIndex();
        List<ChatMember> members = chatMemberService.addMembers(request.getChatIndex(), requesterIndex, request.getMember());
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{chatIndex}")
    public ResponseEntity<List<ChatMemberResponse>> getMembers(@PathVariable Long chatIndex) {
        return ResponseEntity.ok(chatMemberService.getMemberResponses(chatIndex));
    }

    @DeleteMapping("/{chatIndex}/{userIndex}")
    public String deleteMember(@PathVariable Long chatIndex, @PathVariable Long userIndex) {
        chatMemberService.deleteMember(chatIndex, userIndex);  // 서비스에서 사용자 삭제 처리
        return "User removed from chat";
    }
}
