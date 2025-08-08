package com.bank.server.controller.notification;

import com.bank.server.dto.notification.NotificationPayload;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    // 사용자별 WebSocket 채널로 알림 전송
    public void sendNotificationToUser(Long userId, NotificationPayload payload) {
        messagingTemplate.convertAndSend("/topic/notify/" + userId, payload);
    }

    // 테스트용 전역 broadcast (기존 유지)
    @PostMapping("/api/test/notify")
    public String testNotify(@RequestBody String message) {
        NotificationPayload payload = new NotificationPayload(
            null,                // id (서버에서 자동 생성 가능)
            "NOTICE",            // type
            message,             // message
            null,                // referenceId
            LocalDateTime.now()  // createdAt
        );
        messagingTemplate.convertAndSend("/topic/notify", payload); // 테스트는 broadcast 유지
        return "알림 전송 완료: " + message;
    }
}
