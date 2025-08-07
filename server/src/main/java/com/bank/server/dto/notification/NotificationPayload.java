package com.bank.server.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPayload {
     private Long id;
    private String type; // 예: "NOTICE", "PRODUCT", "CHAT"
    private String message; // 사용자에게 표시할 메시지
    private Long referenceId; // 관련 엔티티의 ID (예: 공지사항 ID, 상품 ID, 채팅방 ID 등)
    private LocalDateTime createdAt;
}
