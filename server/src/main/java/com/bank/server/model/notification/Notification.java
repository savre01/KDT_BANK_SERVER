package com.bank.server.model.notification;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // 알림 대상자

    private String type; // NOTICE, PRODUCT, CHAT 등

    private String message;

    private Long referenceId;

    private LocalDateTime createdAt;

    @Column(name = "is_read")
    private boolean isRead = false;
}
