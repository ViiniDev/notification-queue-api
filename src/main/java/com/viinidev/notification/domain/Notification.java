package com.viinidev.notification.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status = NotificationStatus.QUEUED;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime processedAt;

    protected Notification() {
    }

    public Notification(String recipient, String message, NotificationChannel channel) {
        this.recipient = recipient;
        this.message = message;
        this.channel = channel;
    }

    public Long getId() {
        return id;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void markAsProcessed() {
        this.status = NotificationStatus.PROCESSED;
        this.processedAt = LocalDateTime.now();
    }
}
