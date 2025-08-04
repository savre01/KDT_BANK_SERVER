package com.bank.server.controller;

import com.bank.server.dto.user.UserSummaryResponse;
import com.bank.server.model.User;
import com.bank.server.service.FriendService;
import com.bank.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')") // 필요 시
    public ResponseEntity<String> deleteFriend(@PathVariable Long friendIndex, Authentication authentication) {
        // 현재 로그인한 사용자 ID 추출
        String userId = authentication.getName();
        User user = userService.getUserByUserId(userId);

        return friendService.deleteFriend(user.getUserIndex(), friendIndex)
                .map(f -> ResponseEntity.ok("삭제 완료"))
                .orElse(ResponseEntity.notFound().build());
    }
}