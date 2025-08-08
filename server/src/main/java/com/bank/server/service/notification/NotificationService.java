package com.bank.server.service.notification;

import com.bank.server.controller.notification.NotificationWebSocketController;
import com.bank.server.dto.notification.NotificationPayload;
import com.bank.server.model.notification.Notification;
import com.bank.server.model.chat.Chat;
import com.bank.server.model.chat.ChatMember;
import com.bank.server.repository.notification.NotificationRepository;
import com.bank.server.repository.chat.ChatMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bank.server.service.chat.ChatService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationWebSocketController notificationWebSocketController;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatService chatService;

    public void notifyNoticeCreated(Long noticeId, Long userId) {
        LocalDateTime now = LocalDateTime.now();

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("NOTICE");
        notification.setMessage("새 공지사항이 등록되었습니다.");
        notification.setReferenceId(noticeId);
        notification.setCreatedAt(now);
        notification.setRead(false);
        notificationRepository.save(notification);

        NotificationPayload payload = new NotificationPayload(
                notification.getId(),
                "NOTICE",
                notification.getMessage(),
                noticeId,
                now
        );
        notificationWebSocketController.sendNotification(payload);
    }

    public void notifyProductCreated(Long productId, Long userId) {
        LocalDateTime now = LocalDateTime.now();

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("PRODUCT");
        notification.setMessage("새 상품이 추가되었습니다.");
        notification.setReferenceId(productId);
        notification.setCreatedAt(now);
        notification.setRead(false);
        notificationRepository.save(notification);

        NotificationPayload payload = new NotificationPayload(
                notification.getId(),
                "PRODUCT",
                notification.getMessage(),
                productId,
                now
        );
        notificationWebSocketController.sendNotification(payload);
    }

    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("알림이 존재하지 않습니다."));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void notifyChatMessage(Long chatIndex, Long senderId) {
        Chat chat = new Chat();
        chat.setChatIndex(chatIndex);

        List<ChatMember> members = chatMemberRepository.findByChat(chat);
        System.out.println("💬 채팅방 #" + chatIndex + " 메시지 전송 시작 - 발신자: " + senderId);
        System.out.println("📌 채팅방 참여자 수: " + members.size());

        for (ChatMember member : members) {
            Long memberId = member.getUser().getUserIndex();
            boolean isSender = memberId.equals(senderId);
            boolean isInChat = chatService.isUserInChat(chatIndex, memberId);

            System.out.println("👉 대상 사용자: " + memberId + " | 발신자 여부: " + isSender + " | 접속 중 여부: " + isInChat);

            // 발신자 본인이 아니고, 현재 채팅방에 접속해 있지 않은 경우에만 알림 전송
            if (!isSender && !isInChat) {
                LocalDateTime now = LocalDateTime.now();

                Notification notification = new Notification();
                notification.setUserId(memberId);
                notification.setType("CHAT");
                notification.setMessage("새 채팅 메시지가 도착했습니다.");
                notification.setReferenceId(chatIndex);
                notification.setCreatedAt(now);
                notification.setRead(false);
                notificationRepository.save(notification);

                NotificationPayload payload = new NotificationPayload(
                        notification.getId(),
                        "CHAT",
                        notification.getMessage(),
                        chatIndex,
                        now
                );
                System.out.println("🔔 알림 전송 대상 → 사용자: " + memberId);
                notificationWebSocketController.sendNotification(payload);
            }
        }
    }
}