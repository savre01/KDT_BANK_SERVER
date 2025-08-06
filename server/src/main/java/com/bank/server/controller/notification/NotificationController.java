package com.bank.server.controller.notification;

import com.bank.server.model.notification.Notification;
import com.bank.server.repository.notification.NotificationRepository;
import com.bank.server.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;

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

    // 🔹 특정 유형의 알림을 모두 읽음 처리
    @PostMapping("/mark-read")
    public void markAsRead(@RequestParam Long userId, @RequestParam String type) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        for (Notification n : notifications) {
            if (n.getType().equals(type)) {
                n.setRead(true);
            }
        }
        notificationRepository.saveAll(notifications);
    }

    @PutMapping("/api/notifications/{id}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}