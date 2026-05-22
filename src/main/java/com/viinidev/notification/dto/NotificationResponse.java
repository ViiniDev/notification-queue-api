package com.viinidev.notification.dto;

import com.viinidev.notification.domain.Notification;
import com.viinidev.notification.domain.NotificationChannel;
import com.viinidev.notification.domain.NotificationStatus;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String recipient,
        String message,
        NotificationChannel channel,
        NotificationStatus status,
        LocalDateTime createdAt,
        LocalDateTime processedAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getRecipient(),
                notification.getMessage(),
                notification.getChannel(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getProcessedAt()
        );
    }
}
