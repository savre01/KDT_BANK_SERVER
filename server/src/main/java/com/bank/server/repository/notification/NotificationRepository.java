package com.bank.server.repository.notification;

import com.bank.server.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndIsReadFalse(Long userId);

    List<Notification> findByUserId(Long userId);

    long countByUserIdAndIsReadFalseAndType(Long userId, String type);

}
