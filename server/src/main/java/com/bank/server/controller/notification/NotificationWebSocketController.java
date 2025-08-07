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

    public void sendNotification(NotificationPayload payload) {
        messagingTemplate.convertAndSend("/topic/notify", payload);
    }

    @PostMapping("/api/test/notify")
    public String testNotify(@RequestBody String message) {
        NotificationPayload payload = new NotificationPayload(
            null,                // id (서버에서 자동 생성 가능)
            "NOTICE",            // type
            message,             // message
            null,                // referenceId
            LocalDateTime.now()  // createdAt
        );
        sendNotification(payload);
        return "알림 전송 완료: " + message;
    }
}
