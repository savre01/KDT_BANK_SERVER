package com.bank.server.controller;

import com.bank.server.dto.user.UserSummaryResponse;
import com.bank.server.service.FriendService;
import com.bank.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;
    // 친구추가
    @PostMapping
    public ResponseEntity<Void> addFriend(@RequestBody Map<String, Long> body, Authentication auth) {
        String userId = auth.getName();
        Long myIndex = userService.getUserByUserId(userId).getUserIndex();
        Long friendIndex = body.get("friendIndex");

        friendService.addFriend(myIndex, friendIndex);
        return ResponseEntity.ok().build();
    }
    //내 친구 조회
    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getFriends(Authentication auth) {
        String userId = auth.getName();
        Long myIndex = userService.getUserByUserId(userId).getUserIndex();

        return ResponseEntity.ok(friendService.getFriends(myIndex));
    }
    //친구 삭제
    @DeleteMapping("/{friendIndex}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long friendIndex, Authentication auth) {
        String userId = auth.getName();
        Long userIndex = userService.getUserByUserId(userId).getUserIndex();

        // 친구 삭제 서비스 호출
        friendService.deleteFriend(userIndex, friendIndex);
        
        return ResponseEntity.noContent().build();  // 204 No Content 응답
    }
}