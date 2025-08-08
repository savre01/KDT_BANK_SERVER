package com.bank.server.controller.notification;

import com.bank.server.model.User;
import com.bank.server.model.notification.Notification;
import com.bank.server.repository.notification.NotificationRepository;
import com.bank.server.service.notification.NotificationService;
import com.bank.server.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Notification>> getUnreadNotifications(Authentication authentication) {
        String userId = authentication.getName();
        User user = userService.getUserByUserId(userId);

        List<Notification> unread = notificationRepository
                .findByUserIdAndIsReadFalse(user.getUserIndex());

        return ResponseEntity.ok(unread);
    }
    
    
    // 🔹 사용자별 읽지 않은 알림 개수를 유형별로 조회
    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCounts(@RequestParam Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        Map<String, Long> counts = new HashMap<>();

        for (Notification n : notifications) {
            counts.put(n.getType(), counts.getOrDefault(n.getType(), 0L) + 1);
        }

        return counts;
    }

    // 특정 유형의 알림을 모두 읽음 처리
    @PostMapping("/mark-read")
    public ResponseEntity<String> markAllAsReadAndDelete(Authentication authentication) {
        String userId = authentication.getName();
        User user = userService.getUserByUserId(userId);

        // 안 읽은 알림만 조회
        List<Notification> unreadList = notificationRepository.findByUserIdAndIsReadFalse(user.getUserIndex());

        // 읽음 처리
        for (Notification n : unreadList) {
            n.setRead(true);
        }

        // 즉시 삭제
        notificationRepository.deleteAll(unreadList);

        return ResponseEntity.ok("읽은 알림 삭제 완료: " + unreadList.size() + "건");
    }

    @PutMapping("/api/notifications/{id}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}