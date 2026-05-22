package com.viinidev.notification.repository;

import com.viinidev.notification.domain.Notification;
import com.viinidev.notification.domain.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStatus(NotificationStatus status);
}
