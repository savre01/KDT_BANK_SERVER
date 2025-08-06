package com.bank.server.controller.notification;

import com.bank.server.dto.notification.NotificationPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(NotificationPayload payload) {
        messagingTemplate.convertAndSend("/topic/notify", payload);
    }
}
